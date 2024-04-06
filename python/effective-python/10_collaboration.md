# **Chapter 10: Collaboration**

協働作業(コラボレーション)

Collaborating on Python programs requires you to be deliberate about how you write your code. Even if you’re working alone, you’ll want to understand how to use modules written by others. This chapter covers the standard tools and best practices that enable people to work together on Python programs.

1. Know Where to Find Community-Built Modules
2. Use Virtual Environments for Isolated and Reproducible Dependencies
3. Write Docstrings for Every Function, Class, and Module
4. Use Packages to Organize Modules and Provide Stable APIs
5. Consider Module-Scoped Code to Configure Deployment Environments
6. Define a Root `Exception` to Insulate Callers from APIs
7. Know How to Break Circular Dependencies
8. Consider `warnings` to Refactor and Migrate Usage
9. Consider Static Analysis via `typing` to Obviate Bugs

## 82. Know Where to Find Community-Built Modules

コミュニティのモジュールをどこで見つけられるかを知っておく。

Pythonパッケージインデックス PyPI を使うには `pip` コマンドラインツールを使う。

## 83. Use Virtual Environments for Isolated and Reproducible Dependencies

隔離された複製可能な依存関係のために仮想環境を使う。

`venv` で仮想環境を作ってそこにパッケージをインストールすることで、パッケージ依存関係の問題が起きないようにする。

`requirements.txt` を使って他の人とパッケージ依存関係を統一する。venvにパッケージをインストールした後に `python3 -m pip freeze` コマンドで明示的なパッケージ依存関係すべてをファイルに保存できる。

```bash
$ python3 -m pip freeze > requirements.txt
```

## 84. Write Docstrings for Every Function, Class, and Module

すべての関数、クラス、モジュールについてdocstringを書く。

