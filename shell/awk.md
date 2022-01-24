awk
===

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

`NF`(Number of Fields)で列数を取得できる

```
$ cat access.log | awk -F: '{print $(NF-2)}' | awk '$1<12{print "Morning"}$1>=12{print "Evening"}' | sort | uniq -c
   3 Evening
   2 Morning
```
