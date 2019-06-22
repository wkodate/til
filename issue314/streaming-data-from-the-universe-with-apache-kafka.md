Streaming Data from the Universe with Apache Kafka
===

* Apache Kafkaの天文学でのユースケース

https://www.confluent.io/blog/streaming-data-from-the-universe-with-apache-kafka

![C28CA757-A921-41DB-BAAF-BF3EAA683A63](https://user-images.githubusercontent.com/3250247/59964925-7849e680-9542-11e9-8a8e-35047187c827.png)



* 画像を撮って明るさや位置を比較することによってオブジェクトをリアルタイムに検出する
* 物体検出のアラートを配信するシステムにKafkaが利用されている
* データはAvroでシリアライズ、天文学で使われるPythonクライアントを利用
* 45秒ごとに画像を撮影、シリアライズしてがKafka Brokerにインプット、2週間保持
* 永続用と分析用の環境にMirrorMakerで転送
* サイエンティストに合わせてフィルタリングしたtopicは16partitionsに分散
* 一晩で60万-120万のアラートを生成、1アラートのサイズは60KB、システム全体で一晩70GB、producerから菜園ティスのconsumerまで約4秒

