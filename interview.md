1001 Data Engineering Interview Questions
==

https://github.com/andkret/Cookbook/blob/master/InterviewQuestions.md

# SQL DBs

## What are windowing functions?

対象の行に対して区間ごとに集計する機能

集約関数では同じカラム値を持つ全行を1行に集計するが、ウィンドウ関数ではまとめられることはなく行が返される。分析関数やOLAP機能とも呼ばれる

### 例

city毎にpopulationを集約している

```
SELECT
  userid,
  city,
  population,
  sum(population) OVER(PARTITION BY city)
FROM
  user
```

|userid|city|population|sum(population)|
|---|---|---|---|
|1|tokyo|10|18|
|2|tokyo|8|18|
|3|osaka|7|7|
|4|nagoya|6|6|

## What is a stored procedure?

一連のSQLをまとめて関数のように呼び出せる仕組み

入出力を指定して呼べる。データベース側に保存しておくのでアプリケーション側の負荷を減らすことができる

### 例

```
CREATE PROCEDURE sample01()
SELECT NOW()
```

引数を渡すこともできる

```
CREATE PROCEDURE get_comment(IN id INT)
BEGIN
  SELECT comment from post
END//
```

作成したプロシージャを確認

```
SHOW CREATE PROCEDURE get_comment
```

## What are atomic attributes?

原子性と呼ばれ、トランザクションのタスクがすべて実行されるか、全く実行されないことを保証する性質

ACID特性のAtomicity

### 例

口座Aから口座Bに1万円を送金する例。以下の、1,2が両方行われるか、どちらも行われないかを保証する

1. 口座Aから1万円を引く
2. 口座Bに1万円を加える

## Explain ACID props of a database

トランザクション処理の信頼性を保証するための特性

以下の頭文字をとったもの

* Atomicity (原子性)
    * トランザクションのタスクがすべて実行されるか、全く実行されないかの1or0を保証
* Consistency (一貫性)
    * トランザクションにより実行結果が矛盾した状態にならない
* Isolation (独立性)
    * トランザクション中に行われる操作は他の動作の影響を受けない
* Durability (永続性)
    * 終了したトランザクションの結果は障害があっても失われない

## How to optimize queries?

インデックスを作成する、SELECT句で*やDISTINCTを使わない、先頭にワイルドカードを使わない、などの方法がある

* predicates(WHERE句, ORDER BY句, JOIN句)で利用されるすべてのカラムにインデックスを作成する
* predicatesで関数を使うのを避ける
    * `SELECT * FROM TABLE1 WHERE UPPER(COL1)='ABC'`
* predicatesの最初にワイルドカード(%)の利用を避ける。
    * `SELECT * FROM TABLE1 WHERE COL1 LIKE '%ABC'`
* `SELECT *` は使わず不必要なカラムを入れるのは避ける
* OUTER JOINの代わりにINNER JOINを使う
* DISTINCTやUNIONは必要な場合だけ使う。UNIONの代わりにUNION ALLを使う
* ORDER BYはソートするのでとても時間がかかる
* 参考
    * https://www.ibm.com/support/knowledgecenter/en/SSZLC2_9.0.0/com.ibm.commerce.developer.doc/refs/rsdperformanceworkspaces.htm#rsdperformanceworkspaces__t4

## What are the different types of JOIN (CROSS, INNER, OUTER)?

INNER JOINは2つのテーブルに行がある時だけ結合、OUTER JOINはどちらか一方のテーブルに行があれば結合、CROSS JOINはキーを指定せずにすべての行の組み合わせを返す

* INNER JOINは、CROSS JOINの結果から、ON句で与えられた結合条件を満たさない行を返す
* OUTER JOINは、どちらか一方のテーブルに存在していればその行を返す


* What is the difference between Clustered Index and Non-Clustered Index - with examples?

Clustered indexはテーブル毎に一つ、readで速い。Non-Clustered indexはテーブル毎jjugに複数、insertやupdateで速い

* Clustered index
    * クラスター化インデックス、CI
    * テーブルの行をキーでソートして格納
    * Clusterd indexが含まれていないテーブルのデータ行は、ヒープと呼ばれる順序付けられていない構造に格納
* Non-Clustered Index
    * 非クラスター化インデックス、NCI
    * インデックスで指定した列の値に対応するレコードへの参照がインデックスファイルに格納される
* 実テーブルに含まれる情報を見に行かないといけないときはClustered index, そうでないときはNon-Clustered index

# The Cloud

## What is serverless?

