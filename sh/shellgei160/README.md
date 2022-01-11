## 1. ファイル名の検索

files.txtから`.exe`の拡張子を持つファイルだけを抜き出す

```
$ grep "\.exe$" files.txt
test.exe
画面仕様書.xls.exe
```

## 2. 画像ファイルの一括変換

PNG形式の画像をconvertコマンドでJPEG形式に変換する

```
$ ls *.png | sed 's/\.png$//' | xargs -I FILE convert FILE.png FILE.jpg

```

xargsの-I(iのupper case)オプションで指定した文字列に受け取った値が入る。上記の場合は`FILE`。

## 3. ファイル名の一括変換

ファイル名の先頭に0をつけて4桁に揃える。本では7桁だがファイル数が多いので4桁にした

ファイルの作成

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

ファイル名を変更。awkで rename前後のファイル名を用意してxargsに渡す。

```
$ ls | sed 's/^\.\///' | awk '{print $1, sprintf("%04d", $1)}' | xargs -n2  mv

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
