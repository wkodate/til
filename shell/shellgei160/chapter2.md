# 第2章 シェルの基本

## 2.1.a 標準入出力・標準エラー出力

標準入力は`<`もしくは`0<`で表す。

標準出力は`>`もしくは`1>`で表す。

標準エラー出力は`2>`で表す。


```
$ gsed 2> a

$ head a
Usage: gsed [OPTION]... {script-only-if-no-other-script} [input-file]...

  -n, --quiet, --silent
                 suppress automatic printing of pattern space
      --debug
                 annotate program execution
  -e script, --expression=script
                 add the script to the commands to be executed
  -f script-file, --file=script-file
                 add the contents of script-file to the commands to be executed
```

## 2.1.c 文字列の結合と置換

`${a:0:1}`は`a`の0文字目から1文字、という意味。


```
$ echo $a
私は俳優よ

$ b=${a:0:1}${a:2:2}; echo $b
私俳優
```

## 2.1.d 変数を使った計算

`((式))`で四則演算できる。

```
$ a=6
$ b=2
$ echo $((a+b))
8
```

## 2.1.f Bashの配列と連想配列

`declare -A`で連想配列を作る。

```
$ declare -A b
$ b["SHELL"]="$SHELL"
$ b["LANG"]="$LANG"
$ b["USER"]="$USER"

$ echo ${b["LANG"]}
ja_JP.UTF-8
```

`[@]`や`[*]`で全ての要素を出力する。

```
$ echo ${b[@]}
/usr/local/bin/bash MY_USER_NAME ja_JP.UTF-8 2

$ echo ${b[*]}
/usr/local/bin/bash MY_USER_NAME ja_JP.UTF-8 2
```

## 2.1.g 繰り返しと終了ステータス

setコマンドでBashの$1, $2, ...の変数に値をセットできる。

```
$ set aa bb cc

$ echo $2
bb
```

## 2.1.f 条件分岐

Bashの条件分岐の書き方。

```
$ if echo $a | grep '[02468]$'; then echo Even; elif echo $a | grep '[13579]$'; then echo Odd; else echo Other; fi
0
Even

$ a=1
$ if echo $a | grep '[02468]$'; then echo Even; elif echo $a | grep '[13579]$'; then echo Odd; else echo Other; fi
1
Odd

$ a=x
$ if echo $a | grep '[02468]$'; then echo Even; elif echo $a | grep '[13579]$'; then echo Odd; else echo Other; fi
Other
```

同じ動きをするシェルスクリプト。

```
#!/bin/bash

if grep '[02468]$' <<< "$1"; then
  echo Even
elif grep '[13579]$' <<< "$1"; then
  echo Odd
else
  echo Other
fi
```

数字の大小の比較

```
$ a=0

$ [ 10 -gt "$a" ]
$ echo $?
0

$ [ -1 -gt "$a" ]
$ echo $?
1
```

文字列の比較

```
$ a="Yes we can!"

$ [ "$a" = "No we cannot!" ]
$ echo $?
1
```

testを使って比較

```
$ a=0

$ test 10 -gt "$a"
$ echo $?
0
```

ファイルの存在チェック

```
$ [ -e /etc/passwd ]

$ echo $?
0
```

## 12. 変数の読み込み

標準入力あるいは引数で受け取った数字を倍にして返す。

`read`で標準入力を受け取る。

`$#`で引数の個数を取得できる。


### 実行

double.bash

```
#!/bin/bash

v=0
if [ $# = 0 ]; then
  read val
  v=$val
else
  v=$1
fi

echo $((v * 2))
```

出力の確認

```
$ bash double.bash 5
10

$ echo 3 | bash double.bash
6

$ echo 3 | bash double.bash 5
10
```

## 13. 存在しないファイルの初期化

ファイルがなければファイルを作成する。exit statusが0を返すようにする。

### 実行

OR演算子を使用し、ファイルが存在しなければtouch unfileが実行される。

```
$ [ -e unfile ] || touch unfile

$ echo $?
0
```

別解。`<>`は読み書きモードでファイルを開くの意味。unfileが存在すれば読み込み、存在しなければ作る。

```
$ cat <> unfile

$ echo $?
0
```

## 14. さまざまなループ

1からNまで1秒ごとに表示する。

### 実行

ワンライナーでfor loopを実行し、sleepを挟む。

```
$ for i in `seq 1 5`; do echo $i; sleep 1; done
1
2
3
4
5
```