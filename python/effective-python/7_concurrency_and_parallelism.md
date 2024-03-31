# **Chapter 7: Concurrency and Parallelism**

並行性と並列性

Concurrency (並行性): 多数の異なることを見かけ上同じ時間に実行

Parallelism (並列性): 多数の異なることを実際に同じ時間に実行

Python makes it easy to write concurrent programs that do many different things seemingly at the same time. Python can also be used to do parallel work through system calls, subprocesses, and C extensions. This chapter covers how to best utilize Python in these subtly different situations.

1. Use `subprocess` to Manage Child Processes
2. Use Threads for Blocking I/O, Avoid for Parallelism
3. Use `Lock` to Prevent Data Races in Threads
4. Use `Queue` to Coordinate Work Between Threads
5. Know How to Recognize When Concurrency Is Necessary
6. Avoid Creating New `Thread` Instances for On-demand Fan-out
7. Understand How Using `Queue` for Concurrency Requires Refactoring
8. Consider `ThreadPoolExecutor` When Threads Are Necessary for Concurrency
9. Achieve Highly Concurrent I/O with Coroutines
10. Know How to Port Threaded I/O to `asyncio`
11. Mix Threads and Coroutines to Ease the Transition to `asyncio`
12. Avoid Blocking the `asyncio` Event Loop to Maximize Responsiveness
13. Consider `concurrent.futures` for True Parallelism

## 52 Use `subprocess` to Manage Child Processes

subprocessを使って子プロセスを管理する。

Pythonでサブプロセスを管理素には、 `os.popen` , `os.execc*` 等があるが、 `subprocess` を使うことが最良の選択肢。

```python
import subprocess

result = subprocess.run(
    ['echo', 'Hello from the child!'],
    capture_output=True,
    encoding='utf-8')

result.check_returncode()
print(result.stdout)

>>>
Hello from the child!
```

上記 `run` 関数の代わりに `Popen` を使って子プロセスを作ると、子プロセスの状態を定期的にポーリングしてチェックできる。

```python
proc = subprocess.Popen(['sleep', '1'])
while proc.poll() is None:
    print('Working...')
    import time
    time.sleep(0.3)

print('Exit status', proc.poll())

>>>
Working...
Working...
Working...
Working...
Exit status 0
```

`communicate` メソッドに `timeout` 引数を渡すことで、うまく動作していない子プロセスを停止できる。

```python
proc = subprocess.Popen(['sleep', '10'])
try:
    proc.communicate(timeout=0.1)
except subprocess.TimeoutExpired:
    proc.terminate()
    proc.wait()

print('Exit status', proc.poll())

>>>
Exit status -15
```

`-15` は子プロセスがシグナル15 (SIGTERM) によって中止させられたことを表す。

## 53. Use Threads for Blocking I/O, Avoid for Parallelism

スレッドはブロッキングI/Oで使い、並列性のために使うのは避ける。

グローバルインタプリタロック (GIL) は、割り込みを防ぐ相互排他ロックの仕組み。PythonはこのGILによって複数スレッド下でもロックを持つ1つのスレッドしかバイトコード実行をできない。

ブロッキングI/O (ファイルの書き込み、ネットワーク通信、外部機器との通信など) の間に計算などの処理を同時にしたい場合、Pythonスレッドは役立つ。

## 54. Use `Lock` to Prevent Data Races in Threads

スレッドのデータ競合を防ぐために `Lock` を使う。

スレッド間で同じオブジェクトに同時にアクセスすることによるデータ競合は、GILではなくプログラマ側の責任。

そのために組み込みモジュール `threading` の `Lock` クラスを使い、マルチスレッドからの同時アクセスの際に、ある時点で1つのスレッドだけロックできる。

以下の例では、 `LockingCounter` でマルチスレッドからcounterの値を増やすときに、with文を使ってロックの獲得と解放をしている。 `Lock` がないとこの値はおかしくなる。

