Designing Data-Intensive Applications - Part 1. Foundations of Data Systems
===

第1部: データ指向アプリケーションの基本的な概念

* 1章: 信頼性、スケーラビリティ、メンテナンス性
* 2章: データモデルとクエリ言語の比較
* 3章: ストレージエンジン
* 4章: データエンコーディング(シリアライゼーション)

# Chapter 1. Reliable, Scalable and Maintainable Applications

信頼性(Reliabile)

* 問題が生じたときに正しく動作し続けること

スケーラビリティ(Scalable)

* 負荷の増大に対してシステムが対応できる能力

メンテナンス性(Maintainable)

* 運用性(Operability): 運用しやすくする
* 単純性(Simplicity): 抽象化などによって複雑さを取り除いてシステムを理解しやすくする
* 進化性(Evalvability): システムを変更し続けるための拡張性

## 用語

* レジリエント(resilient)
  * フォールトトレラント。耐障害性
* ヘッドオブラインブロッキング(head-of-line blocking)
  * 少数の遅いリクエストによって他のリクエスト全体が待たされる状態
* シェアードナッシングアーキテクチャ(shared nothing architecture: SN)
  * 複数のマシンに負荷を分散させる構成
* エラスティック(elastic)
  * 負荷の増大を検知して自動的にコンピューティングリソースを追加できるシステム

# Chapter 2. Data Models and Query Languages

アプリケーション開発者側の観点でデータを扱う方法

* データモデル: データベースにデータを渡すフォーマット
* クエリ言語: データを取り出す仕組み

## Data model

| データモデル | 特徴|
|-|-|
| リレーショナルデータベース| データの結合、多対一や多対多のサポート |
| ドキュメントデータベース| ドキュメント間の関係がそれほどないユースケースで使われる |
| グラフデータベース| データの関係の多くが多対多の場合に使われる |


すべてのデータが入れ子になった階層モデルは多対多の扱いの課題があり、これに対応するためにリレーショナルデータベースが作られた

ドキュメントデータベースの特徴

* 階層モデルへの回帰
* スキーマの柔軟性(スキーマオンリード)
* ローカリティによるパフォーマンスの向上

## Query language

宣言的クエリ言語

* SQLなどが代表的な例
* 以下を記述する
  * データのパターン(結果が満たすべき条件)
  * データの変換
* 命令的コードと違ってどのように達成するかは指定しない。これはDBのクエリオプティマイザが判断
* データベースエンジンの実装の詳細を隠蔽
* 並列実行に向いている

## 用語

* ポリグロットパーシステンス(polyglot persistence)
  * 複数のデータベースを併用する構成
* データローカリティ
  * データがある場所で処理をするという考え方。全ての関連情報を一度のクエリで取得できる。

# Chapter 3. Storage and Retrieval

データベース側の観点でデータを扱う方法

* データの保存方法
* リクエストされたデータの抽出方法

## Storage engine

データベースから特定のキーを効率的に見つけるためにはインデックスが必要

インデックスはreadのクエリを高速してくれるが、データ書き込みのたびにインデックスを更新するため書き込みを低速にする

### LSM tree (Log-Structured Merge-Tree)

* ソート済みファイルのマージとコンパクションという原理を基盤としたインデックス構造
  * log-structuredストレージ。シーケンシャルに追記が行われるインデックス構造
  * そのためトランザクション処理のようなwriteが多い用途で使われる
  * マージ
    * 同じキーをマージして最新の値にする
  * コンパクション
    * ログ中で重複しているキーを捨てて最新の値だけを残す処理
  * 可変サイズのセグメントに分割
* Write: インメモリのmemtableに書き込む
  * 閾値を超えたらSSTableのファイルとしてディスクに書き込む
  * シーケンシャル書き込みなので速い
  * memtable
    * インメモリのバランスドツリーデータ構造。ツリー構造でソートされたkey-valueが管理されている
  * SSTable (Sorted String Table)
    * キーでソートされたディスク上のセグメントファイルフォーマット
* Read: インメモリのmemtableからキーを探して、なければSSTableの新しいセグメントから古いセグメントまでを探していく
  * ブルームフィルターでキーの存在有無をチェックすることで、すべてのSSTableを調べる必要がないのでディスクreadが減る
* バックグラウンドでマージとコンパクションが実行される

### B tree

* もっとも広く使われているインデックス構造
  * 固定サイズのブロックあるいはページに分割
  * 追記ではなくインプレースで更新
  * 読み取りが高速