[PEP 257 – Docstring Conventions](https://peps.python.org/pep-0257/) にDocstringのガイドラインが書かれている。

## 85. Use Packages to Organize Modules and Provide Stable APIs

モジュールの構成にパッケージを用い、安定したAPIを提供する。

パッケージは、モジュールの集まり。 `__init__.py` の空ファイルをディレクトリに置くことで定義される。

以下のモジュール `utils` をインポートするには `from mypackage import utils` を使う。

```python
main.py
mypackage/__init__.py
mypackage/models.py
mypackage/utils.py
```

パッケージ利用の目的は、モジュールを別々の名前空間に分割する、外部のユーザに対して厳密で安定したAPIを提供する。

`__all__` にパブリックな名前をリスとして、モジュールの明示的なAPIを提供できる。でも、自分のモジュール間で使うAPIなのであれば、わざわざ `__all__` を使う必要はない。

## 86. Consider Module-Scoped Code to Configure Deployment Environments

複数の異なるデプロイ環境の構成のためにモジュールスコープのコードを考える。

例えば、開発環境と本番環境で以下のように環境毎に異なる機能を提供する。

```python
# dev_main.py
TESTING = True
import db_connection
db = db_connection.Database()
```

```python
# prod_main.py
TESTING = False
import db_connection
db = db_connection.Database()
```

`db_connection.Database` は、定数 `TESTING` の値によって利用する `Database` が変更される。

```python
# db_connection.py
import __main__

class TestingDatabase:
    pass

class RealDatabase:
    pass

if __main__.TESTING:
    Database = TestingDatabase
else:
    Database = RealDatabase
```

## 87. Define a Root `Exception` to Insulate Callers from APIs

APIからの呼び出し元を分離するために、ルート例外を定義する。

モジュールの中にルート `Exception` を提供し、そのモジュールで起こされた他の例外はそのルート例外を継承するようにする。

ルート例外は、APIを利用するコードのバグを見つけやすくなる、API実装のバグを見つけやすくなる、将来的に例外が増えた場合に追加が楽になる、などのメリットがある。

```python
class Error(Exception):
    """Base-class for all exceptions raised by this module."""

class InvalidDensityError(Error):
    """There was a problem with a provided density value."""

class NegativeDensityError(InvalidDensityError):
    """A provided density value was negative."""

try:
    my_module.NegativeDensityError = NegativeDensityError
    my_module.determine_weight = determine_weight
    try:
        weight = my_module.determine_weight(1, -1)
    except my_module.NegativeDensityError:
        raise ValueError('Must supply non-negative density')
    except my_module.InvalidDensityError:
        weight = 0
    except my_module.Error:
        logging.exception('Bug in the calling code')
    except Exception
        logging.exception('Bug in the API code!')
        raise
except:
    logging.exception('Expected')
```

## 88. Know How to Break Circular Dependencies

循環依存を取り除く方法を知る。

循環依存は、2つのモジュールがインポート時に互いに呼び出すときに生じる。これは、プログラムを実行時にクラッシュさせる。

相互依存をリファクタリングし、依存木の底に切り離されたモジュールが来るようにする。

このような分割が常に可能とは限らない。それ以外の方法として、関数またはメソッドの中で `import` 文を使う、動的インポートをする方法がある。

## 89. Consider `warnings` to Refactor and Migrate Usage

リファクタリングや使用法のマイグレーションには `warnings` を検討する。

`warnings` を使用すると、コード修正の通知ができる。

例外はマシンによる自動エラー処理であるのに対して、 `warning` は互いに何を期待するかの人と人とのコミュニケーションである。

以下は、 `print_distance` の引数に `speed_units` が指定されていないときに、将来問題が起きるかもしれないことを通知する例。

```python
import warnings

CONVERSIONS = {
  'mph': 1.60934 / 3600 * 1000,   # m/s
  'hours': 3600,                  # seconds
  'miles': 1.60934 * 1000,        # m
  'meters': 1,                    # m
  'm/s': 1,                       # m
  'seconds': 1,                   # s
}

def convert(value, units):
    rate = CONVERSIONS[units]
    return rate * value

def print_distance(speed, duration, *, speed_units=None):
  if speed_units is None:
    warnings.warn('speed_units required', DeprecationWarning)
    speed_units = 'mph'
  norm_speed = convert(speed, speed_units)
  norm_duration = convert(duration, 'hours')
  print(norm_speed)

print_distance(1000, 3)
print_distance(1000, 3, speed_units='mph')

>>>
<stdin>:3: DeprecationWarning: speed_units required
447.0388888888889
447.0388888888889
```

## 90. Consider Static Analysis via `typing` to Obviate Bugs

バグを回避するために静的解析を検討する。

最近のPythonでは、 `typeing` モジュールを導入し、変数、クラスメソッド、関数、メソッドに型情報を与えられる。

よく使われている静的解析ツールは、mypy, pytype, pyright, pyre。実行時に生じるエラーを回避するのを助ける。

`mypy` を `--strict` フラグで実行する例:

```python
# example.py
def subtract(a: int, b: int) -> int:  # Function annotation
    return a - b

subtract(10, '5')  # Oops: passed string value
```

```python
$ mypy --strict example_02.py
example_02.py:4: error: Argument 2 to "subtract" has incompatible type "str"; expected "int"  [arg-type]
Found 1 error in 1 file (checked 1 source file)
```

型ヒントのベストプラクティス

- コードを書くとき最初から型ヒントを指定しようとすると作業が遅くなる。最初に型ヒント無しでコードを書き、テストを書き、その後に最も価値のある部分で型ヒントを書く
- 多くの呼び出し元が依存するAPIで型ヒントが重要になる。
- APIの部分以外では、コードベースで最も複雑で、エラーになりやすい箇所で型ヒントを使うのが有効。
- 可能なら静的解析をテストの自動システムに含めて、コミットのたびにチェックされるように素べき。
- コードに型情報を追加したなら、そのたびに型チェッカを実行するのが重要