```python
from threading import Lock, Barrier, Thread

def worker(sensor_index, how_many, counter):
  # I have a barrier in here so the workers synchronize
  # when they start counting, otherwise it's hard to get a race
  # because the overhead of starting a thread is high.
  BARRIER.wait()
  for _ in range(how_many):
    # Read from the sensor
    # Nothing actually happens here, but this is where
    # the blocking I/O would go.
    counter.increment(1)

class LockingCounter:
  def __init__(self):
    self.lock = Lock()
    self.count = 0
  def increment(self, offset):
    with self.lock:
      self.count += offset

BARRIER = Barrier(5)
how_many = 10**5
counter = LockingCounter()
threads = []

for i in range(5):
  thread = Thread(target=worker, args=(i, how_many, counter))
  threads.append(thread)
  thread.start()

for thread in threads:
  thread.join()

expected = how_many * 5
found = counter.count
print(f'Counter should be {expected}, got {found}')

>>>
Counter should be 500000, got 500000
```

## 55. Use `Queue` to Coordinate Work Between Threads

スレッド間の協調作業には `Queue` を使う。

ある作業が終わったら次の作業、のようなパイプラインの各作業をそれぞれ並列で実現するために、作業間の入出力でキューを使う。

自分でキューを用意すると、ビジーウェイト、作業停止、メモリ爆発のような問題が起きやすいので、これらに必要な機能を提供する組み込みモジュール `queue` の `Queue` クラスを使う。 

