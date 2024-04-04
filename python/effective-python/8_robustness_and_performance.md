# **Chapter 8: Robustness and Performance**

頑健性と性能

Python has built-in features and modules that aid in hardening your programs so they are dependable. Python also includes tools to help you achieve higher performance with minimal effort. This chapter covers how to use Python to optimize your programs to maximize their reliability and efficiency in production.

1. Take Advantage of Each Block in `try`/`except`/`else`/`finally`
2. Consider `contextlib` and `with` Statements for Reusable `try`/`finally` Behavior
3. Use `datetime` Instead of `time` for Local Clocks
4. Make `pickle` Reliable with `copyreg`
5. Use `decimal` When Precision Is Paramount
6. Profile Before Optimizing
7. Prefer `deque` for Producer–Consumer Queues
8. Consider Searching Sorted Sequences with `bisect`
9. Know How to Use `heapq` for Priority Queues
10. Consider `memoryview` and `bytearray` for Zero-Copy Interactions with bytes

## 65. Take Advantage of Each Block in `try`/`except`/`else`/`finally`

`try`/`except`/`else`/`finally` を活用する。

`finally` ブロックはファイルハンドルのcloseのような例外の発生にかかわらず後処理をしたいときに使う。

`else` ブロックは、 `try` ブロックで例外が発生しなかった場合に実行される。 `try` ブロックのコードが少なくなり例外の原因が分離されて読みやすくなる。

以下の例について、

- `try` で成功した場合、 `try` , `else` , `finally` のブロックが実行される。
- `try` ブロック内の計算結果で `ZeroDivisionError` が起きた場合、 `try` , `except`, `finally` が実行され、 `else` は実行されない。
- 入力のJSONデータに問題があり `try` ブロック内で `UnicodeDecodeError` が起きた場合、 `except` と `else` は実行されず、 `try` と `finally` が実行される。
- `else` ブロック内の書き込みでディスクフル `OSError` が発生した場合、例外が発生して `finally` ブロックが実行される。

```bash
def divide_json(path):
    print('* Opening file')
    handle = open(path, 'r+')   # May raise OSError
    try:
        print('* Reading data')
        data = handle.read()    # May raise UnicodeDecodeError
        print('* Loading JSON data')
        op = json.loads(data)   # May raise ValueError
        print('* Performing calculation')
        value = (
            op['numerator'] /
            op['denominator'])  # May raise ZeroDivisionError
    except ZeroDivisionError as e:
        print('* Handling ZeroDivisionError')
        return UNDEFINED
    else:
        print('* Writing calculation')
        op['result'] = value
        result = json.dumps(op)
        handle.seek(0)          # May raise OSError
        if DIE_IN_ELSE_BLOCK:
            import errno
            import os
            raise OSError(errno.ENOSPC, os.strerror(errno.ENOSPC))
        handle.write(result)    # May raise OSError
        return value
    finally:
        print('* Calling close()')
        handle.close()          # Always runs
```

## 66. Consider `contextlib` and `with` Statements for Reusable `try`/`finally` Behavior

`try` / `finally` の代わりに `contextlib` と `with` 文を検討する。

`with` 文の方が見た目をすっきり書ける。

組み込みモジュール `contextlib` は単純な関数を `with` 文で使えるようにする `contextmanager` デコレータを含んでいる。

`contextmanager` で `yield` した値が `with` 文の `as` に渡される。

```python
@contextmanager
def log_level(level, name):
    logger = logging.getLogger(name)
    old_level = logger.getEffectiveLevel()
    logger.setLevel(level)
    try:
        yield logger
    finally:
        logger.setLevel(old_level)

with log_level(logging.DEBUG, 'my-log') as logger:
    logger.debug(f'This is a message for {logger.name}!')
    logging.debug('This will not print')
```

## 67. Use `datetime` Instead of `time` for Local Clocks

ローカルクロックには `time` の代わりに `datetime` を使う。

`time` はホストのOS依存で、ローカルタイムを適切に変換できない。

`datetime` は `pytz` モジュールがタイムゾーンのデータベースを持っているので、変換する機能は信頼できる。

時刻は常にUTCに変換し、表示の直前でローカルタイムに変換する。

## 68. Make `pickle` Reliable with `copyreg`

`copyreg` で `pickle` を信頼できるようにする。

組み込みモジュール `pickle` は、Pythonオブジェクトをシリアライズしてバイトストリームにしたり、バイトをデシリアライズしたりしてオブジェクトに戻す。

以前 `pickle` したオブジェクトをデシリアライズするとき、クラスの属性追加削除が合ったときに正しく動かないことがある。

`copyreg` モジュールは、Pythonオブジェクトをシリアライズしたりデシリアライズする責任のある関数を登録して、 `pickle` の振る舞いを制御して信頼できるものにする。後方互換性を保つ。

以下の例は、 `GameState` にフィールドを追加したときの振る舞い。

```python
class GameState:
  def __init__(self, level=0, lives=4, points=0):
    self.level = level
    self.lives = lives
    self.points = points

# Serialize
def pickle_game_state(game_state):
  kwargs = game_state.__dict__
  return unpickle_game_state, (kwargs,)

# Deserialize
def unpickle_game_state(kwargs):
  return GameState(**kwargs)

# Before
import pickle
import copyreg

copyreg.pickle(GameState, pickle_game_state)
state = GameState()
state.points += 1000
serialized = pickle.dumps(state)
state_after = pickle.loads(serialized)
print(state_after.__dict__)

# After
class GameState:
  def __init__(self, level=0, lives=4, points=0, magic=5):
    self.level = level
    self.lives = lives
    self.points = points
    self.magic = magic  # New field

state_after = pickle.loads(serialized)
print(state_after.__dict__)

>>>
{'level': 0, 'lives': 4, 'points': 1000}
{'level': 0, 'lives': 4, 'points': 1000, 'magic': 5}
```

