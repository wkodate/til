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

高可用性構成にするためには、Master-eligibleノードは3以上の奇数台構成にする。また、`discoverty.zen.minimum_master_nodes`の数をMaster-eligibleノード数の過半数に設定する

#### Dataノード

* データの格納
* クエリへの応答
* Luceneインデックスファイルのマージ

クエリ内容に対応する1つ以上のシャードを持つノードへルーティングを行い(scatterフェーズ)、各ノードからのレスポンスを集約して(gatherフェーズ)、まとめられた結果をクライアントへ応答する。

#### Ingestノード

データの変換や加工をするノード。Logstashで行う処理を実行できる

#### Coordinatingノード

クライアントからのリクエストのハンドリングのみを実行する

### シャード分割、レプリカ



クラスタ参加に関する設定

* `cluster.name`
* `discovery.zen.ping.unicast.hosts`
* `discovery.zen.minimum_master_nodes`

クラスタ名を設定し、


## 第3章 ドキュメント/インデックス/クエリの操作

## 第4章 Analyzer/Aggregation/スクリプティングによる高度なデータ分析

## 第5章 システム運用とクラスタの管理

## 第6章 Elastic Stack インテグレーション
