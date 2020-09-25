Elasticsearch実践ガイド

## 第1章 Elasticsearchとは


Elasticsearchは全文検索をするソフトウェア

### 全文検索システム

検索システムは、Searcher, Indexer, Crawlerで構成される

| 構成要素 | 意味 |
| -- | -- |
| Searcher | Indexerが構成したインデックスに基づいて、検索クエリの機能を提供 |
| Indexer | 対象ドキュメントから単語を抽出して、インデックスのデータベースを構成する|
| Crawler | 定期的に検索対象を巡回してドキュメントを収集し、インデックスを更新する|

Elasticsearchは検索サーバとしての役割を持ち、内部の検索ライブラリ(Indexer, Searcher)にはApache Luceneを利用している。

Elasticsearchはこれに加えて、API提供、クラスタ管理など、検索サーバとしての機能を持っている

### Elasticsearchの特徴

* 分散配置による高速化と高可用性の実現
* シンプルなREST APIによるアクセス
* JSONに対応した柔軟性の高いドキュメント指向データベース
* ログ収集、可視化などの多様な関連ソフトウェアとの連携

### Elastic Stack

| Elastic Stack | 概要 |
| -- | -- |
| Kibana | Elasticsearchのデータを可視化 |
| Logstach | ログを収集・加工・転送するためのパイプラインツール |
| Beats | メトリクス収集、転送のための軽量データシッパー |


## 第2章 Elasticsearchの基礎

### 用語

##### 論理的な概念

| 用語 | 意味 |
| -- | -- |
| ドキュメント | レコード |
| フィールド | カラム |
| インデックス | データベース |
| マッピング | テーブル定義 |

##### 物理的な概念

| 用語 | 意味 |
| -- | -- |
| ノード | Elasticsearchサーバ |
| クラスタ | ノードのグループ |
| シャード | 分割単位 |
| レプリカ | 可用性を高めるための複製 |

### システム構成

ノード種別

* Masterノード
* Dataノード
* Ingestノード
* Coordinatingノード

#### Masterノード

* ノードの参加と離脱の管理
* クラスタメタデータの管理
* シャードの割当と再配置

Masterノードはクラスタ内で常に1台存在する。

Masterノードがダウンしたときに昇格する候補ノードをMaster-eligibleノードと呼ぶ。デフォルトはすべてのノード

スプリットブレイン(Elasticsearchクラスタが3つに分断されてしまう)を防ぐためにMaster-eligibleノードは3以上の奇数台構成にする。

`discoverty.zen.minimum_master_nodes`の数をMaster-eligibleノード数の過半数に設定する


#### Dataノード

* データの格納
* クエリへの応答
* Luceneインデックスファイルのマージ

クエリ内容に対応する1つ以上のシャードを持つノードへルーティングを行い(scatterフェーズ)、各ノードからのレスポンスを集約して(gatherフェーズ)、まとめられた結果をクライアントへ応答する。

#### Ingestノード

データの変換や加工をするノード。Logstashで行う処理を実行できる

#### Coordinatingノード

クライアントからのリクエストのハンドリングのみを実行する

#### ディスカバリ

クラスタへのノード参加、Masterノード選定の仕組み

Zen discoveryというメカニズムが使われている。

これを利用するためには、クラスタ名とmasterノードを設定する

```
cluster.name: my-cluster
discovery.zen.ping.unicast.hosts: ["master1", "master2", "master3"]
discovery.zen.minimum_master_nodes: 2
```

### シャード分割、レプリカ

シャード数はインデックス作成時に決める必要がある。レプリカ数は作成後も変更可能

#### シャード数の検討

* 拡張可能なノード数に合わせてシャード数を設定する
* インデックスに格納するデータサイズが十分小さい場合(20-30GB程度)はシャード数を1に設定する

#### レプリカ数の検討

* 検索負荷が高い場合にはレプリカ数も増やす
* バルクインデックスなどバッチ処理の際はレプリカ数を一時的に0にする

### Elasticsearchの基本設定

Elasticsearchの設定ファイル

| ファイル名 | 用途 |
| -- | -- |
| `elasticsearch.yml` | Elasticsearchサーバのデフォルト設定 |
| `jvm.options` | JVMオプションの設定 |
| `log4j2.properties` | Elasticsearchサーバのデフォルト設定 |

#### `elasticsearch.yml`

