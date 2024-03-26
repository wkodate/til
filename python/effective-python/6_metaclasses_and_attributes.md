# **Chapter 6: Metaclasses and Attributes**

メタクラスと属性

メタクラスは、Pythonの `class` 文に割り込んでクラスが定義されるたびに特別な振る舞いを与える。

メタクラスの使用方法は、クラスが正しく定義されていることの検証、型の自動登録、クラス定義後の実際に使われる前の属性の修正や注釈の追加、など。

Metaclasses and dynamic attributes are powerful Python features. However, they also enable you to implement extremely bizarre and unexpected behaviors. This chapter covers the common idioms for using these mechanisms to ensure that you follow the *rule of least surprise*.

44. Use Plain Attributes Instead of Setter and Getter Methods
45. Consider `@property` Instead of Refactoring Attributes
46. Use Descriptors for Reusable `@property` Methods
47. Use `__getattr__`, `__getattribute__`, and `__setattr__` for Lazy Attributes
48. Validate Subclasses with `__init_subclass__`
49. Register Class Existence with `__init_subclass__`
50. Annotate Class Attributes with `__set_name__`
51. Prefer Class Decorators Over Metaclasses for Composable Class Extensions

## 44. Use Plain Attributes Instead of Setter and Getter Methods

getterやsetterメソッドは使わず属性を直接使う。

```python
class Resistor:
    def __init__(self, ohms):
        self.ohms = ohms
        self.voltage = 0
        self.current = 0

# Bad
r0 = Resistor(50e3)
print('Before:', r0.get_ohms())
r0.set_ohms(10e3)
print('After: ', r0.get_ohms())

# Good
r1 = Resistor(50e3)
r1.ohms = 10e3
print(f'{r1.ohms} ohms, '
      f'{r1.voltage} volts, '
      f'{r1.current} amps')
```

