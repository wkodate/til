GraalVM
========

* JVMベースとした多言語対応VM
* Graal+Thuffle
    * JITコンパイラにGraalを採用
    * Truffleの生成するASTを解釈

#### Graal
* JVM用の新しいJITコンパイラ。Java9以降で利用可能
* JIT(Just-In-Time)コンパイラ
    * 実行時コンパイラ。事前にコンパイルするのではなく実行される際にコンパイル
    * Javaのバイトコードを機械語に変換

#### Truffle
* プログラミング言語実装用のフレームワーク
* ソースコードを解析してAST(抽象構文木)を生成するためのAPIを提供

## Getting Started

#### JVMから他の言語を呼び出す

```
# バイトコード生成
$ clang -c -O1 -emit-llvm hello.c

# 実行
$ ./bin/lli hello.bc
Hello from GraalVM!
```

#### Native Imageを作る

## 参考
* https://gihyo.jp/news/report/01/oco2018/0002
* https://www.slideshare.net/nowokay/graalvm
* https://www.slideshare.net/tamrin69/getting-started-graalvm


