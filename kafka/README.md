Kafka
===

# 概要

# Producer

# Consumer

## オフセットコミット

オフセットをコミットする前にリバランスが実行されると重複が発生する。

コミットの自動/手動は `enable.auto.commit` で設定することができる。

自動の場合は `auto.commit.interval.ms` でコミット間隔を指定する。

手動の場合はsync or asyncに合わせてCommitAPIを選択する。

## 信頼性のための設定

* `consumer.id`
  * consumer groupのID
* `auto.offset.reset`
  * 存在しないオフセットを指定した場合に先頭から読むか、最新から読むか
* `enable.auto.offset`
  * 自動でオフセットをコミットさせるか、コード内で手動でコミットするか
* `auto.commit.interval.ms`
  * 自動オフセットコミットのときのインターバル

# Broker

## コントローラ

コントローラはパーティションリーダーの選出をする役割を持つ。

クラスタ内で最初に起動するBrokerがZooKeeperの/controllerにノードを作ってリーダーになる。

これでクラスタにコントローラが1つだけ存在する事を保証する。

コントローラになってるBrokerが死ぬと、クラスタ内の他のBrokerに通知され、それぞれがZooKeeperにcontrollerノードを作ろうとする。

最初にノードを作成したBrokerが新たにコントローラとなる.

コントローラはBrokerのクラスタ追加削除を検知すると新しいリーダーを選出して既存のBrokerに通知する。

新しいリーダーはレプリカリストの次のレプリカがリーダーとして選ばれる。

通知を受けたBrokerのレプリカは、リーダーからメッセージを複製し始める。

## レプリケーション

レプリケーション設定

* `replication.factor`
  * レプリケーション数。3以上推奨
* `unclean.leader.election.enable`
  * in-syncでないレプリカがリーダーになることを許容するかどうか
* `min.insync.replicas`
  * 書き込みに必要なin-syncレプリカの最小数
  
## コンパクション

保存期間よりも古いメッセージを削除するdeleteポリシーと、トピックの各キーの最新の値のみを格納するcompactポリシーがある。

# Kafka Connect

外部データストアとの接続がコネクタの設定だけで書ける。データストアのコネクタが存在しない場合は、Connect APIを使ってアプリケーションを書く。

Confluentが提供しているコネクタ一覧

https://www.confluent.io/product/connectors-repository/
