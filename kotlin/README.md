## Introduction

### Function

- voidのように返り値でなにも返さない場合は`Unit`。 省略可能
- 関数の引数にデフォルト値を指定できる
- `val`で変数を定義。immutable。基本的にはこれを使う。`var`は変更する場合のみ
- 標準ライブラリに`Pair`型がある

```kotlin
val pair = "A" to "B"
println(pair)    // (A, B)
```

- `vararg`で引数を可変長にできる

```kotlin
fun printAll(vararg messages: String) {
    for (m in messages) println(m)
}
printAll("Hello", "Hallo", "Salut", "Hola", "你好")
```

### Null Safety

- `let`は、引数として関数を受け取り、元の関数がnullの場合はnullを返す。JavaのOptionalに似ている。
- 基本的にnon null typeを使う。必要な時だけ null type(`?`をつける)
    - 普通の型に`?`をつけるとnullの値を持つことができる、NPEを発生しにくくする
- `?:` エルビス演算子
    - 三項演算子 `A ? B : C` のBが内容な機能。これでnullの場合の処理を書くことができる

例

```jsx
printString(todo.text ?: "")   //  todo.textがnullの場合にから文字を出力
```

### Classes

- コンストラクタを定義しないと自動で引数なしコンストラクタが生成される
- 1つのプライマリコンストラクタと複数のセカンダリコンストラクタが使える
- インスタンス生成に`new`は不要

- `List`は中身の変更が不可、`mutableList`は変更可能。同様に`mutableMap`もある
- 参照先が同じかどうか`===`、構造の等価性(equalsメソッドによる同一性チェック)は`==`
- if文で値を返すこともできる
- whenはswitch文
- `MutableList`から`List`にアップキャストできる
- 関数はパッケージのトップレベルで宣言できるのでクラスメソッドにする必要はない
- 
- すべてのクラスは`Any`を継承する
- データクラスは、データを保持したいだけのクラスとして使われる。class宣言の最初に`data`をつける
    - setter, getterの宣言は不要
- コンパニオンオブジェクトはstaticのメンバ変数を定義する
- 型チェックに`is`と`!is`が使える。チェックした後自動的にキャストされる

- `use`はJavaの`try-with-resources`
- `buildString`はJavaの`StringBuilder`