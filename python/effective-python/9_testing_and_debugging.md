# **Chapter 9: Testing and Debugging**

テストとデバッグ

You should always test your code, regardless of what language it’s written in. However, Python’s dynamic features can increase the risk of runtime errors in unique ways. Luckily, they also make it easier to write tests and diagnose malfunctioning programs. This chapter covers Python’s built-in tools for testing and debugging.

1. Use `repr` Strings for Debugging Output
2. Verify Related Behaviors in `TestCase` Subclasses
3. Isolate Tests from Each Other with `setUp`, `tearDown`, `setUpModule`, and `tearDownModule`
4. Use Mocks to Test Code with Complex Dependencies
5. Encapsulate Dependencies to Facilitate Mocking and Testing
6. Consider Interactive Debugging with `pdb`
7. Use `tracemalloc` to Understand Memory Usage and Leaks

## 75. Use `repr` Strings for Debugging Output

出力のデバッグには `repr` 文字列を使う。

`print` だと型情報は表示されない。

```python
int_value = 5
str_value = '5'
datetime_value = datetime.now()

print(int_value)
print(str_value)
print(datetime_value)

print(repr(int_value))
print(repr(str_value))
print(repr(datetime_value))

>>>
5
5
2024-04-05 23:23:20.933068

5
'5'
datetime.datetime(2024, 4, 5, 23, 23, 20, 933068)
```

クラスの出力情報をカスタマイズしたいのであれば、 `__repr__` メソッドを定義する。

```python
class OpaqueClass:
  def __init__(self, x, y):
    self.x = x
    self.y = y

obj = OpaqueClass(1, 'foo')
print(obj)

# Example 9
class BetterClass:
  def __init__(self, x, y):
    self.x = x
    self.y = y

  def __repr__(self):
    return f'BetterClass({self.x!r}, {self.y!r})'
    
obj = BetterClass(2, 'bar')
print(obj)

>>>
<__main__.OpaqueClass object at 0x1016da410>
BetterClass(2, 'bar')
```

## 76. Verify Related Behaviors in `TestCase` Subclasses

関連する振る舞いを `TestCase` サブクラスで検証する。

組み込みの `assert` 文より、等価性を検証する `assertEqual` 、論理式を検証する `assertTrue` を使う方が、失敗時の理由がわかりやすく表示されるので良い。

例外を検証するときは `assertRaises` を使う。 `with` 文でコンテキストマネージャとして使うことができる。

`subTest` を使えば、同一のテストメソッドの中で複数データを使っての複数のテストしたときにどの値で失敗したかがわかる。

## 77. Isolate Tests from Each Other with `setUp`, `tearDown`, `setUpModule`, and `tearDownModule`

`setUp` , `tearDown` , `setUpModule` , `tearDownModule` でテストを分離する。

`setUp` , `tearDown` は、各テストメソッドの前後でそれぞれ実行される。

`setUpModule` , `tearDownModule` はモジュールの全テストの最初と最後に実行される。

## 78. Use Mocks to Test Code with Complex Dependencies

複雑な依存を持ったテストコードのためにモックを使う。

テストするコードで要求される依存性をセットアップするのが困難な場合にモックが役立つ。

モックが正しく呼び出されているかは `assert_called_once_with` メソッドで確認できる。

モックを作りたいメソッドを元の関数のキーワード引数に与えるように修正する。

これをもっと簡単にするには、 `unittest.mock.patch` を使えばテストするコードにモックを投入できる。

## 79. Encapsulate Dependencies to Facilitate Mocking and Testing

モックとテストを活用して依存性をカプセル化する。

元のオブジェクトを引数として関数に渡す代わりに、データベースのインターフェースをラッパーオブジェクトを使ってカプセル化するのが良い。

以下の例は、元の関数 `do_rounds` をテストしたい。 `DatabaseConnection` オブジェクトを引数として渡す代わりに、テストで `ZooDatabase` オブジェクトをモックに渡している。

