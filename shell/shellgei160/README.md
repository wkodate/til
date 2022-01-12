## 1. ファイル名の検索

files.txtから`.exe`の拡張子を持つファイルだけを抜き出す

### 実行

```
$ grep "\.exe$" files.txt
test.exe
画面仕様書.xls.exe
```

## 2. 画像ファイルの一括変換

PNG形式の画像をconvertコマンドでJPEG形式に変換する

### 実行

```
$ ls *.png | sed 's/\.png$//' | xargs -I FILE convert FILE.png FILE.jpg

```

xargsの-I(iのupper case)オプションで指定した文字列に受け取った値が入る。上記の場合は`FILE`。

## 3. ファイル名の一括変換

ファイル名の先頭に0をつけて4桁に揃える。本では7桁だがファイル数が多いので4桁にした

### 準備

ファイルを作成

```
$ mkdir tmp && cd tmp

$ seq 1000 | xargs touch

$ ls | wc -l
    1000

$ ls | head
1
10
100
1000
101
102
103
104
105
106
```

### 実行

ファイル名を変更。 awkで rename前後のファイル名を用意してxargsに渡す。

```
$ ls | sed 's/^\.\///' | awk '{print $1, sprintf("%04d", $1)}' | xargs -n2  mv
```

### 確認

```
$ ls | head
0001
0002
0003
0004
0005
0006
0007
0008
0009
0010
```

## 4. 特定のファイルの削除

ファイルの中身が10のファイルを削除する

### 準備

100000個のファイルを作成。BashのRANDOM変数は0-32767の中からランダムで1つの整数が選ばれる。

```
$ mkdir tmp && cd tmp

$ seq 100000 | sed 's/^/echo $RANDOM> /' | bash

$ grep -r "^10$" .
./34854:10
```

ファイルの中身が10のファイルが1つ作られていた。

### 実行

マッチしたファイル名をxargsに渡してrmする

```
$ grep -rl "^10$" . | xargs rm
```

grepの`-l`オプションを使えばファイル名だけを出力できるので、これを使えばわざわざawkなどでファイル名を抽出する必要がなくなる。

man grepの結果

>      -l, --files-with-matches
>             Only the names of files containing selected lines are written to standard output.  grep will only search a file until a match has been found, making
>             searches potentially less expensive.  Pathnames are listed once per file searched.  If the standard input is searched, the string ``(standard
>             input)'' is written.

### 確認

マッチしたファイルが削除されている。

```
$ ls 34854
ls: 34854: No such file or director

$ ls | wc -l
   99999
```