```
$ cat /usr/local/etc/elasticsearch/elasticsearch.yml.default
# ======================== Elasticsearch Configuration =========================
#
# NOTE: Elasticsearch comes with reasonable defaults for most settings.
#       Before you set out to tweak and tune the configuration, make sure you
#       understand what are you trying to accomplish and the consequences.
#
# The primary way of configuring a node is via this file. This template lists
# the most important settings you may want to configure for a production cluster.
#
# Please consult the documentation for further information on configuration options:
# https://www.elastic.co/guide/en/elasticsearch/reference/index.html
#
# ---------------------------------- Cluster -----------------------------------
#
# Use a descriptive name for your cluster:
#
cluster.name: elasticsearch_wkodate
#
# ------------------------------------ Node ------------------------------------
#
# Use a descriptive name for the node:
#
#node.name: node-1
#
# Add custom attributes to the node:
#
#node.attr.rack: r1
#
# ----------------------------------- Paths ------------------------------------
#
# Path to directory where to store the data (separate multiple locations by comma):
#
path.data: /usr/local/var/lib/elasticsearch/
#
# Path to log files:
#
path.logs: /usr/local/var/log/elasticsearch/
#
# ----------------------------------- Memory -----------------------------------
#
# Lock the memory on startup:
#
#bootstrap.memory_lock: true
#
# Make sure that the heap size is set to about half the memory available
# on the system and that the owner of the process is allowed to use this
# limit.
#
# Elasticsearch performs poorly when the system is swapping the memory.
#
# ---------------------------------- Network -----------------------------------
#
# Set the bind address to a specific IP (IPv4 or IPv6):
#
#network.host: 192.168.0.1
#
# Set a custom port for HTTP:
#
#http.port: 9200
#
# For more information, consult the network module documentation.
#
# --------------------------------- Discovery ----------------------------------
#
# Pass an initial list of hosts to perform discovery when this node is started:
# The default list of hosts is ["127.0.0.1", "[::1]"]
#
#discovery.seed_hosts: ["host1", "host2"]
#
# Bootstrap the cluster using an initial set of master-eligible nodes:
#
#cluster.initial_master_nodes: ["node-1", "node-2"]
#
# For more information, consult the discovery and cluster formation module documentation.
#
# ---------------------------------- Gateway -----------------------------------
#
# Block initial recovery after a full cluster restart until N nodes are started:
#
#gateway.recover_after_nodes: 3
#
# For more information, consult the gateway module documentation.
#
# ---------------------------------- Various -----------------------------------
#
# Require explicit names when deleting indices:
#
#action.destructive_requires_name: true
```

## 第3章 ドキュメント/インデックス/クエリの操作

### CRUD操作

#### Create

インデックスやドキュメントタイプがない場合には自動的に作成される

```
$ curl -XPOST 'http://localhost:9200/my_index/my_type/' \
-H 'Content-Type: application/json' \
-d '{"name":"Taro", "age":30, "city":"Tokyo"}'
```

#### Read

```
$ curl -XGET 'http://localhost:9200/my_index/my_type/1'

{
  "_index" : "my_index",
  "_type" : "my_type",
  "_id" : "1",
  "_version" : 1,
  "_seq_no" : 0,
  "_primary_term" : 1,
  "found" : true,
  "_source" : {
    "name" : "Taro",
    "age" : 30,
    "city" : "Tokyo"
  }
}
```

検索

システム全体、my_index内、my_type内から検索できる

```
$ curl -XGET 'http://localhost:9200/my_index/my_type/_search?pretty' \
-H 'Content-Type: application/json' \
-d '{"query": {"match":{"city":"Osaka"}} }'

{
  "took" : 399,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 1,
      "relation" : "eq"
    },
    "max_score" : 0.6931471,
    "hits" : [
      {
        "_index" : "my_index",
        "_type" : "my_type",
        "_id" : "ml_Vw3QBEtGUi7eQ5KGR",
        "_score" : 0.6931471,
        "_source" : {
          "name" : "Hanako",
          "age" : 25,
          "city" : "Osaka"
        }
      }
    ]
  }
}
```

#### Update

```
$ curl -XPUT 'http://localhost:9200/my_index/my_type/1' \
-H 'Content-Type: application/json' \
-d '{"name":"Taro", "age":80, "city":"Tokyo"}'

$ curl -XGET 'http://localhost:9200/my_index/my_type/1?pretty'

{
  "_index" : "my_index",
  "_type" : "my_type",
  "_id" : "1",
  "_version" : 2,
  "_seq_no" : 2,
  "_primary_term" : 1,
  "found" : true,
  "_source" : {
    "name" : "Taro",
    "age" : 80,
    "city" : "Tokyo"
  }
}
```

#### Delete

```
$ curl -XDELETE 'http://localhost:9200/my_index/my_type/1'

$ curl -XGET 'http://localhost:9200/my_index/my_type/1?pretty'

{
  "_index" : "my_index",
  "_type" : "my_type",
  "_id" : "1",
  "found" : false
}
```

### インデックスとドキュメントタイプの管理

デフォルト値と異なるレプリカ数やシャード数を持つインデックスを作成したい場合は、明示的にインデックスを作成する

Elasticsearchの自動推測ではなく明示的にデータ型指定したい場合は、明示的にドキュメントタイプを作成する

#### インデックステンプレート

日時で作成されるインデックスすべてに共通のマッピングを定義して動的に自動生成するための定義ファイル

インデックス名の条件(`index_patterns`)とシャード数などの設定(`settings`)、マッピング(`mappings`)を定義する

## 第4章 Analyzer/Aggregation/スクリプティングによる高度なデータ分析

## 第5章 システム運用とクラスタの管理

## 第6章 Elastic Stack インテグレーション
