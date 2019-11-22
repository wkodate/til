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

|userid |city |population |sum(population |
|---|---|---|
|1 |tokyo  |10 |18 |
|2 |tokyo  |8 |18 |
|3 |osaka  |7 |7 |
|4 |nagoya  |6 |6 |

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

## How to ensure FIFO?

## How do you know if all messages in a topic have been fully consumed?

## What are brokers?

## What are consumer groups?

## What is a producer?

# Coding

##  What is the difference between an object and a class?
##  Explain immutability
##  What are AWS Lambda functions and why would you use them?
##  Difference between library, framework and package
##  How to reverse a linked list
##  Difference between args and kwargs
##  Difference between OOP and functional programming

# NoSQL DBs

##  What is a key-value (rowstore) store?
##  What is a columnstore?
##  Diff between Row and col.store
##  What is a document store?
##  Difference between Redshift and Snowflake

# Hadoop

##  What file formats can you use in Hadoop?
##  What is the difference between a name and a datanode?
##  What is HDFS?
##  What is the purpose of YARN?

# Lambda Architecture

##  What is streaming and batching?
##  What is the upside of streaming vs batching?
##  What is the difference between lambda and kappa architecture?
##  Can you sync the batch and streaming layer and if yes how?

# Python

##  Difference between list tuples and dictionary

# Data Warehouse & Data Lake

## What is a data lake?
## What is a data warehouse?
## Are there data lake warehouses?
## Two data lakes within single warehouse?
## What is a data mart?
## What is a slow changing dimension (types)?
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
## Write a pseudo code for wordcount
## What is a combiner?

# Docker & Kubernetes

## What is a container?
## Difference between Docker Container and a Virtual PC
## What is the easiest way to learn kubernetes fast?

# Data Pipelines

## What is an example of a serverless pipeline?
## What is the difference between at most once vs at least once vs exactly once?
## What systems provide transactions?
## What is a ETL pipeline?

# Airflow

## What is a DAG (in context of airflow/luigi)?
## What are hooks/is a hook?
## What are operators?
## How to branch?

# DataVisualization

# What is a BI tool?

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
## Flink vs Spark?

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
