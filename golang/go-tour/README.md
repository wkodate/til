A Tour of Go
===

https://go-tour-jp.appspot.com/list

### Basics

* 実行
```
$ go run xxx.go
```
* パッケージ名を指定し、関数名の先頭は大文字にする
```
fmt.Printf("Hello world")
```
* 最後にセミコロンはつけない
* `:=` で初期値を宣言
```
s := ""
```
* 関数は複数の戻り値を返すことができる
```
func swap(x, y string) (string, string) {
  return y, x
}
```
