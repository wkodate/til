# **Chapter 3: Functions**

関数

Functions in Python have a variety of extra features that make a programmer’s life easier. Some are similar to capabilities in other programming languages, but many are unique to Python. This chapter covers how to use functions to clarify intention, promote reuse, and reduce bugs.

- 19. Never Unpack More Than Three Variables When Functions Return Multiple Values
- 20. Prefer Raising Exceptions to Returning `None`
- 21. Know How Closures Interact with Variable Scope
- 22. Reduce Visual Noise with Variable Positional Arguments
- 23. Provide Optional Behavior with Keyword Arguments
- 24. Use `None` and Docstrings to Specify Dynamic Default Arguments
- 25. Enforce Clarity with Keyword-Only and Position-Only Arguments
- 26. Define Function Decorators with `functools.wraps`

## 19. Never Unpack More Than Three Variables When Functions Return Multiple Values

関数からの複数の戻り値をでアンパックするときに、4個以上の変数を使わない。

```python
# Correct:
minimum, maximum, average, median, count = get_stats(lengths)

# Oops! Median and average swapped:
minimum, maximum, median, average, count = get_stats(lengths)
```

戻り値から順番のバグが発生し、それを特定しにくくなる。

アンパックする行が長くなって可読性が下がる。

軽量なクラスか `namedtuple` を使い、関数でそのインスタンスを返すようにする。

`namedtuple` は名前付きのtuple。

[collections — Container datatypes — Python 3.12.2 documentation#collections.namedtuple](https://docs.python.org/3/library/collections.html#collections.namedtuple)

```python
from collections import namedtuple

Point = namedtuple('Point', ['x', 'y'])
p = Point(11, y=22)
print(p[0] + p[1])
x, y = p
print(x, y)

>>>
33
11 22
```

## 20. Prefer Raising Exceptions to Returning `None`

`None` を返す代わりにExcpetionを送出する。

`None` を返すと、呼び出し元でハンドリングするときに、他の値と同様に条件式で `False` と評価されてしまうためエラーを引き起こしやすい。

```python
def careful_divide(a, b):
    try:
        return a / b
    except ZeroDivisionError:
        return None

x, y = 0, 5
result = careful_divide(x, y)
if not result:
    print('Invalid inputs')
```

その代わりにExceptionをraiseして、呼び出し元で適切に例外処理をする。Exception送出の振る舞いをドキュメント化する。

```python
def careful_divide(a: float, b: float) -> float:
    """Divides a by b.

    Raises:
        ValueError: When the inputs cannot be divided.
    """
    try:
        return a / b
    except ZeroDivisionError as e:
        raise ValueError('Invalid inputs')
```

## 21. Know How Closures Interact with Variable Scope

クロージャと変数スコープの関係を知っておく。

クロージャは、定義されたスコープの変数を参照する関数。

```python
def sort_priority(values, group):
    def helper(x):
        if x in group:
            return (0, x)
        return (1, x)
    values.sort(key=helper)
```

`nonlocal` 文を使って、クロージャが外のスコープにある変数を修正できる。

```python
def sort_priority3(numbers, group):
    found = False
    def helper(x):
        nonlocal found
        if x in group:
            found = True
            return (0, x)
        return (1, x)
    numbers.sort(key=helper)
    return found

numbers = [8, 3, 1, 2, 5, 4, 7, 6]
group = {2, 3, 5, 7}
found = sort_priority3(numbers, group)
print(found)
print(number)

>>>
True
[2, 3, 5, 7, 1, 4, 6, 8]
```

`nonlocal` 文は単純な関数でのみ使う。

## 22. Reduce Visual Noise with Variable Positional Arguments

可変長位置引数( `varargs` , `*args`スター引数とも呼ばれる)を使って見やすくする。

```python
def log(message, *values):
    if not values:
        print(message)
    else:
        values_str = ', '.join(str(x) for x in values)
        print(f'{message}: {values_str}')

log('My numbers are', 1, 2)
log('Hi there')

>>>
My numbers are: 1, 2
Hi there
```

`*args` を使う関数に新たに位置パラメータを追加すると、バグを引き起こす可能性がある。

これを回避するためには、キーワード専用引数を使う、型ヒントを使う。

## 23. Provide Optional Behavior with Keyword Arguments

キーワード引数。

辞書から関数のキーワード引数を `**` 演算子を使って渡すことができる。

```python
def remainder(number, divisor):
    return number % divisor

my_kwargs = {
	'divisor': 7,
}
print(remainder(number=20, **my_kwargs))

>>>
6
```

キーワード引数のメリット

- 読み手がわかりやすい。
- 関数定義でデフォルト値を持てる。
- 既存の呼び出し元の広報互換性を保ちながら関数の引数を変更できる。

必須ではないオプションのキーワード引数は、位置ではなくキーワードで渡すべき。

## 24. Use `None` and Docstrings to Specify Dynamic Default Arguments

動的なデフォルト引数を指定するときは `None`とdocstringを使う。

`datetime.now` は関数が定義されたときにしか評価されないので、これをデフォルト引数にすると期待した値にならない。

このような動的なデフォルト引数は、 `None` にしておいてdocstringに実際に振る舞いを記載しておく。

```python
def log(message, when=None):
    """Log a message with a timestamp.

    Args:
        message: Message to print.
        when: datetime of when the message occurred.
            Defaults to the present time.
    """
    if when is None:
        when = datetime.now()
    print(f'{when}: {message}')
```

## 25. Enforce Clarity with Keyword-Only and Position-Only Arguments

キーワード専用引数と位置専用引数を使って可読性を高める。

```python
# Bad
def safe_division(number, divisor,
                  ignore_overflow,
                  ignore_zero_division):
  ...

result = safe_division(1.0, 10**500, True, False)

# Good
def safe_division_b(number, divisor,
                    ignore_overflow=False,
                    ignore_zero_division=False):
  ...

result = safe_division_b(1.0, 10**500, ignore_overflow=True)
```

引数リストの `/` は位置専用引数の終わり、 `*` 記号はキーワード専用引数の始まり、 `/` と `*` の間は位置でもキーワードでも渡せる。

```python
def safe_division_e(numerator, denominator, /,
                    ndigits=10, *,
                    ignore_overflow=False,
                    ignore_zero_division=False):
  ...

safe_division_e(22, 7)
safe_division_e(22, 7, 5)
safe_division_e(22, 7, ndigits=2)
```

## 26. Define Function Decorators with `functools.wraps`

Pythonのデコレータは、ラップする関数への呼び出しの前後で追加コードを実行することができる。

```python
from functools import wraps

def trace(func):
    @wraps(func)
    def wrapper(*args, **kwargs):
        result = func(*args, **kwargs)
        print(f'{func.__name__}({args!r}, {kwargs!r}) '
              f'-> {result!r}')
        return result

@trace
def fibonacci(n):
    ...
```

デコレータを自分で定義するときは、 `functools` の `wraps` ヘルパー関数を使う。

[functools — Higher-order functions and operations on callable objects — Python 3.12.2 documentation#functools.wraps](https://docs.python.org/3/library/functools.html#functools.wraps)
