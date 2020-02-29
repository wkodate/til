Issue330
===

# Processing guarantees in Kafka

https://medium.com/@andy.bryant/processing-guarantees-in-kafka-12dd2e30be0e

Kafkaのメッセージ処理の保証について

* どのように全てのメッセージを処理することを保証するか
* どのようにメッセージの重複を避けるか

メッセージ処理の保証

* No guarantee
    * Consumerはメッセージを一度処理する
    * 重複もあれば一度も処理しないこともある
* At most once
    * Consumerはメッセージを多くても一度処理する
* At least once
    * Consumerは同じメッセージを一度以上処理する
* Effectively once
    * できるかぎり正確に一回メッセージを処理する

処理が失敗するシナリオ

* Producerの失敗
* Consumerのpublishの失敗
* メッセージングシステムの失敗
* Consumer処理の失敗

## No guarantee

`enable.auto.commit`をtrueに設定すればconsumerは非同期で結果をデータベースに書き込む。`auto.commit.interval.ms`でコミット間隔を指定できる

メッセージをデータベースにコミットした後、オフセット更新前にアプリケーションがクラッシュした場合、処理は再実行され、データベースに二度書き込まれる

データベースに保存される前にコミットした後、アプリケーションがクラッシュした場合、処理は再実行されずにデータはロストする

## At most once

オフセットを更新してからメッセージを書き込む

Producerはメッセージを一度だけ送り、その後のBrokerからのレスポンスは無視される

Producerはデータソースからデータを読み込みProgressを書き込んだ後、Kafkaにメッセージを書き込む。もしProducerがProgressを書き込んだ後、メッセージを書き込む前にクラッシュした場合、メッセージはKafkaに書き込まれない

Consumerは`enable.auto.commit` をfalseにセットしてデータベースに書き込む前にKafkaにオフセットを書き込む。もし、オフセットを書き込んだ後にデータベースへの書き込みに失敗した場合、データはロストする

## At least once

メッセージを書き込んでからオフセットを更新する

ProducerがバッチメッセージをKafkaに送るとき、ackがロストした場合にメッセージが二回送信される

Producerはデータソースからデータを読み込みKafkaに書き込んだ後、オフセットを書き込む。もしProducerがメッセージを書き込んだ後、Progressを書き込む前にクラッシュした場合、メッセージはKafkaに二度書き込まれる

Consumerは`enable.auto.commit` をfalseにセットしてデータベースに書き込んだ後、Kafkaにオフセットを書き込む。もし、データベースに書き込んだ後にオフセットへの書き込みに失敗した場合、データは二度書き込まれる

## Effectively once

データとオフセットの両方をアトミックに書き込む

Idempotent writes と Transactionsの特徴がある

### Idempotent writes

`enable.idempotence`をtrueにして冪等性を保証する書き込みに設定

Brokerからproducer id(pid)をリクエストする。pidはKafkaクラスタがProducerを一意に決めるID。Producerがバッチと一緒にpidとバッチ毎のシーケンス番号を送る。

シーケンス番号は同じProducerによって送られたバッチ毎にインクリメントされる。
もしシーケンス番号が既にコミットされているじょうたいでバッチを受け取ったら、バッチはリトライとして扱われ無視される

### Transactions

`enable.idempotence`をtrueにするとtransactional id(`transactional.id=my-tx-id`)が提供される。

Producerは`initTransactions`によってKafkaクラスタに
登録される必要がある。同じtransaction idで再接続するとき、Producerは同じpidとエポック番号でアサインされる

## Effectively once production of data to Kafka

データの書き込みにはプログレスの保存とデータのpublishが必要になる

## Effectively once consuming of data from Kafka

外部システムへの更新とオフセットの保存を1つのトランザクションとしてアトミックに書き込む
