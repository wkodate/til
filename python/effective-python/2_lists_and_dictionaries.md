# **Chapter 2: Lists and Dictionaries**

リストと辞書

In Python, the most common way to organize information is in a sequence of values stored in a `list`. A `list`‘s natural complement is the `dict` that stores lookup keys mapped to corresponding values. This chapter covers how to build programs with these versatile building blocks.

1. Know How to Slice Sequences
2. Avoid Striding and Slicing in a Single Expression
3. Prefer Catch-All Unpacking Over Slicing
4. Sort by Complex Criteria Using the `key` Parameter
5. Be Cautious When Relying on `dict` Insertion Ordering
6. Prefer `get` Over `in` and `KeyError` to Handle Missing Dictionary Keys
7. Prefer `defaultdict` Over `setdefault` to Handle Missing Items in Internal State
8. Know How to Construct Key-Dependent Default Values with `__missing__`

## 1. Know How to Slice Sequences

`list` などのシーケンスをどのようにスライスするかを知っておく。

基本形は `list[start:end]`。 `start`の要素は含まれ、 `end`の要素は含まれない。 `start` が0の場合、 `end` がlistの長さになる場合は省略する。

```python
list = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h']
print(list[3:5])

>>>
['d', 'e']
```

## 2. Avoid Striding and Slicing in a Single Expression

ストライドとスライスを1角式で同時に使わない。

スライスの増分(ストライド)を `list[start:end:stride]`の形式で定義できる。

```python
x = ['red', 'orange', 'yellow', 'green', 'blue', 'purple']
odds = x[::2]
print(odds)

>>>
['red', 'yellow', 'blue']
```

`start`, `end`, `stride` をすべて指定すると読みにくいので、一緒に使わずに代入を2回する。

```python
 x = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h']

# NG
y = x[2:-2:2]
print(y)

>>>
['c', 'e']

# OK
y = x[::2]
z = y[1:-1]
print(z)

>>>
['c', 'e']
```

## 3. Prefer Catch-All Unpacking Over Slicing

アンパックするときは、インデックスとスライドを使うのではなく、 `catch-all` アンパックを使う。

アスタリスク付きの引数で表し、他のアンパックパターンに合致しない残りの値をリストで返す。

```python
cat_ages_descending = [20, 19, 15, 9, 8, 7, 6, 4, 1, 0]

# NG
oldest = car_ages_descending[0]
second_oldest = car_ages_descending[1]
others = car_ages_descending[2:]
print(oldest, second_oldest, others)

>>>
20 19 [15, 9, 8, 7, 6, 4, 1, 0]

# OK

oldest, second_oldest, *others = car_ages_descending
print(oldest, second_oldest, others)

>>>
20 19 [15, 9, 8, 7, 6, 4, 1, 0]
```

## 4. Sort by Complex Criteria Using the `key` Parameter

`key` パラメータを使って複雑なソートをする。

`sort` メソッドの `key` パラメータを使って、 `list` の各要素をソートする値を返すヘルパー関数を与えることができる。

```python
places = ['home', 'work', 'New York', 'Paris']
places.sort()
print('Case sensitive:  ', places)
places.sort(key=lambda x: x.lower())
print('Case insensitive:', places)

>>>
['New York', 'Paris', 'home', 'work']
['home', 'New York', 'Paris', 'work']
```

`key` 関数で `tuple` を返せば、複数のソートを組み合わせることができる。

`list` の `sort` メソッドは、 `key` が互いに等しい値を返したときには入力listの順を保持するので、これを利用して `sort` を複数回呼び出して複数のソートを組み合わせる。

以下の例は、 `weight` が降順、 `name` が昇順になるソート:

```python
power_tools = [
    Tool('drill', 4),
    Tool('circular saw', 5),
    Tool('jackhammer', 40),
    Tool('sander', 4),
]

power_tools.sort(key=lambda x: x.name)
print(power_tools)
power_tools.sort(key=lambda x: x.weight, reverse=True)
print(power_tools)

>>>
[Tool('circular saw', 5), Tool('drill', 4), Tool('jackhammer', 40), Tool('sander', 4)]
[Tool('jackhammer', 40), Tool('circular saw', 5), Tool('drill', 4), Tool('sander', 4)]  # 上記ソートによるdrill, sanderの順が保持されている。
```

## 5. Be Cautious When Relying on `dict` Insertion Ordering

`dict` の順番に注意する。

Python3.7以降は、 `dict` の内容は挿入順に保持される。

挿入順に依存しないコードを書く、実行時に `dict` 型かを明示的にチェックする、型ヒントと静的解析で `dict` をチェックする。

## 6. Prefer `get` Over `in` and `KeyError` to Handle Missing Dictionary Keys

辞書の欠損キーの処理には `in` や `KeyError` ではなく `get` を使う。

```python
counters = {
    'pumpernickel': 2,
    'sourdough': 1,
}

key = 'multigrain'
count = counters.get(key, 0)
counters[key] = count + 1
print(counters)

>>>
{'pumpernickel': 2, 'sourdough': 1, 'multigrain': 1}

```

## 7. Prefer `defaultdict` Over `setdefault` to Handle Missing Items in Internal State

辞書への値の代入もする場合は、 `get` よりも `setdefault` が好ましい場合もある。

その場合は、`setdefault` ではなくcollectionsの `defaultdict` を使う。

[collections — Container datatypes — Python 3.12.2 documentation#collections.defaultdict](https://docs.python.org/3/library/collections.html#collections.defaultdict)

```python
class Visits:
  def __init__(self):
    self.data = defaultdict(set)
  def add(self, country, city):
    self.data[country].add(city)

visits = Visits()
visits.add('England', 'Bath')
visits.add('England', 'London')
print(visits.data)

>>>
defaultdict(<class 'set'>, {'England': {'Bath', 'London'}})
```

## 8. Know How to Construct Key-Dependent Default Values with `__missing__`

`__missing__` でキー依存デフォルト値を作成する方法を知る。

メソッド `__missing__` を持つdictサブクラスを定義して、どのキーをアクセスしているのかがわかるデフォルト値を作ることができる。

```python
class Pictures(dict):
  def __missing__(self, key):
    value = open_picture(key)
    self[key] = value
    return value
```

[Built-in Types — Python 3.12.2 documentation#dict](https://docs.python.org/3/library/collections.html#collections.defaultdict.__missing__)

`__missing__` メソッドは、辞書からキーを取得できなかった場合に呼ばれる。