```python
from datetime import datetime, timedelta
from unittest.mock import call
from unittest.mock import Mock

# Original function
def do_rounds(database, species, *, utcnow=datetime.utcnow):
  now = utcnow()
  feeding_timedelta = database.get_food_period(species)
  animals = database.get_animals(species)
  fed = 0
  for name, last_mealtime in animals:
    if (now - last_mealtime) >= feeding_timedelta:
      database.feed_animal(name, now)
      fed += 1
  return fed

# Create Mock
class ZooDatabase:
  def get_animals(self, species):
    pass
  def get_food_period(self, species):
    pass
  def feed_animal(self, name, when):
    pass

now_func = Mock(spec=datetime.utcnow)
now_func.return_value = datetime(2019, 6, 5, 15, 45)

database = Mock(spec=ZooDatabase)
database.get_food_period.return_value = timedelta(hours=3)
database.get_animals.return_value = [
  ('Spot', datetime(2019, 6, 5, 11, 15)),
  ('Fluffy', datetime(2019, 6, 5, 12, 30)),
  ('Jojo', datetime(2019, 6, 5, 12, 55))
]

# Unittest with Mock
result = do_rounds(database, 'Meerkat', utcnow=now_func)
assert result == 2

database.get_food_period.assert_called_once_with('Meerkat')
database.get_animals.assert_called_once_with('Meerkat')
database.feed_animal.assert_has_calls(
  [
    call('Spot', now_func.return_value),
    call('Fluffy', now_func.return_value),
  ],
  any_order=True)
```

## 80. Consider Interactive Debugging with `pdb`

`pdb` で対話的にデバッグすることを考える。

`pdb` モジュールをインポートして `breakpoint` を呼び出す。　 コード内で `breakpoint` が呼び出されると、プログラムは一時停止し、対話型シェルに切り替わる。

以下の例では、 `breakpoint()` を実行した後の挙動を確認している。

```python
import math

def compute_rmse(observed, ideal):
    total_err_2 = 0
    count = 0
    for got, wanted in zip(observed, ideal):
        err_2 = (got - wanted) ** 2
        breakpoint()  # Start the debugger here
        total_err_2 += err_2
        count += 1

    mean_err = total_err_2 / count
    rmse = math.sqrt(mean_err)
    return rmse

result = compute_rmse(
    [1.8, 1.7, 3.2, 6],
    [2, 1.5, 3, 5])
print(result)
```

```python
$ python always_breadkpoint.py
-> total_err_2 += err_2
(Pdb) where
-> result = compute_rmse(
(Pdb) total_err_2
0
(Pdb) count
0
(Pdb) continue
-> total_err_2 += err_2
(Pdb) total_err_2
0.03999999999999998
(Pdb) count
1
```

プログラムが例外を発生させたりクラッシュした後のデバッグもできる。

`python3 -m pdb -c continue <program path>` を使うと、問題がおきたタイミングで対話デバッガを実行して、その時点でのプログラムの状態を調べることができる。

もしくは対話型インタプリタで例が発生した後に、 `import pdb; pdb.pm()` で後から確認もできる。

## 81. Use `tracemalloc` to Understand Memory Usage and Leaks

メモリの使用とリークを理解するために `tracemalloc` を使う。

Pythonプログラマがプログラムでメモリを割り当てたり削除したりは基本的に意識する必要はない。

それでもメモリリークが見つかった場合は、 `gc` でガベージコレクタのオブジェクトをリスト氏、 `tracemalloc` でオブジェクトがどこで割り付けられたかを知ることができる。

以下は、どのオブジェクトでメモリの使用量が大きいか、ソースコードのどこで割り付けられているかを知る例。 `waste_memory.run()` の実行前後でスナップショットを取って比較している。

```python
import os

class MyObject:
    def __init__(self):
        self.data = os.urandom(100)

def get_data():
    values = []
    for _ in range(100):
        obj = MyObject()
        values.append(obj)
    return values

def run():
    deep_values = []
    for _ in range(100):
        deep_values.append(get_data())
    return deep_values
```

```python
import tracemalloc

tracemalloc.start(10)                      # Set stack depth
time1 = tracemalloc.take_snapshot()        # Before snapshot

import waste_memory

x = waste_memory.run()                     # Usage to debug
time2 = tracemalloc.take_snapshot()        # After snapshot

stats = time2.compare_to(time1, 'lineno')  # Compare snapshots
for stat in stats[:3]:
    print(stat)
```

```python
$ python top_n.py
{PATH}/waste_memory.py:5: size=1299 KiB (+1299 KiB), count=10000 (+10000), average=133 B
{PATH}/waste_memory.py:10: size=785 KiB (+785 KiB), count=20000 (+20000), average=40 B
{PATH}/waste_memory.py:11: size=84.4 KiB (+84.4 KiB), count=100 (+100), average=864 B
```
