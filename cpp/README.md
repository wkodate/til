C++ Grammar
===

## コーディング

- ビルド、実行

```
g++ a.cpp

./a.out
```

- VSCodeのformatterはAlt + Shift + f

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
  
```
# 宣言
verctor<int> data(3);
# 大きさを指定せずに宣言
verctor<int> data;
# 初期値を指定して宣言
vector<int> data = {1, 2, 3};

# 追加
data.at[2] = 1;
# 要素を指定せずに追加
data.push_back(1);

# 取得
int d = data.at(0):
```

STL
- `min`, `max`, `swap`, `sort`, `reverse`

ソート

```
# asc
sort(data.begin(), data.end());
# desc
sort(data.begin(), data.end(), greater<int>());
```

並びを逆順にする
```
reverse(data.begin(), data.end());
```

### 2次元配列


```
# 宣言
vector<vector<int>> data(3, vector<int>(4));

# 初期値を指定して宣言
vector<vector<int>> mat1 = { {1, 0, 0}, {0, 0, 1}, {1, 0, 0} };
```

## Map

```
# 宣言
map<Keyの型, Valueの型> 変数名;

# 追加
map[key] = value;

# 取得
k = map.at(key);
k = map[key];

# contains
map.count(key)

# for loop
for (auto p : map) {
    key = p.first;
    value = p.second;
}
```

## Set


```
# 宣言
set<int> s;

# 追加
s.insert(3);
s.insert(4);

# contains
if (s.count(3)) {
    cout << "found 3" << endl;
}

# loopで取得
for (auto num : s) {
  cout << num << endl;
}
```

## pair

2つの値の組を表す。

```
# 宣言
pair<string int> p;
pair<string int> p("abc", 3);

# pairの生成
p = make_pair("hello", 2)

# 取得
p.first()
p.second()
```

## Priority queue

優先度付きキュー。追加した要素のうち最も大きいものを取り出す。

```
# 宣言
priority_queue<int> queue;

# 小さい順に取り出されるpriority queueの宣言
priority_queue<int, vector<int>, greater<int>> queue;

# 追加
queue.push(1);

# 要素数の取得. size_t型を返す
queue.size(); 

# 最大の要素を取得
queue.top();

# 最大の要素を削除
queue.pop()

# queueが空かどうか
while(!queue.empty()) {
  # 最大値を取り出して削除
  cout << queue.top() << endl;
  queue.pop();
}
```