フィールドの削除は `copyreg` の引数にバージョンを追加、クラス名の変更は `copyreg` にシリアライズしたときのインポートパスを渡すことで解決できる。

## 69. Use `decimal` When Precision Is Paramount

精度が重要な場合は `decimal` を使う。

`Decimal` クラスはデフォルトで固定小数点28桁の演算を行う。

```python
rate = 1.45
seconds = 3*60 + 42
cost = rate * seconds / 60
print(cost)

# Decimal
from decimal import Decimal
rate = Decimal('1.45')
seconds = Decimal(3*60 + 42)
cost = rate * seconds / Decimal(60)
print(cost)

>>>
5.364999999999999
5.365
```

浮動小数点数近似値ではなく正確な値が必要なら、 `Decimal` コンストラクタに `float` インスタンスではなく `str` インスタンスを渡す。

```python
print(Decimal('1.45'))
print(Decimal(1.45))

>>> 
1.45
1.4499999999999999555910790149937383830547332763671875
```

## 70. Profile Before Optimizing

最適化の前にまずはプロファイリングをする。

PythonはピュアPythonのプロファイラである `profile` とC拡張モジュールのプロファイラ( `cProfile` ) を提供する。 `Profile` の方が実行時のパフォーマンスの影響が少ないので良い。

`profile` オブジェクトの `runcall` メソッドでテスト関数を実行し、終わったら `pstats` を使って統計情報を取得する。

以下の例は、プロファイル情報によって `insert_value` のパフォーマンスが悪いことがわかる。

```python
def insertion_sort(data):
  result = []
  for value in data:
    insert_value(result, value)
  return result

def insert_value(array, value):
  for i, existing in enumerate(array):
    if existing > value:
      array.insert(i, value)
      return
  array.append(value)

from random import randint
from pstats import Stats
from sys import stdout as STDOUT

max_size = 10**4
data = [randint(0, max_size) for _ in range(max_size)]
test = lambda: insertion_sort(data)

from cProfile import Profile

profiler = Profile()
profiler.runcall(test)

stats = Stats(profiler)
stats = Stats(profiler, stream=STDOUT)
stats.strip_dirs()
stats.sort_stats('cumulative')
stats.print_stats()

>>>
         20003 function calls in 1.380 seconds

   Ordered by: cumulative time

   ncalls  tottime  percall  cumtime  percall filename:lineno(function)
        1    0.000    0.000    1.380    1.380 <stdin>:1(<lambda>)
        1    0.002    0.002    1.380    1.380 <stdin>:1(insertion_sort)
    10000    1.363    0.000    1.378    0.000 <stdin>:1(insert_value)
     9989    0.015    0.000    0.015    0.000 {method 'insert' of 'list' objects}
       11    0.000    0.000    0.000    0.000 {method 'append' of 'list' objects}
        1    0.000    0.000    0.000    0.000 {method 'disable' of '_lsprof.Profiler' objects}
```

## 71. Prefer `deque` for Producer–Consumer Queues

Producer-Consumer キューには `deque` を使う。

`list` はFIFOキューとして使うと、要素数が増えたときに性能が低下する。

## 72. Consider Searching Sorted Sequences with `bisect`

ソート済みシーケンスの探索には `bisect` を検討する。

`bisect` の `bisect_left` で二分探索ができる。

## 73. Know How to Use `heapq` for Priority Queues

プライオリティキューで `heapq` をどのように使うかを知っておく。

`functool` の `total_ordering` クラスデコレータを使い、 `__lt__` を定義する。

```python
import functools
from heapq import heappush

def add_book(queue, book):
    heappush(queue, book)

@functools.total_ordering
class Book:
  def __init__(self, title, due_date):
    self.title = title
    self.due_date = due_date
  def __lt__(self, other):
    return self.due_date < other.due_date

queue = []
add_book(queue, Book('Pride and Prejudice', '2019-06-01'))
add_book(queue, Book('The Time Machine', '2019-05-30'))
add_book(queue, Book('Crime and Punishment', '2019-06-06'))
add_book(queue, Book('Wuthering Heights', '2019-06-12'))
print([b.title for b in queue])

>>>
['The Time Machine', 'Pride and Prejudice', 'Crime and Punishment', 'Wuthering Heights']
```

## 74. Consider `memoryview` and `bytearray` for Zero-Copy Interactions with bytes

bytes型のゼロコピー処理には `memoryview` と `bytearray` を検討する。

`memoryview` は `bytes` インスタンスなどの内部で使用しているメモリ領域(バッファ)へアクセスするためのラッパーになる。

`bytearray` 型は `bytes` のミュータブル版。

`memoryview` は `bytearray` のラップもできる。この `memoryview` をスライスするとき、この結果オブジェクトを使ってデータをバッファの特定の場所に割り当てることができる。

これにより、クライアントからスライスされたデータを受け取ったあとのジョインの時間を大幅に削減できて性能が上がる。
