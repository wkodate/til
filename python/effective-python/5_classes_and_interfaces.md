# **Chapter 5: Classes and Interfaces**

クラスと継承

Python is an object-oriented language. Getting things done in Python often requires writing new classes and defining how they interact through their interfaces and hierarchies. This chapter covers how to use classes to express your intended behaviors with objects.

- 37. Compose Classes Instead of Nesting Many Levels of Built-in Types
- 38. Accept Functions Instead of Classes for Simple Interfaces
- 39. Use `@classmethod` Polymorphism to Construct Objects Generically
- 40. Initialize Parent Classes with `super`
- 41. Consider Composing Functionality with Mix-in Classes
- 42. Prefer Public Attributes Over Private Ones
- 43. Inherit from `collections.abc` for Custom Container Types

## 37. Compose Classes Instead of Nesting Many Levels of Built-in Types

深いネストなどでクラスが複雑になりそうならクラスに分ける。

以下の例だと、 `average_grade` が二重ループが含まれて記録管理が複雑になっている。

変更前

```python
class WeightedGradebook:
    def __init__(self):
        self._grades = {}

    def add_student(self, name):
        self._grades[name] = defaultdict(list)

    def report_grade(self, name, subject, score, weight):
        by_subject = self._grades[name]
        grade_list = by_subject[subject]
        grade_list.append((score, weight))

    def average_grade(self, name):
        by_subject = self._grades[name]

        score_sum, score_count = 0, 0
        for subject, scores in by_subject.items():
            subject_avg, total_weight = 0, 0
            for score, weight in scores:
                subject_avg += score * weight
                total_weight += weight

            score_sum += subject_avg / total_weight
            score_count += 1

        return score_sum / score_count
```

変更後

```python
from collections import namedtuple

Grade = namedtuple('Grade', ('score', 'weight'))

class Subject:
    def __init__(self):
        self._grades = []

    def report_grade(self, score, weight):
        self._grades.append(Grade(score, weight))

    def average_grade(self):
        total, total_weight = 0, 0
        for grade in self._grades:
            total += grade.score * grade.weight
            total_weight += grade.weight
        return total / total_weight

class Student:
    def __init__(self):
        self._subjects = defaultdict(Subject)

    def get_subject(self, name):
        return self._subjects[name]

    def average_grade(self):
        total, count = 0, 0
        for subject in self._subjects.values():
            total += subject.average_grade()
            count += 1
        return total / count

class Gradebook:
    def __init__(self):
        self._students = defaultdict(Student)

    def get_student(self, name):
        return self._students[name]
```

`namedtuple` でイミュータブルな小さなデータクラスを簡単に定義できる。

コードは長くなったけどわかりやすくなった。

## 38. Accept Functions Instead of Classes for Simple Interfaces

シンプルなインターフェースのクラスの代わりに関数を使う。

Pythonには関数を渡すことで振る舞いをカスタマイズできる組み込みAPIのフックがある。 `sort` の `key` 引数など。

`__call__` メソッドを使用すると、クラスのインスタンスをPythonの関数として呼び出すことが可能になる。

```python
# Bad
class CountMissing:
    def __init__(self):
        self.added = 0

    def missing(self):
        self.added += 1
        return 0

counter = CountMissing()
result = defaultdict(counter.missing, current)  # Method ref
for key, amount in increments:
    result[key] += amount
assert counter.added == 2
print(result)

# Good
class BetterCountMissing:
    def __init__(self):
        self.added = 0

    def __call__(self):
        self.added += 1
        return 0

counter = BetterCountMissing()
result = defaultdict(counter, current)  # Relies on __call__
for key, amount in increments:
    result[key] += amount
assert counter.added == 2
print(result)
```

## 39. Use `@classmethod` Polymorphism to Construct Objects Generically

ジェネリックにオブジェクトを生成するために `@classmethod` ポリモーフィズムを使用する。

Pythonではクラスに対して `__init__` メソッドという1つのコンストラクタしかサポートしていない。

サブクラスのコンストラクタを定義するために `@classmethod` を使う。

```python
# Parent class
class GenericInputData:
    def read(self):
        raise NotImplementedError

    @classmethod
    def generate_inputs(cls, config):
        raise NotImplementedError

class GenericWorker:
    def __init__(self, input_data):
        self.input_data = input_data
        self.result = None

    def map(self):
        raise NotImplementedError

    def reduce(self, other):
        raise NotImplementedError

    @classmethod
    def create_workers(cls, input_class, config):
        workers = []
        for input_data in input_class.generate_inputs(config):
            workers.append(cls(input_data))
        return workers

# Subclass
class PathInputData(GenericInputData):
    def __init__(self, path):
        super().__init__()
        self.path = path

    def read(self):
        with open(self.path) as f:
            return f.read()

    @classmethod
    def generate_inputs(cls, config):
        data_dir = config['data_dir']
        for name in os.listdir(data_dir):
            yield cls(os.path.join(data_dir, name))

class LineCountWorker(GenericWorker):
    def map(self):
        data = self.input_data.read()
        self.result = data.count('\n')

    def reduce(self, other):
        self.result += other.result

# Connect all the pirces together to run each step
def mapreduce(worker_class, input_class, config):
    workers = worker_class.create_workers(input_class, config)
    return execute(workers)

# Call mapreduce
config = {'data_dir': tmpdir}
result = mapreduce(LineCountWorker, PathInputData, config)
```

