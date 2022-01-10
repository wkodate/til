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