サーバ管理なしでアプリケーションを構築、実行する

* AWSではAWS Lambda、GCPdえはGoogle Cloud Functions, AzureではAzure Functionsが使われる。FaaS(Function as a Service)と呼ばれる
* 起動時間ではなく、プログラムが実行されている時間だけ課金される

## What is the difference between IaaS, PaaS and SaaS?

IaaSはOS以下を提供するサービス。PaaSはミドルウェア以下を提供するサービス。SaaSはアプリケーションをサービス化して提供するサービス

* IaaSはGmailとか、PaaSはHerokuとか、SaaSはEC2とか

## How do you move from the ingest layer to the Consumption layer? (In Serverless)

collectorからpushされたデータはスケールアップ可能なリアルタイムストレージ(Kinesis Streams, DynamoDB, Pub/Sub, Kafkaなど)に書き込まれる

## What is edge computing?

ユーザ側の近くで処理することでデータを最適化してクラウドの負荷を減らす

## What is the difference between cloud and edge and on-premise?

クラウドはリモートにコンピューティングリソースがあり、エッジコンピューティングはエンドユーザ側にコンピューティングリソースがある

# Linux

## What is crontab?

定期的に処理を実行するためのコマンド

# Big Data

## What are the 4 V's?

ビッグデータの4つのビジネス価値を表したVolume, Variety, Variation, Visibility

* Volume
    * データ量
    * データのサイズやスループットへの対応
* Variety
    * データの生成される速度
    * リアルタイム処理、大量のデータをいかにして速く処理するか
* Variation
    * データの多様性
    * 非構造化データ
* Veracity
    * データの正確さ
    * 意思決定で利用するためのデータの信頼性
* 5つ目のVとしてValueもある

## Which one is most important?

Veracity?

# Kafka

## What is a topic?

メッセージを保存する分散メッセージキュー

## How to ensure FIFO?

Topic内で分けられるpartitionのなかでメッセージの順番が保証される

## How do you know if all messages in a topic have been fully consumed?

kafka adminツールからトピックのオフセットの値を確認する

## What are brokers?

トピックを持つ複数台のマシンのクラスタ

## What are consumer groups?

トピックのメッセージを分散読み出しするための仕組み

## What is a producer?

メッセージをTopicに書き込むためのクライアント

# Coding

##  What is the difference between an object and a class?

クラスはフィールドとメソッドが定義されたテンプレート、オブジェクトはそれを実体化したもの

##  Explain immutability

変更不可能な状態のころ。オブジェクトの破壊的変更が起きないため想定外の問題が起きにくくなる、バグを抑制できる

* JavaのStringは元のオブジェクトを変えずに新しい文字列を返しているのでイミュータブル
* Arrayは呼び出し元のオブジェクトに変更があるのでミュータブル

##  What are AWS Lambda functions and why would you use them?

サーバレスを使うとサーバを準備しなくていいのでアプリケーションロジックに集中できる


##  Difference between library, framework and package

ライブラリはプログラムの集合、フレームワークはライブラリの集合、パッケージはライブラリやフレームワークとは異なりコードをまとめて配布する方法のこと

##  How to reverse a linked list

head Nodeのprevとnextを入れ替えるのをheadから開始してnextがnullになるまで続ける

##  Difference between args and kwargs

argsはtuple、kwargsはdictionary。**args, **kwargsで任意の数の引数を受け取りたいときに指定する。Pythonで利用される

##  Difference between OOP and functional programming

オブジェクト指向プログラミングはオブジェクト生成をベースとしたプログラミング技法。関数型プログラミングはデータに何らかの処理を加えていくプログラミング技法

* https://postd.cc/an-introduction-to-functional-programming/
* https://qiita.com/stkdev/items/5c021d4e5d54d56b927c
* 関数型言語ではリストをイテレートせずmapとreduceを使う

# NoSQL DBs

##  What is a key-value (rowstore) store?

NoSQLの一つで、すべてのデータに固有のキーを付けてKey-Value形式で保存するデータストア。分散データストアとして利用され、ディスクへの読み書き性能が高く、スケールが容易であり、分散処理に適している

* https://qiita.com/uenohara/items/23eb6ee1259f8a927445

##  What is a columnstore?

2つ以上の任意のキーでデータを格納できるデータストア。高いスケーラビリティを実現。Google Cloud BigTable、Apache HBase, Apache Cassandranなど

##  Diff between Row and col.store

行指向は、データの追加を効率的にお香なうことができる、インデックスを作成したデータを高速に検索できる。列指向は、最小限のデータだけを読み込むため、高速な読み書きができ、分散処理に優れている