属性が設定されたときに特別な振る舞いが必要になる場合は `@property` デコレータを使う。[組み込み関数 — Python 3.12.2 ドキュメント#property](https://docs.python.org/ja/3/library/functions.html#property)

## 45. Consider `@property` Instead of Refactoring Attributes

属性をリファクタリングする代わりに `@property` を検討する。

以下はquotaをリファクタリングする代わりに `@property` メソッドを使ってquota属性を修正した例。

```python
class NewBucket:
    def __init__(self, period):
        self.period_delta = timedelta(seconds=period)
        self.reset_time = datetime.now()
        self.max_quota = 0
        self.quota_consumed = 0

    def __repr__(self):
        return (f'NewBucket(max_quota={self.max_quota}, '
                f'quota_consumed={self.quota_consumed})')

    @property
    def quota(self):
        return self.max_quota - self.quota_consumed

    @quota.setter
    def quota(self, amount):
        delta = self.max_quota - amount
        if amount == 0:
            # Quota being reset for a new period
            self.quota_consumed = 0
            self.max_quota = 0
        elif delta < 0:
            # Quota being filled during the period
            self.max_quota = amount + self.quota_consumed
        else:
            # Quota being consumed during the period
            self.quota_consumed = delta
```

頻繁に `@property` メソッドを修正するようになったら、クラスのリファクタリングを検討する。

## 46. Use Descriptors for Reusable `@property` Methods

`@property` メソッドの再利用にはディスクリプタを使う。

`#property` でデコレートするメソッドを同じクラスの複数の属性で使うことはできない。

Pythonのディスクリプタは、 `__get__` メソッドや `__set__` メソッドを実装したクラスのことを指し、これによって属性にアクセスしたときのロジックを異なる属性で再利用できる。

以下の例では、math_grade, writing_grade, science_grade でGradeの `__get__` と `__set__` を利用できる。

```python
class Grade:
    def __init__(self):
        self._values = WeakKeyDictionary()

    def __get__(self, instance, instance_type):
        if instance is None:
            return self
        return self._values.get(instance, 0)

    def __set__(self, instance, value):
        if not (0 <= value <= 100):
            raise ValueError(
                'Grade must be between 0 and 100')
        self._values[instance] = value

class Exam:
    math_grade = Grade()
    writing_grade = Grade()
    science_grade = Grade()
```

`_value` 辞書は `__set__` に渡されるすべての `Exam` インスタンスへの参照を保持するのでメモリリークを起こす可能性がある。ディスクリプタクラスがメモリリークを起こさないように `WeakKeyDictionary` を使う。

## 47. Use `__getattr__`, `__getattribute__`, and `__setattr__` for Lazy Attributes

遅延属性には `__getattr__` , `__getattribute__` , `__setattr__` を使う。

クラスが `__getattr__` を定義していれば、オブジェクトのインスタンス辞書に属性が見つからないときに `__getattr__` が呼び出される。呼ばれるのは一度だけ。

以下の例では、 `foo` が定義されていないので、 `__getattr__` が呼ばれている。

```python

# w/o __getattr__
class LazyRecord:
  def __init__(self):
    self.exists = 5

data = LazyRecord()
print('Before:', data.__dict__)
print('foo:   ', data.foo)
print('After: ', data.__dict__

>>>
Before: {'exists': 5}
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
AttributeError: 'LazyRecord' object has no attribute 'foo'
After:  {'exists': 5}

# w/ __getattr__
class LazyRecord:
  def __init__(self):
    self.exists = 5
  def __getattr__(self, name):
    value = f'Value for {name}'
    setattr(self, name, value)
    return value

data = LazyRecord()
print('Before:', data.__dict__)
print('foo:   ', data.foo)
print('After: ', data.__dict__)

>>>
Before: {'exists': 5}
foo:    Value for foo
After:  {'exists': 5, 'foo': 'Value for foo'}
```

一方、 `__getattribute__` はアクセスがあるたびに呼ばれる。

```python
# __getattribute__
class ValidatingRecord:
  def __init__(self):
    self.exists = 5
  def __getattribute__(self, name):
    print(f'* Called __getattribute__({name!r})')
    try:
      value = super().__getattribute__(name)
      print(f'* Found {name!r}, returning {value!r}')
      return value
    except AttributeError:
      value = f'Value for {name}'
      print(f'* Setting {name!r} to {value!r}')
      setattr(self, name, value)
      return value

data = ValidatingRecord()
print('exists:     ', data.exists)
print('First foo:  ', data.foo)
print('Second foo: ', data.foo)

>>>
# >>> data = ValidatingRecord()
# >>> print('exists:     ', data.exists)
* Called __getattribute__('exists')
* Found 'exists', returning 5
exists:      5
# >>> print('First foo:  ', data.foo)
* Called __getattribute__('foo')
* Setting 'foo' to 'Value for foo'
First foo:   Value for foo
# >>> print('Second foo: ', data.foo)
* Called __getattribute__('foo')
* Found 'foo', returning 'Value for foo'
Second foo:  Value for foo

```

`__setattr__` はデータを遅延的に戻したいときに属性に代入する。これも属性がインスタンスで代入されるたびに呼び出される。

## 48. Validate Subclasses with `__init_subclass__`

サブクラスを `__init_subclass__` で検証する。

メタクラスは、クラスの定義後かつ作成される前に、妥当性検証などに使われる。

`__init_subclass__` を使ってサブクラスを定義して、その方のオブジェクトが作られる前に正しく定義されることを確認できる。

以下は `BetterPolygon` クラスを継承した `Hexagon` クラスの検証。

```python
class BetterPolygon:
    sides = None  # Must be specified by subclasses

    def __init_subclass__(cls):
        super().__init_subclass__()
        if cls.sides < 3:
            raise ValueError('Polygons need 3+ sides')

    @classmethod
    def interior_angles(cls):
        return (cls.sides - 2) * 180

class Hexagon(BetterPolygon):
    sides = 6

assert Hexagon.interior_angles() == 720
```

クラス階層の複数のレベルで定義したい場合は、 `__init_subclass__` 内で `super().__init_subclass__` を呼ぶ。

## 49. Register Class Existence with `__init_subclass__`

クラスを `__init_subclass__` で登録する。

メタクラス構文の複雑さを避けて単純化することができる。サブクラス定義のときに必ず呼ぶ必要があるメソッドを記述しておくなど。

## 50. Annotate Class Attributes with `__set_name__`

クラス属性に `__set_name__` で注釈を加える。

ディスクリプタで `__set_name__` を定義すると、取り込むクラスとその属性名についての処理ができる。

以下のように、スーパークラスから継承したりメタクラスを使わなくても `Field` ディスクリプタを利用できる。

```python
class Field:
  def __init__(self):
    self.name = None
    self.internal_name = None

  def __set_name__(self, owner, name):
    # Called on class creation for each descriptor
    self.name = name
    self.internal_name = '_' + name

  def __get__(self, instance, instance_type):
    if instance is None:
      return self
    return getattr(instance, self.internal_name, '')

  def __set__(self, instance, value):
    setattr(instance, self.internal_name, value)

class FixedCustomer:
  first_name = Field()
  last_name = Field()
  prefix = Field()
  suffix = Field()

cust = FixedCustomer()
print(f'Before: {cust.first_name!r} {cust.__dict__}')
cust.first_name = 'Mersenne'
print(f'After:  {cust.first_name!r} {cust.__dict__}')

>>>
Before: '' {}
After:  'Mersenne' {'_first_name': 'Mersenne'}
```

## 51. Prefer Class Decorators Over Metaclasses for Composable Class Extensions

合成可能なクラス拡張のためにはメタクラスではなくクラスデコレータを使う。

クラスデコレータは `class` インスタンスを引数として、新たなクラスまたはサブクラスの修正バージョンを返す関数。

クラスデコレータは関数デコレータと同じように、関数名の前に `@` をつけてクラス宣言の前に置くことで、クラス定義時に実行できる。

デコレートっするクラスでメタクラスが定義済みでも使うことができる。
```python
def my_class_decorator(klass):
  klass.extra_param = 'hello'
  return klass

@my_class_decorator
class MyClass:
  pass

print(MyClass)
print(MyClass.extra_param)  

>>>
<class '__main__.MyClass'>
hello
```
