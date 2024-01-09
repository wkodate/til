# **Chapter 1: Pythonic Thinking**

Pythonic思考

The Python community has come to use the adjective *Pythonic* to describe code that follows a particular style. The idioms of Python have emerged over time through experience using the language and working with others. This chapter covers the best way to do the most common things in Python.

1. Know Which Version of Python You’re Using
2. Follow the PEP 8 Style Guide
3. Know the Differences Between `bytes` and `str`
4. Prefer Interpolated F-Strings Over C-style Format Strings and `str.format`
5. Write Helper Functions Instead of Complex Expressions
6. Prefer Multiple Assignment Unpacking Over Indexing
7. Prefer `enumerate` Over range
8. Use `zip` to Process Iterators in Parallel
9. Avoid `else` Blocks After `for` and `while` Loops
10. Prevent Repetition with Assignment Expressions

## 1. Know Which Version of Python You’re Using

Pythonのバージョンを知っておく。

```python
$ python --version
Python 3.11.4

$ python
>>> import sys
>>> print(sys.version_info)
sys.version_info(major=3, minor=11, micro=4, releaselevel='final', serial=0)
>>> print(sys.version)
3.11.4 (main, Jul 25 2023, 17:36:13) [Clang 14.0.3 (clang-1403.0.22.14.1)]
```

## 2. Follow the PEP 8 Style Guide

[PEP8スタイルガイド](https://peps.python.org/pep-0008/)に従う。

[PyLint](https://www.pylint.org/)を使えば自動で確認ができる。

一部の例

- 式の否定(`if not a is b`)ではなく、内側の項の否定(`if a is not b`)を使う。
- `if len(somelist) == 0`のように長さを使って`[]`や`''`をチェックしない。`if not somelist`を使う。
- `\`で行を分けるよりカッコを使って複数の式を囲む。

## 3. Know the Differences Between `bytes` and `str`

bytesとstrの違いを知っておく。

bytesは、符号なし8ビット値のASCIIエンコーディング。テキストエンコーディングを持たない。

strは、Unicodeコードポイントの文字列。バイナリエンコーディングを持たない。

```python
# bytes
>>> a = b'h\x65llo'
>>> print(list(a))
[104, 101, 108, 108, 111]
>>> print(a)
b'hello'

# str
>>> a = 'a\u0300 propos'
>>> print(list(a))
['a', '̀', ' ', 'p', 'r', 'o', 'p', 'o', 's']
>>> print(a)
à propos
```

bytesとstrはいくつかの演算子で一緒に使うことはできない。

```python
# bytes + str
>>> b'one' + 'two'
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
TypeError: can't concat str to bytes

# bytes > str
>>> assert b'red' > 'blue'
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
TypeError: '>' not supported between instances of 'bytes' and 'str'

# bytes == str
>>> print(b'foo' == 'foo')
False
```

ファイルハンドルはデフォルトではUnicode文字列が必要

```python
# bytesをread/writeするときは'b' modeが必要
>>> with open('data.bin', 'w') as f:
...   f.write(b'\xf1\xf2\xf3\xf4\xf5')
...
Traceback (most recent call last):
  File "<stdin>", line 2, in <module>
TypeError: write() argument must be str, not bytes
>>> with open('data.bin', 'wb') as f:
...   f.write(b'\xf1\xf2\xf3\xf4\xf5')
...
5

>>> with open('data.bin', 'r') as f:
...   f.read()
...
Traceback (most recent call last):
  File "<stdin>", line 2, in <module>
  File "<frozen codecs>", line 322, in decode
UnicodeDecodeError: 'utf-8' codec can't decode byte 0xf1 in position 0: invalid continuation byte
>>> with open('data.bin', 'rb') as f:
...   f.read()
...
b'\xf1\xf2\xf3\xf4\xf5'
```

Unicodeデータを読み書きするときは、システムのデフォルトのテキスト符号化に注意。 `open`で `encoding` パラメータを明示的に指定する。

```python
# システムのテキストエンコーディングを確認
>>> import locale
>>> print(locale.getpreferredencoding())

# 'cp1252'を指定
with open('data.bin', 'r', encoding='cp1252') as f:
```

## 4. Prefer Interpolated F-Strings Over C-style Format Strings and `str.format`

Cスタイルフォーマットや `str.format`を使わず、f文字列(format string: フォーマット済み文字列)で埋め込む。

```python
>>> key = 'my_var'
>>> value = 1.234

# Cスタイルフォーマット
>>> formatted = '%-10s = %.2f' % (key, value)
>>> print(formatted)
my_var     = 1.23

# str.format
>>> formatted = '{} = {}'.format(key, value)
>>> print(formatted)
my_var = 1.234

# f文字列
>>> formatted = f'{key} = {value}'
>>> print(formatted)
my_var = 1.234
```

## 5. Write Helper Functions Instead of Complex Expressions

複雑な式の代わりにHelper関数を使う。

```python
>>> my_values
{'red': ['5'], 'blue': ['0'], 'green': ['']}

>>> int(my_values.get('red', [''])[0] or 0)
5
>>> int(my_values.get('green', [''])[0] or 0)
0
>>> int(my_values.get('opacity', [''])[0] or 0)
0
```

Helper関数を使う。

```python
def get_first_int(values, key, default=0):
    found = values.get(key, [''])
    if found[0]:
        return int(found[0])
    return default

>>> get_first_int(my_values, 'green')
0
```

無理に多数のロジックを1行にまとめない。

## 6. Prefer Multiple Assignment Unpacking Over Indexing

インデックスではなくアンパックを使う。

Pythonでは1つの代入文で複数の値を代入できるアンパック構文がある。コレクションの要素を一括で変数に代入できる。

```python
# アンパック構文
>>> item = ('Peanut butter', 'Jelly')
>>> first, second = item
>>> print(first, 'and', second)
Peanut butter and Jelly

snacks = [('bacon', 350), ('donut', 240), ('muffin', 190)]
# インデックスの例
for i in range(len(snacks)):
	item = snacks[i]
	name = item[0]
	calories = item[1]
	print(f'#{i+1}: {name} has {calories} calories')

# アンパックの例
for rank, (name, calories) in enumerate(snacks, 1):
	print(f'#{rank}: {name} has {calories} calories')
```

## 7. Prefer `enumerate` Over range

## 8. Use `zip` to Process Iterators in Parallel

## 9. Avoid `else` Blocks After `for` and `while` Loops

## 10. Prevent Repetition with Assignment Expressions
