# 第1章 Linux環境

## 1.2.c 1+1の計算

`bc`は計算を処理するコマンド。

```
$ echo '1+1' | bc
2
```

## 1.3.a sedによる置換の練習

最初に発見した対象を置換

```
$ echo クロロエチルエチルエーテル | sed 's/エチル/メチル/'
クロロメチルエチルエーテル
```

すべて置換したい場合は後ろに`g`をつける

```
$ echo クロロメチルメチルエーテル | sed 's/メチル/エチル/g'
クロロエチルエチルエーテル
```

 `&`で検索対象の文字列を再利用する
 ```
$ echo クロロエチルエーテル | sed 's/エチル/エチルエチル/g'
クロロエチルエチルエーテル
$ echo クロロエチルエーテル | sed 's/エチル/&&/g'
クロロエチルエチルエーテル
```

検索対象の文字列をかっこで囲むと順番に番号が与えられ、置換後の文字列のところで`\1`, `\2`として検索対象を再利用できる。後方参照と呼ばれる。

```
$ echo クロロメチルエチルエーテル | sed -E 's/(メチル)(エチル)/\2\1/'
クロロエチルメチルエーテル
```

## 1.3.b grepによる検索の練習

先頭が1で始まり、その後0が0個以上最後まで続く数を抽出

```
$ seq 100 | grep "^10*$" | xargs
1 10 100
```

最後が0でも2でも4でも6でも8でもない数を抽出

```
$ seq 100 | grep "[^02468]$" | xargs
1 3 5 7 9 11 13 15 17 19 21 23 25 27 29 31 33 35 37 39 41 43 45 47 49 51 53 55 57 59 61 63 65 67 69 71 73 75 77 79 81 83 85 87 89 91 93 95 97 99
```

1桁と2桁目が同じ数を抽出

```
$ seq 100 | grep -E "^(.)\1$"| xargs
11 22 33 44 55 66 77 88 99
```

## 1.3.c grepによる検索&切り出しの練習

`-o`はマッチした部分のみを出力する

```
$ echo 中村 山田 田代 上田 | grep -o "[^ ]"田
山田
上田
```

## 1.3.d awkによる検索と計算の練習

`awk '/正規表現/'` で `grep '正規表現'`と同じ意味になる

```
$ seq 5 | awk '/[24]/'
2
4
```

読み込んだ値は`$1`に入る

```
$ seq 5 | awk '$1%2==0'
2
4
```

`awk '条件{処理}条件{処理}'`のように2つ以上の条件と処理を書くことができる。

以下の2つ目のルールは`$1%2==1`と書いてもいいが、C言語と同様、非ゼロは真なので省略。

```
$ seq 5 | awk '$1%2==0{print $1, "Even"}$1%2{print $1,"Odd"}'
1 Odd
2 Even
3 Odd
4 Even
5 Odd
```

`BEGIN`はawkが１行目の処理を始める前、`END`はawkが最終行の処理を終えた後、の状況にマッチする。

`awk 'BEGIN{事前処理}条件{処理}条件{処理}END{事後処理}'`

```
$ seq 5 | awk 'BEGIN{a=0}$1%2==0{print $1, "Even"}$1%2{print $1,"Odd"}{a+=$1}END{print "Sum", a}'
1 Odd
2 Even
3 Odd
4 Even
5 Odd
Sum 15
```

## 1.3.e sortとuniqによる集計

奇数と偶数の数を出力する例。

```
$ seq 5 | awk '$1%2==0{print "Even"}$1%2{print "Odd"}' | sort | uniq -c | sort -nr | awk '{print $2, $1}'
Odd 3
Even 2
```

## 1.3.f xargsによる一括処理

`dir_1`, `dir_2`, `dir_3`, `dir_4`という名前のディレクトリを作成する。

`-I`オプションの後ろに指定した文字列(以下では`@`)に受け取った文字列が入る。


```
$ seq 4 | xargs -I@ mkdir dir_@
```

## 1.3.g bashによるメタプログラミング

