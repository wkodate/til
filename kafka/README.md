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
    *  存在しないオフセットを指定した場合に先頭から読むか、最新から読むか
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

# MirrorMaker

* 0.10.0以上のKafkaでは、メッセージにタイムスタンプが含まれているため、オフセットをタイムスタンプで指定することができる
* 複数のConsumerと1つのProducerを使用
* デフォルトで60秒ごとにProducerにまとめて送信する
* 設定はkafka-mirror-makerコマンドのオプションを参照
    * `consumer.config`
        * Consumerの設定ファイルを指定。つまりgroup.idを複数持つことはできない
    * `producer.config`
        * Producerの設定ファイルを指定
    * `num.streams`
        * consumerの数
    * `whitelist`
        * ミラーリング対象トピックの正規表現指定
* MirrorMakerのプロセスは送信先のデータセンターでことが推奨されている。ConsumeしたあとMirrorMakerイベントがProduceできないと、このイベントが失われるリスクがある
* 必ず遅延やメトリクスのモニタリングをしよう
* チューニング
    * Consumerスレッド数を増やす、イベントの解凍圧縮によるCPU使用率を確認
    * 別のConsumerグループを持つMirrorMakerに分離
    * Linuxのネットワーク設定
        * TCPバッファサイズを大きくする
        * 自動ウィンドウスケーリングを有効にする
        * TCPスロースタート時間を短縮する
    * スレッドダンプからMirrorMakerスレッドがポーリングや送信に時間を費やしていないかを確認する
        * ポーリングが多いならConsumer、送信が多いならProducerがボトルネック
    * 特定のプロセスがアイドル状態で別のプロセスがフルで動いていないか
    * Producerチューニング
        * `max.in.flight.requests.per.connection`
            * レスポンスを受信する前にProducerがサーバに送信するメッセージ数
        * `linger.ms`, `batch.size`
            * batch-size-avgとbatch-size-maxが`batch.size`よりも低い場合、Produerは部分的に空のバッチを送信しているため、`linger.ms`を調整して少し遅延を入れることでスループットが向上する。フルのバッチを送信してもメモリに余裕がある場合は、`batch.siz`1を大きくする
    * Consumerチューニング
        * `partition.assignment.strategy`をラウンドロビンにする
        * fetch-size-avgとfetch-size-maxが`fetch.max.bytes`に近い場合は、限界までデータを読み出している状態なので、`fetch.max.bytes`を増やして多くのデータを受信できるようにする
        * fetch-rateが高い場合は、Consumerがリクエストをたくさん送信している状態なので、`fetch.min.bytes`, `fetch.max.wait.ms` を増やして多くのデータを受信できるようにする
* uReplicator
    * Uberが開発
    * MirrorMakerでは、リバランスの遅延、トピックの追加が困難、といった課題があった
    * Apache Helixでトピックとパーティションを管理
    * Uberの規模で運用している会社は少ないため同じ問題に遭遇する会社は少ない、Helixを導入することで複雑になるデメリットがある
* Replicator
    * Confluenceが開発
    * MirrorMakerでは分散クラスタの設定、クラスタ管理に課題があった
* Kafka ConnectはMirrorMaker2.0
    * https://cwiki.apache.org/confluence/display/KAFKA/KIP-382%3A+MirrorMaker+2.0


# Kafka Connect

外部データストアとの接続がコネクタの設定だけで書ける。データストアのコネクタが存在しない場合は、Connect APIを使ってアプリケーションを書く。

Confluentが提供しているコネクタ一覧

https://www.confluent.io/product/connectors-repository/