[queue --- 同期キュークラス](https://docs.python.org/ja/3/library/queue.html)

- `task_done` は、キューのコンシューマスレッドがタスクを完了したことをキューに伝える。
- `join` は、キューに入れたタスクが完了するまでブロックして待つ。

以下の例は、download, resize, upload を実行するパイプライン。 

1. 各download, resize, uploadのステップ `start_threads` を実行。StoppableWorker のスレッドを実行。入力キューのアイテムごとにfuncを実行して出力キューに渡す。
2. パイプラインの最初のキューである `download_queue` に1000件のデータを投入。
3. 各download, resize, uploadのステップで順番に `stop_threads` を実行。 close処理としてキューにSENTINEL objectを渡し、それが見つかれば処理を終了させる。
4. パイプラインの最後のキューである `done_queue` の件数が1000件あることを確認。

```python
from queue import Queue

class StoppableWorker(Thread):
  def __init__(self, func, in_queue, out_queue):
    super().__init__()
    self.func = func
    self.in_queue = in_queue
    self.out_queue = out_queue

  def run(self):
    for item in self.in_queue:
      result = self.func(item)
      self.out_queue.put(result)

class ClosableQueue(Queue):
  SENTINEL = object()

  def close(self):
    self.put(self.SENTINEL)

  def __iter__(self):
    while True:
      item = self.get()
      try:
        if item is self.SENTINEL:
          return  # Cause the thread to exit
        yield item
      finally:
        self.task_done()

def start_threads(count, *args):
  threads = [StoppableWorker(*args) for _ in range(count)]
  for thread in threads:
    thread.start()
  return threads

def stop_threads(closable_queue, threads):
  for _ in threads:
    closable_queue.close()
  closable_queue.join()
  for thread in threads:
    thread.join()

def download(item):
  return item

def resize(item):
  return item

def upload(item):
  return item

download_queue = ClosableQueue()
resize_queue = ClosableQueue()
upload_queue = ClosableQueue()
done_queue = ClosableQueue()

download_threads = start_threads(3, download, download_queue, resize_queue)
resize_threads = start_threads(4, resize, resize_queue, upload_queue)
upload_threads = start_threads(5, upload, upload_queue, done_queue)

for _ in range(1000):
  download_queue.put(object())

stop_threads(download_queue, download_threads)
stop_threads(resize_queue, resize_threads)
stop_threads(upload_queue, upload_threads)

print(done_queue.qsize(), 'items finished')

>>>
1000 items finished
```

## 56. Know How to Recognize When Concurrency Is Necessary

並行性が必要なときをどのように認知するかを知る。

並行協調処理で最も多いのは、ファンアウトとファンイン。

ファンアウト (fan-out): 新たな並行処理単位の生成。例えば、thread.start。

ファンイン (fan-in): 並行処理単位の実行終了を待つ。例えば、thread.join。

Item 57, 58, 59, 60はファンアウト、ファンインを行うための組み込みツールの話。

- Item 57: `Thread`
- Item 58: `Queue`
- Item 59: `ThreadPoolExecutor`
- Item 60-63: コルーチン, `async`, `await`

## 57. Avoid Creating New `Thread` Instances for On-demand Fan-out

ファンアウトのために新たな `Thread` インスタンスを作るのを避ける。

スレッドの問題点

- 協調のために `Lock` が必要であり、その分コードが複雑になる。
- 多くのメモリが必要。
- 開始にはコストが掛かり、コンテキストスイッチもある。
- デバッグが大変。スレッドでraiseされたexceptionがスレッドを起動した呼び出し元にまで渡されない。

Item 58, 59, 60で説明する方法の方が良い。

## 58. Understand How Using `Queue` for Concurrency Requires Refactoring

並行性のリファクタリングのためにどのように `Queue` を使うかを理解する。

`Queue` を使うほうが `Thread` を使うよりファンイン、ファンアウトが改善するが、 `Queue`を使うためにリファクタリングするにはかなり労力がかかる。

Item 59, 60で説明する方法の方が良い。

## 59. Consider `ThreadPoolExecutor` When Threads Are Necessary for Concurrency

並行性のためにスレッドが必要なときは `TheadPoolExecutor`を検討する。

`concurrent.futures` の `ThreadPoolExecutor` クラスは、Item 57の `Thread` とItem58の `Queue` のいいところが組み合わされている。

限られたリファクタリングで単純なI/O並列を提供し、並行ファンアウトに必要なスレッド起動コストを簡単に削減できる。

`ThreadPoolExecutor` クラスは限られた個数のI/O並列化しか提供しない。

多くの並行数を実現したいのであれば、Item 60の方法が良い。

## 60. Achieve Highly Concurrent I/O with Coroutines

コルーチンで高度な並行I/Oを達成する。

コルーチンによって、Pythonプログラムにおいて見かけ上同時実行する関数を非常に多く実行できる。 `async` と `await` を使う。

コルーチンが `await` 式で停止し、待ち状態の `awaitable` が解消した時点で `async` 関数の実行を再開する。これは ジェネレータにおける `yield` の振る舞いと同じ。

このようなコルーチンの機構はイベントグループと呼ばれ、高度な並行I/Oを効率的に処理しながら、関数実行を頻繁にインタリーブする。

以下は `game_logic` 関数内で生じるI/Oをコルーチンを使って実装する例。

```python

async def game_logic(state, neighbors):
    # Do some input/output in here:
    data = await my_socket.read(50)

next_state = await game_logic(state, neighbors)
```

`[asyncio.run](http://asyncio.run)` でイベントループを実行

```python
asyncio.run(simulate(grid)
```

## 61. Know How to Port Threaded I/O to `asyncio`

スレッドIOをどのように `asyncio` に移行すればよいかを知っておく。

Pythonでは、forループ、with文、ジェネレータ、内包表記、ライブラリヘルパー関数の非同期バージョンが用意されている。

よって、スレッドのブロッキングI/Oを行うコードからコルーチンとasyncioをに移行するのが簡単。

## 62. Mix Threads and Coroutines to Ease the Transition to `asyncio`

スレッドとコルーチンを組み合わせて `asyncio` への移行を楽にする。

大規模なプログラムを移行するためには、コードベースでブロッキングI/Oにスレッドを使いながら、同時に非同期I/Oにコルーチンを使い、互いに互換性があるようにしておく。スレッドがコルーチンを実行でき、コルーチンでスレッドの開始と待機ができるようにしておく。

変換にはトップダウン方式とボトムダウン方式がある。

トップダウン方式では、 `main` エントリのようなコードベースの最上部から始めて、徐々に呼び出し階層の下へ作業する。

`run_in_executor` メソッドは、コルーチンで `ThreadPoolExecutor` プールの同期関数を実行でき、トップダウンのマイグレーションに使われる。

```python
async def run_tasks_mixed(handles, interval, output_path):
    loop = asyncio.get_event_loop()

    with open(output_path, 'wb') as output:
        async def write_async(data):
            output.write(data)

        def write(data):
            coro = write_async(data)
            future = asyncio.run_coroutine_threadsafe(coro, loop)
            future.result()
```

ボトムアップはその逆で、呼び出し階層を葉からエントリへ遡る。

`run_until_complete` メソッドは、同期コードでコルーチンを実行できる。 `run_coroutine_threadsafe` は、スレッド境界を超えて同じ機能を提供し、ボトムアップのマイグレーションに使われる。

```python
def tail_file(handle, interval, write_func):
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)

    async def write_async(data):
        write_func(data)

    coro = tail_async(handle, interval, write_async)
    loop.run_until_complete(coro)
```

## 63. Avoid Blocking the `asyncio` Event Loop to Maximize Responsiveness

応答を最大化するために `asyncio` イベントループのブロッキングを避ける。

`open` , `close` , `write` などのファイルハンドルの呼び出しがイベントループで生じると、これはかなりの時間をブロックして他のコルーチンの進行を妨げる。

`asyncio.run` 関数に `debug=True` を渡してイベントループの応答性を妨げている場所を検出できる。

```python
import asyncio
import time

async def slow_coroutine():
    time.sleep(0.5)  # Simulating slow I/O

asyncio.run(slow_coroutine(), debug=True)
Executing <Task finished name='Task-1' coro=<slow_coroutine() done, defined at <stdin>:1> result=None created at /opt/homebrew/Cellar/python@3.11/3.11.4_1/Frameworks/Python.framework/Versions/3.11/lib/python3.11/asyncio/runners.py:100> took 0.508 seconds
```

メインイベントループで行われるシステムコールを最小にするために、別スレッドで実行させる。

## 64. Consider `concurrent.futures` for True Parallelism

本当の並列性のために `concurrent.futures` を検討する。

Pythonのコードを書いていてパフォーマンスの壁にぶつかったとき、複数のCPUコアで実行したいけどGILがそれを妨げる。Cでコードを書き直すにしてもそれによるバグの混入、または一部の置き換えだと不十分な場合がある。

組み込みモジュール `concurrent.futures` からアクセスされる組み込みモジュール `multiprocessing` は、Pythonで複数のコアを活用し、追加インタプリタを子プロセスとして並列に実行する。

`concurrent.futures` と `ProcessPoolExecutor` を利用する。 `ThreadPoolExecutor` を `ProcessPoolExecutor` に置き換えるだけ。

シリアル実行の場合: 

```python
# run_serial.py
def main():
    start = time.time()
    results = list(map(my_module.gcd, NUMBERS))
    end = time.time()
    delta = end - start
    print(f'Took {delta:.3f} seconds')
```

```python
$ python run_serial.py
Took 0.542 seconds
```

`ThreadPoolExecutor` の場合:

```python
# run_threads.py
def main():
    start = time.time()
    pool = ThreadPoolExecutor(max_workers=2)  # Change
    results = list(pool.map(my_module.gcd, NUMBERS))
    end = time.time()
    delta = end - start
    print(f'Took {delta:.3f} seconds')
```

```python
$ python run_threads.py
Took 0.551 seconds
```

`ProcessPoolExecutor` の場合:

```python
# run_parallel.py
def main():
    start = time.time()
    pool = ProcessPoolExecutor(max_workers=2)  # Change
    results = list(pool.map(my_module.gcd, NUMBERS))
    end = time.time()
    delta = end - start
    print(f'Took {delta:.3f} seconds')
```

```python
$ python run_parallel.py
Took 0.360 seconds
```

最初から `multiprocessing` を使うのは避ける。最終的に他の手段がない場合は使用を検討する。
