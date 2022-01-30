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
