Issue289
==

# Apache Kafka Supports 200K Partitions Per Cluster

https://blogs.apache.org/kafka/entry/apache-kafka-supports-more-partitions

Kafka1.1.0からpartition数をこれまで以上に増やすことができるようになった

* Kafka1.1.0で単一クラスタのpartition数を多く増やすことができるようになった
* brokerがfailしたときのcontrolled shutdownを改善しパフォーマンスが向上、レイテンシが低下。これによってKafkaクラスタで多くのpartition作成が可能になった
* controlled shutdownの流れ
    1. SIG_TERMシグナルがbrokerに送られる
    2. このbrokerからcontrollerを持つbrokerにshut downのリクエストを送信
    3. controllerがZooKeeperにleader変更の情報を送信
    4. controllerがpartitionを持つ全てのbrokerに新しいleaderの情報を送信
    5. controllerがダウンしたbrokerに成功の情報を送信
* 改善点
    * 3: ZooKeeperへの書き込みを非同期に変更
    * 4: 各partition毎のへのリクエストを1つにまとめる
* 各brokerに4,000partition、単一クラスタに200,000partitionまで作成することをオススメする。将来的には、単一クラスタで数百万のpartitionをサポートしたい
* 詳細のJIRAチケット
    * [KAFKA-5642](https://issues.apache.org/jira/browse/KAFKA-5642)
    * [KAFKA-5027](https://issues.apache.org/jira/browse/KAFKA-5027)
