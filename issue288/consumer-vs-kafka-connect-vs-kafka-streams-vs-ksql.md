Consumer vs Kafka Connect vs Kafka Streams vs KSQL !
==

https://medium.com/@stephane.maarek/the-kafka-api-battle-producer-vs-consumer-vs-kafka-connect-vs-kafka-streams-vs-ksql-ef584274c1e

Kafka API, Kafka Connect, Kafka Streams, KSQLの比較記事

## Kafka Producer API

* クリックのストリーム、ログ、IoTなど直接データをProduceするアプリケーション
* 拡張性が高い
* エンジニアが多くのロジックを書く必要がある、Producer再開時のオフセット追跡が難しい、ETL処理の負荷分散が難しい

## Kafka Connect Source API

* CDC, Postgres, MongoDB, Twitter, REST APIなど、データストアとKafkaを繋ぐアプリケーション
* Producerタスクを分散できる、producerの再開が簡単
* 利用できるsource connectorがなければ自分で書く必要がある

## Kafka Stream API / KSQL

* KafkaからConsumeしてKafkaにProduceして戻したいアプリケーション。ストリーム処理
* SQLライクにかけるのであればKSQL, 複雑なロジックなのであればKafka Streams
* まだ一部複雑なことはできない

## Kafka Consumer API

* メールを送るなど、Streamを読んでリアルタイムにアクションをする

## Kafka Connect Sink API

* Kafka to S3, Kafka to HDFS, Kafka to PstgreSQL, Kafka to MongoDBなど、Streamを読んでデータストアに保存する
* 外部のデータソースに対して複雑なロジックを書く場合は、Kafka Consumer APIよりもこちらの方が優れている
* Connectorがないときは自分で書く必要がある