## 40. Initialize Parent Classes with `super`

`super` でスーパークラスを初期化する。

`super().__init__` の呼び出しは、多重継承を頑強にするだけでなく、親クラスの `__init__` をサブクラスから呼び出すよりも保守性が大幅に良くなる。

```python
# Bad
class ThisWay(TimesSeven, PlusNine):
    def __init__(self, value):
        TimesSeven.__init__(self, value)
        PlusNine.__init__(self, value)

# Good
class GoodWay(TimesSevenCorrect, PlusNineCorrect):
    def __init__(self, value):
        super().__init__(value)
```

`super` に引数なしで使う。引数を渡さないといけないのは、スーパークラスの実装の特定の機能にサブクラスからアクセスしなければいけないときだけ。

## 41. Consider Composing Functionality with Mix-in Classes

クラスの多重継承は酒、Mix-inクラスで機能合成を考える。

mix-inは、サブクラスが提供すべき一連の追加メソッドを定義するクラス。インスタンス属性を持たず、 `__init__` コンストラクタを呼び出す必要もない。

```python
# Mix-in class
class ToDictMixin:
    def to_dict(self):
        return self._traverse_dict(self.__dict__)

    def _traverse_dict(self, instance_dict):
        output = {}
        for key, value in instance_dict.items():
            output[key] = self._traverse(key, value)
        return output

    def _traverse(self, key, value):
        if isinstance(value, ToDictMixin):
            return value.to_dict()
        elif isinstance(value, dict):
            return self._traverse_dict(value)
        elif isinstance(value, list):
            return [self._traverse(key, i) for i in value]
        elif hasattr(value, '__dict__'):
            return self._traverse_dict(value.__dict__)
        else:
            return value

# A class that uses the mix-in
class BinaryTree(ToDictMixin):
    def __init__(self, value, left=None, right=None):
        self.value = value
        self.left = left
        self.right = right

tree = BinaryTree(10,
    left=BinaryTree(7, right=BinaryTree(9)),
    right=BinaryTree(13, left=BinaryTree(11)))
tree.to_dict()
```

## 42. Prefer Public Attributes Over Private Ones

クラスの属性の可視性はPrivateよりPublicのほうが良い。

プライベートフィールドにするには、属性の名前の戦闘にアンダースコアを2つつける。

内部的に `__private_field` は `MyChildObject__private_field` に変換されているので、実は任意のクラスのプライベート属性にアクセスできる。

```python
class MyParentObject:
    def __init__(self):
        self.__private_field = 71

class MyChildObject(MyParentObject):
    def get_private_field(self):
        return self.__private_field

baz = MyChildObject()
print(baz.__dict__)
print(baz._MyParentObject__private_field)
print(baz.get_private_field())

>>> 
{'_MyParentObject__private_field': 71}
71
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
  File "<stdin>", line 3, in get_private_field
AttributeError: 'MyChildObject' object has no attribute '_MyChildObject__private_field'. Did you mean: '_MyParentObject__private_field'?
```

プライベートでアクセス世業するのではなく、保護フィールドそれぞれにどのAPIが利用可能かを文書化すべき。

```python
class MyStringClass:
    def __init__(self, value):
        # This stores the user-supplied value for the object.
        # It should be coercible to a string. Once assigned in
        # the object it should be treated as immutable.
        self._value = value
```

プライベートを利用するのは、サブクラスとの名前の衝突を気にする必要があるときだけ。

## 43. Inherit from `collections.abc` for Custom Container Types

カスタムコンテナ型は `[collections.abc](http://collections.abc)` を継承する。

自分でコンテナ型を定義するときは多数のメソッドが必要になる。これらをすべて備えているかを確かめるために、カスタムのコンテナ型は `[collections.abc](http://collections.abc)` dえ定義されたインターフェースを継承する。

以下シーケンス型の `BadType` クラスには、 `__getitem__` と `__len__` が不足していることがわかる。

```python
from collections.abc import Sequence

class BadType(Sequence):
    pass

foo = BadType()

>>>
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
TypeError: Can't instantiate abstract class BadType with abstract methods __getitem__, __len__
```