* データ分析において事前にどのカラムを利用するかわかっていないため、全てのデータをお見こむ行指向だと大量のディスクIOが発生してしまう

##  What is a document store?

性能向上ではなくデータ処理の柔軟性を目的としたデータストア。JSONのようなスキーマレスデータを格納してクエリで実行できる。MongDBなど

##  Difference between Redshift and Snowflake

https://www.xplenty.com/blog/redshift-vs-snowflake/


# Hadoop

##  What file formats can you use in Hadoop?

テキストCSV、JSON, Avro, シーケンスファイル、RCファイル、ORCファイル、Parquet

https://www.quora.com/What-are-the-different-file-formats-in-Hadoop-and-explain-their-significance-in-detail

* テキストCSV
    * 人手で確認しやすい
    * ブロック圧縮に対応していないのてリードにコストがかかる
    * メタデータを保存できないため、ファイルの書かれたカラムの順番を記録しておく必要がある
* JSONレコード
    * メタデータを保持できる
    * CSVと同様、ブロック圧縮に非対応
    * ネイティブのシリアライザにいくつかバグがある。サードパーティーのシリアライザなら大丈夫
* Avroファイル
    * 広く使われる多目的ストレージフォーマット
    * データとメタデータに加えて、ファイルを読むためのスキーマ仕様が用意される
    * スキーマ変更をサポートしているのでカラムの変更に強い
    * Avroファイルは分割可能、ブロック圧縮、成熟している、Hadoopエコシステムツールのサポート
* シーケンスファイル
    * バイナリでCSV形式で保存
    * メタデータを保存しない
    * ブロック圧縮をサポート
    * 中間ストレージで使われることが多い
* RCファイル
    * Record Columnar ファイル。最初にHadoopで採用されたカラムナーファイル
    * 圧縮率やクエリパフォーマンスが良い
    * スキーマ変更サポートなし
    * クエリは速いが、カラムナーじゃないフォーマットに比べて書き込みにリソースをたくさん使うので遅い
* ORCファイル
    * Optimized RCファイル。HortonworksがHiveのパフォーマンスを最適化のために開発
    * RCより圧縮率がよく、クエリも速い。圧縮サイズはすべてのファイルフォーマットの中で一番小さい
    * スキーマ変更サポートなし
    * 2018年次店でCloudera ImparaはORCをサポートしていない
* Parquetファイル
    * スキーマ変更に制限がある。最後のカラムに追加する
    * ClouderaでサポートされていてCloudera Imparaに最適化されている
    * カラム名が小文字じゃないとHiveで読むときに問題が生じる
* 選び方
    * ファイルフォーマットの選び方は、書き込みパフォーマンス、特定のカラム読み込みパフォーマンス、全データ読み込みパフォーマンス、の3つを考慮する
    * 中間データを保存するならシーケンスファイル
    * クエリパフォーマンスが重要ならORC(Hortonworks/Hive)かParquet(Cloudera/Impara)
    * スキーマ変更の可能性があるならAvro
    * Hadoopからデータベースに書き込むならCSVファイル

##  What is the difference between a name and a datanode?

NameNodeはマスターノードでファイルシステムを管理するノード。DataNodeはスレーブノードでNameNodeの指示を受けて実際に保存するノード

##  What is HDFS?

Hadoop使われる分散ファイルシステム

* 特徴
    * データローカリティ
        * データを保持したサーバで処理を実行。ネットワーク転送が発生しない
    * レプリケーションとデータサイズ
        * デフォルトのレプリケーションは３
        * レプリケーションを増やすとその分データサイズも増える
    * ブロック単位でファイルを管理
        * ブロック単位で個々のノードに分散
    * データの蓄積がメイン
        * データのUPDATE,DELETEは考慮されていない
    * S3との比較
        * S3はデータの一元管理がしやすい、アクセス権限を設定しやすいなどのメリットがある
        * S3はデータローカリティがないのでオーバーヘッドが大きい

##  What is the purpose of YARN?

クラスタのリソースマネージャー。HDFSがストレージ部分、YARNが処理部分をハンドリングしている。ストレージ上のデータをどのようにどのくらいのリソースで処理するかを判断しデーブロックをアサインする

# Lambda Architecture

##  What is streaming and batching?

ストリーミングは低レイテンシの処理、バッチングは高スループットの処理

##  What is the upside of streaming vs batching?

ストリーミングは、正確でなくてもすぐに知ってインサイトを得ることができる。バッチは正確に大量のデータを処理することができる

