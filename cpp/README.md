C++ Grammar
===

## コーディング

- ビルド

```
g++ a.cpp
```

- formatterはAlt + Shift + f

## 数値型

double型の値を出力する際に小数点以下の桁数を指定。以下は小数点以下15桁を表示する。

```
cout << fixed << setprecision(15);
```

## 文字列

文字列の長さ。

```
str.size()
```

文字列の結合は`+`を使う.

```
string str = "Hello"
str += ", World."
```

文字列の切り出し。0文字目から2文字分切り出す。

```
s = "abc";
s.sbstr(0, 2);  // "ab"
```

文字列の反転
```
reverse(str.begin(), str.end());
```

string -> integerの変換

```
int i = atoi(str.c_str());
```

integer -> stringの変換

```
string str = to_string(integer);
```

contains。下の例はsにtが含まれているかどうか。

```
if (s.find(t) != string::npos) {
    cout << "s contains t" << endl;
}
```

## 配列

- vectorによる配列の使用を推奨
- 宣言はint data[3] よりもverctor<int> data(3) を推奨
- 配列へのアクセスはvec[0] よりもvec.at(0)を推奨
  
宣言

```
verctor<int> data(3);
// 大きさを指定せずに宣言
verctor<int> data;
```

set
```
data.at[2] = 1;
// 要素を指定せずに追加
data.push_back(1);
```

get
```
int d = data.at(0):
```


STL
- min, max, swap, sort, reverse

ソート

```
// asc
sort(a.begin(), a.end());
// desc
sort(a.begin(), a.end(), greater<int>());
```

### 2次元配列

宣言

```
vector<vector<int>> data(3, vector<int>(4));
```
```
vector<vector<int>> mat1 = {{1, 0, 0}, {0, 0, 1}, {1, 0, 0}};
```

## Map

宣言

```
map<Keyの型, Valueの型> 変数名;
```

set

```
map[key] = value;
```

get

```
map.at(key)
```

```
map[key]
```

contains

```
map.count(key)
```

for loop

```
for (auto p : map) {
    key = p.first;
    value = p.second;
}
```

## Set

宣言

```
set<int> s;
```

add

```
s.insert(3);
s.insert(4);
```

contains

```
if (s.count(3)) {
    cout << "found 3" << endl;
}
```

## pair

2つの値の組を表す。

宣言
```
pair<string int> p;
pair<string int> p("abc", 3);
```

pariの生成
```
p = make_pair("hello", 2)
```

get
```
p.first()
p.second()
```