* Write: ページの上書き
  * 新しいキーを書く容量がない場合は2つのページに分割
* Read: ルートからルックアップして最終的にリーフページにたどり着く
* WAL(Write ahead log)にBツリーの変更内容を追記してから、ツリーそのもののページを更新することで、クラッシュ時にリカバリができる

## OLTP vs OLAP

OLTP(Online transaction processing): オンライントランザクション処理

OLAP(Online analytic processing): オンライン分析処理

ユーザ --> OLTPシステム --> OLAPシステム <-- ビジネスアナリスト

特性比較

| | Read | Write | データの内容 | データサイズ |
|-|-|-|-|-|
|OLTP|少数のレコードをキー毎にフェッチ|ランダムアクセス、低レイテンシ|最新のログ|GB~TB|
|OLAP|大量のレコードを集計|ETL, イベントストリーム|イベント履歴|TB~PB|

分析のために直接OLTPデータベース上で実行することを避けるためにデータウェアハウスを用意する


## 列指向ストレージ

分析のワークロードでは列指向ストレージが使われる

大量にシーケンシャルにスキャンしなければならない場合に、列でまとめて保存しておく

ディスクからのreadを最小限にでき、データの圧縮効率も良い

## 用語

* マテリアライズドビュー
  * データウェアハウスにおいて、集約処理をキャッシュして参照できるビュー

# Chapter 4. Encoding and Evolution

ファイルにデータを書いたり、ネットワーク経由でデータを送信するような、メモリ共有していない他のプロセスに送信する場合、バイト列にエンコードしなければならない

インメモリ->バイト列をエンコーディング(シリアライゼーション)、バイト列->インメモリをデコーディング(デシリアライゼーション)と呼ぶ

## データエンコードのフォーマット

スキーマのメリット
* バイナリJSONよりもはるかにコンパクト
* でコードにはスキーマが必要になるため、常に最新の状態になっていることが保証される
* スキーマのデータベースを管理することで、互換性をデプロイ前にチェックできる
* スキーマからのコード生成によって、コンパイル時に肩チェックができる

### 言語固有フォーマット

多くのプログラミング言語にエンコーディングライブラリが用意されている。

Javaなら`java.io.Serializable`, Pythonなら`pickle`

特徴

* これらは最低限のコードでSerdeできるので便利
* 他の言語でデータを読むのが難しいため、一時的な目的の場合を除けば、言語固有のエンコーディングするのはよくない

### JSON, XML、バイナリエンコーヂング

* テキスト形式であり、人間が読めるフォーマット
* XMLは数字と文字列の区別がない
* JSONは整数と浮動小数点を区別しない
* バイナリ文字列がサポートされていない
* スキーマを持たない
* MesagePackはJSONのバイナリエンコーディング. 圧縮効率はよくない

### Thrift, Protocol Buffer

* Protocol BufferはGoogle, ThriftはFacebookで開発されたバイナリエンコーディングライブラリ
* スキーマ定義からクラスを生成するツールを使って、スキーマのエンコードやでコードができる
* フィールドタグを使って前方互換性、後方互換性を保つ

Protocol Bufferのスキーマ定義例
```
message Person {
  required string user_name = 1;
  optional int64 favorite_number = 2;
  required string interests = 3;
}
```

### Avro

* Hadoopのサブプロジェクトとして作られた
* フィールドタグがないのでコンパクト
* WriterとReaderのスキーマは同一である必要がなく、互換性があれば良い
* 互換性を保つため、追加や削除ができるのはデフォルト値を持っているフィールドだけ
* 動的に生成されたスキーマと相性が良い

Avroのスキーマ定義例
```
{
  "type": "record",
  "name": "Person",
  "fields": [
    {"name": "userName", "type": "string"},
    {"name": "favoriteNumber", "type": ["null", "long"], "default": null},
    {"name": "interests", "type": ["null", "array"], "items": "string"}
  ]
}
```

## データフローの形態

データベースでは、データベースへの書き込みを行うプロセスがデータをエンコードし、読み取りを行うプロセスがそのデータをデコードする。

REST, RPCでは、クライアントがリクエストをエンコードし、サーバはそのリクエストをデコードしてレスポンスをエンコードする。最後にクライアントがレスポンスをデコードする

非同期のメッセージパッシングでは、ノードはお互いにメッセージを送信することによって通信し、送信側がメッセージをエンコードし、受信側がそのメッセージをデコードする