パイプから受け取ったコマンドを`bash`で実行できる。

以下の例は、`odd_1`, `odd_3`, `even_2`, `even_4`のディレクトリを作るコマンド

```
$ seq 4 | awk '$1%2==0{print "mkdir even_"$1}$1%2{print "mkdir odd_"$1}'
mkdir odd_1
mkdir even_2
mkdir odd_3
mkdir even_4

$ seq 4 | awk '$1%2==0{print "mkdir even_"$1}$1%2{print "mkdir odd_"$1}' | bash

$ ls
even_2		even_4		odd_1		odd_3
```

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

xargsの`-I`(iのupper case)オプションで指定した文字列に受け取った値が入る。上記の場合は`FILE`。

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

## 5. 設定ファイルからの情報抽出

ntp.confからpool項目にあるサーバ名を抽出する

### 準備

用意されたntp.confをpoolでgrepすると以下の通り。pool項目は、先頭がpoolで始まる行を表している。
```
$ cat ntp.conf | grep pool
# on 2011-02-08 (LP: #104525). See http://www.pool.ntp.org/join.html for
pool 0.ubuntu.pool.ntp.org iburst
pool 1.ubuntu.pool.ntp.org iburst
pool 2.ubuntu.pool.ntp.org iburst
pool 3.ubuntu.pool.ntp.org iburst
pool ntp.ubuntu.com
# Needed for adding pool entries
```

### 実行

先頭がpoolで始まる行の2列目を抽出

```
$ cat ntp.conf | grep "^pool" | awk '{print $2}'
0.ubuntu.pool.ntp.org
1.ubuntu.pool.ntp.org
2.ubuntu.pool.ntp.org
3.ubuntu.pool.ntp.org
ntp.ubuntu.com
```

grepの代わりにawkで行抽出するときは、`awk '$n=="STRING"'`が使える。これはn列目がSTRING文字列の行を抽出している。

```
$ cat ntp.conf | awk '$1=="pool"' | awk '{print $2}'
0.ubuntu.pool.ntp.org
1.ubuntu.pool.ntp.org
2.ubuntu.pool.ntp.org
3.ubuntu.pool.ntp.org
ntp.ubuntu.com
```

## 6. 端末に模様を描く

ワンライナーで以下を出力する

```
    x
   x
  x
 x
x
```

### 実行

seqで5から1まで順に渡し、awkはその分だけスペースを出力する。

```
$ seq 5 1 | awk '{for (i=1; i<=$1; i++){printf " "}; print "x"}'
     x
    x
   x
  x
 x
 ```

tacを使えば出力を逆順にできる。

```
$ seq 5 | awk '{for (i=1; i<$1; i++){printf " "}; print "x"}' | tac
    x
   x
  x
 x
x
```

## 7. 消費税

2019/10以前、もしくは、商品名の先頭に`*`が付いている時は消費税8%、それ以外は10%として計算し、合計金額を求める。

### 実行

先頭が`*`の文字列を抽出する条件は`$2~/^\*/`。税率のカラムを追加して最後に税率を掛けた分の合計を計算。

```
$ cat kakeibo.txt | awk '{tax=($1<"20191001"||$2~/^\*/)?1.08:1.1;print $0,tax}' | awk 'BEGIN{sum=0}{sum+=int($3*$4)}END{print sum}'
53612
```

## 8. ログの集計

`access.log`から午前と午後のそれぞれの行数を求める。

### 実行

タイムスタンプが含まれている4列目を抽出し、`:`でsplitして時間ごとにグルーピングしてカウント。


```
$ cat access.log | awk '{print $4}' | awk -F: '$2<12{print "Morning"}$2>=12{print "Evening"}' | sort | uniq -c
   3 Evening
   2 Morning
```

`NF`(Number of Fields)で列数を取得できるので、後ろから時間を抽出するのも良い

```
$ cat access.log | awk -F: '{print $(NF-2)}' | awk '$1<12{print "Morning"}$1>=12{print "Evening"}' | sort | uniq -c
   3 Evening
   2 Morning
```