##  What is the difference between lambda and kappa architecture?

ラムダアーキテクチャはバッチレイヤとスピードレイヤが並行で動いている。アーキテクチャを単純化したのがカッパアーキテクチャであり、スピードレイヤのみを残すアーキテクチャ

* カッパアーキテクチャでは、問題が起きた場合にはストリーム処理をやり直す。ストリーム処理のリソースを大量に消費する

##  Can you sync the batch and streaming layer and if yes how?

あとから処理したバッチレイヤの結果をスピード処理の結果に上書きする

# Python

##  Difference between list tuples and dictionary

リストは配列、tupleはイミュータブルな配列、dictionaryはマップ

# Data Warehouse & Data Lake

## What is a data lake?

構造化データや非構造化データを貯めることができる分散ストレージ

## What is a data warehouse?

処理済みの構造化データを貯めてビジネスに活かす

## Are there data lake warehouses?


## Two data lakes within single warehouse?


## What is a data mart?

データウェアハウスから特定の目的に沿って一部データを抽出

## What is a slow changing dimension (types)?

現行データと履歴データの両方を保存管理するディメンション。3種類のSCDが存在する

* 3種類のSCD
    * 上書き
    * 新バージョンのディメンションレコードの作成
    * 現行値フィールドの作成
* BIツールでは多次元モデルと呼ばれるデータモデルを使ってテーブルとカラムの集合を整理して名前をつけている
    * xx別のyy数(率)のデータについて、xxがディメンション、yyがメジャー
    * ディメンジョン日付や文字列などが使われる
    * メジャーは数値が扱われる

## What is a surrogate key and why use them?

# APIs (REST)

##  What does REST mean?
##  What is idempotency?
##  What are common REST API frameworks (Jersey and Spring)?

# Apache Spark

## What is an RDD?
## What is a dataframe?
## What is a dataset?
## How is a dataset typesafe?
## What is Parquet?
## What is Avro?
## Difference between Parquet and Avro
## Tumbling Windows vs. Sliding Windows
## Difference between batch and stream processing
## What are microbatches?

# MapReduce

## What is a use case of mapreduce?

サイト訪問者の集計、複数ソースからのデータ分析、顧客満足度の改善、顧客の洞察、ハードウェアコスト削減

## Write a pseudo code for wordcount

## What is a combiner?

Mapperの結果を中間処理するクラス

# Docker & Kubernetes

## What is a container?
## Difference between Docker Container and a Virtual PC
## What is the easiest way to learn kubernetes fast?

# Data Pipelines

## What is an example of a serverless pipeline?

## What is the difference between at most once vs at least once vs exactly once?

at most onceは多くても1回処理する(ロストあり重複なし)、at least onceは少なくとも1回処理する(ロストなし重複あり)、exactly onceは確実に1回処理する(ロストなし重複なし)

* exactly onceを実現するには両者の通信を保証するためのコーディネータの存在が必要
    * コーディネータ不在の場合にどうするかのコンセンサスが必要
    * コーディネータとのやりとりで時間がかかる

## What systems provide transactions?

## What is a ETL pipeline?

ETLパイプラインは、1つのシステムからデータを抽出して変換し、データベースやデータウェアハウスに読み込む一連のプロセスを指す。データパイプラインはもう少し一般的な用語で、システムから別のシステムにデータを移動し、その過程でデータを変換する可能性がある処理のことを指す

# Airflow

## What is a DAG (in context of airflow/luigi)?
## What are hooks/is a hook?
## What are operators?
## How to branch?

# DataVisualization

## What is a BI tool?

# Security/Privacy

## What is Kerberos?
## What is a firewall?
## What is GDPR?
## What is anonymization?

# Distributed Systems

## How clusters reach consensus (the answer was using consensus protocols like Paxos or Raft). Good I didnt have to explain paxos

## What is the cap theorem / explain it (What factors should be considered when choosing a DB?)

## How to choose right storage for different data consumers? It's always a tricky question

# Apache Flink

## What is Flink used for?

データストリームをステートフルに処理する分散処理エンジン

## Flink vs Spark?

両方ともストリーム処理アプリケーションが目的で利用される。アーキテクチャやコアコンポーネントが異なるので

# GitHub
## What are branches?
## What are commits?
## What's a pull request?

# Dev/Ops
## What is continuous integration?
## What is continuous deployment?
## Difference CI/CD

# Development / Agile
## What is Scrum?
## What is OKR?
## What is Jira and what is it used for?
