# **Chapter 4: Comprehensions and Generators**

内包表記とジェネレータ

Python has special syntax for quickly iterating through lists, dictionaries, and sets to generate derivative data structures. It also allows for a stream of iterable values to be incrementally returned by a function. This chapter covers how these features can provide better performance, reduced memory usage, and improved readability.

- 27. Use Comprehensions Instead of `map` and `filter`
- 28. Avoid More Than Two Control Subexpressions in Comprehensions
- 29. Avoid Repeated Work in Comprehensions by Using Assignment Expressions
- 30. Consider Generators Instead of Returning Lists
- 31. Be Defensive When Iterating Over Arguments
- 32. Consider Generator Expressions for Large List Comprehensions
- 33. Compose Multiple Generators with `yield from`
- 34. Avoid Injecting Data into Generators with `send`
- 35. Avoid Causing State Transitions in Generators with `throw`
- 36. Consider `itertools` for Working with Iterators and Generators

## 27. Use Comprehensions Instead of map and filter

mapやfilterの代わりにリスト内包表記を使う。

リスト内包表記はlambda式を必要としないのでmapやfilterよりもわかりやすい。

```python
# map + filter
even_squares = map(lambda x: x**2, filter(lambda x: x % 2 == 0, a))

# Comprehensions
even_squares = [x**2 for x in a if x % 2 == 0]
```

## 28. Avoid More Than Two Control Subexpressions in Comprehensions

リスト内包表記では、3つ以上の式を使うことを避ける。2つの条件、2つのループ、1つの条件と1つのループまで。

## 29. Avoid Repeated Work in Comprehensions by Using Assignment Expressions

ウォルラス演算子( `:=` )を使った代入式を使い内包表記での繰り返しをなくす。

以下の例だとget_batchesの評価と代入を行っている部分をウォルラス演算子に置き換えている。

```python
# Bad
found = {name: get_batches(stock.get(name, 0), 8)
         for name in order
         if get_batches(stock.get(name, 0), 8)}

# Good
found = {name: batches for name in order
         if (batches := get_batches(stock.get(name, 0), 8))}
```

## 30. Consider Generators Instead of Returning Lists

リストを返す代わりにジェネレータを使うことを検討する。

ジェネレータ関数は、呼び出されると処理をせずにイテレータを返す。 `next` 関数が呼び出される毎にイテレータはジェネレータを次の `yield` 式に1つ進める。

以下の例では、リストに関する `append` の処理が書かれていないので読みやすくなっている。入力が大きい場合にメモリを使い切ることもなくなる。

```python
# List
def index_words(text):
    result = []
    if text:
        result.append(0)
    for index, letter in enumerate(text):
        if letter == ' ':
            result.append(index + 1)
    return result

# Generator
def index_words_iter(text):
    if text:
        yield 0
    for index, letter in enumerate(text):
        if letter == ' ':
            yield index + 1
```

## 31. Be Defensive When Iterating Over Arguments

引数でイテレータを使うときは確実さを優先する。

イテレータは結果を一度だけしか処理しない。

イテレータプロトコルを実装した新たなコンテナクラスを提供する。

`__iter__` メソッドを実装すれば、forループや関連した式が `iter` を呼び出す。

```python
# Container class
class ReadVisits:
    def __init__(self, data_path):
        self.data_path = data_path

    def __iter__(self):
        with open(self.data_path) as f:
            for line in f:
                yield int(line)

def normalize(numbers):
    total = sum(numbers)
    result = []
    for value in numbers:
        percent = 100 * value / total
        result.append(percent)
    return result

visits = ReadVisits(path)
percentages = normalize(visits)
```

## 32. Consider Generator Expressions for Large List Comprehensions

大きなリスト内包表記にはジェネレータ式を検討する。

リスト内包表記は、大量の入力に対してメモリを使いすぎる問題がある。

その場合はジェネレータ式を使う。前後をカッコで囲うとジェネレータ式になる。

```python
# List comprehension
value = [len(x) for x in open('my_file.txt')]

# Generator expression
it = (len(x) for x in open('my_file.txt'))
```

## 33. Compose Multiple Generators with yield from

`yield from` を使って複数のジェネレータを生成する。

```python
def move(period, speed):
    for _ in range(period):
        yield speed

def pause(delay):
    for _ in range(delay):
        yield 0

# Bad
def animate():
    for delta in move(4, 5.0):
        yield delta
    for delta in pause(3):
        yield delta
    for delta in move(2, 3.0):
        yield delta

# Good
def animate_composed():
    yield from move(4, 5.0)
    yield from pause(3)
    yield from move(2, 3.0)
```

可読性も性能もこっちの方が良い。

## 34. Avoid Injecting Data into Generators with send

`send` でデータをジェネレータに渡すのは避ける。

`send` メソッドは、yield式に与えた値を変数に代入できるようジェネレータにデータを入力できる。

`send` と `yield from` 式を一緒に使うと、ジェネレータの出力に予期せずNone値が現れるような振る舞いがある。 `yield from` で組み合わせたジェネレータにイテレータを渡す方が良い。

```python
def complex_wave_cascading(amplitude_it):
    yield from wave_cascading(amplitude_it, 3)
    yield from wave_cascading(amplitude_it, 4)
    yield from wave_cascading(amplitude_it, 5)

def run_cascading():
    amplitudes = [7, 7, 7, 2, 2, 2, 2, 10, 10, 10, 10, 10]
    it = complex_wave_cascading(iter(amplitudes)).   # Pass iterator
    for amplitude in amplitudes:
        output = next(it)
        transmit(output)

run_cascading()
```

## 35. Avoid Causing State Transitions in Generators with throw

ジェネレータでthrowによる状態遷移を起こすのを避ける。

入れ子になって読みにくい。

```python
def check_for_reset():
    # Poll for external event
    return RESETS.pop(0)

def announce(remaining):
    print(f'{remaining} ticks remaining')

def run():
    it = timer(4)    
    while True:
        try:
            if check_for_reset():    # State transaction
                current = it.throw(Reset()).   # Throw exception
            else:
                current = next(it)
        except StopIteration:
            break
        else:
            announce(current)

run()
```

ジェネレータの例外処理は、  `throw` を使わずに、`__iter__` メソッドと例外の状態遷移をするメソッドを実装するクラスを使うほうがいい。

```python
class Timer:
    def __init__(self, period):
        self.current = period
        self.period = period

    def reset(self):
        self.current = self.period

    def __iter__(self):
        while self.current:
            self.current -= 1
            yield self.current

def run():
    timer = Timer(4)
    for current in timer:
        if check_for_reset():
            timer.reset()
        announce(current)

run()
```

## 36. Consider itertools for Working with Iterators and Generators

イテレータとジェネレータを扱うときは [itertools](https://docs.python.org/3/library/itertools.html) を使う。

```python
import itertools
```

重要な関数は大きく3つ

- Linking Iterators Together
- Filtering Items from an Iterator
- Producing Combinations of Items from Iterators
