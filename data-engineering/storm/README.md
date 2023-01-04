Storm
===

# コンセプト

## Topology

リアルタイム処理のアプリケーションロジックを記述するDAG。SpoutとBoltのグラフで構成される

MapReduceと違ってTopologyは永続的に実行される

## Stream

連続したTuple

TupleはStormで処理されるデータの単位

OutputFieldsDeclarerメソッドでストリーム名が定義される。デフォルトはdefault

## Spout

Streamのデータソースになるコンポーネント

外部ソースからデータ読み込む使われ方が一般的

Stormで処理に失敗したときにtupleを再実行する

declareStreamで複数のStreamを定義できる

`nextTuple` メソッドは新しいTupleをTopologyにemitする

`ack`, `fail`メソッドでStormはTupleのemitが成功したか失敗したかを返す

## Bolt

Spoutから受け取ったデータに対して変換処理をするコンポーネント

具体的な処理は、フィルタリング、集約、ジョイン、DB接続など

`execute` メソッドで入力Tupleを受け取りTuple毎の処理を記述する。`ack` メソッド

`IRichBolt` はBoltの一般的なインターフェース

`IBasicBolt` はフィルタリングや新婦な関数を定義するための便利なインターフェース。ack送信を自動でやってくれる

## Stream grouping

Streamを同じBoltにグルーピングする

よくあるグルーピング

* Shuffle grouping
    * ランダムにグルーピング
* Fields grouping
    * フィールド名を指定してグルーピング。同じ値は同じTaskで実行される
* Local or shuffle grouping
    * 同じWorkerの中に次のタスクがあればその中でシャッフリングされる。そうでなければ普通のShuffle groupingになる

## Reliability

Stormは全てのtupleが最後まで処理されることを保証する。そのためにTupleのツリーをトラッキングしている

## Task

Taskはスレッドの実行単位。

`setSpout`や`setBolt`で実行並列数を指定する

## Worker

WorkerプロセスはJVMで実行される単位

Topologyの並列数が300なら50Workerが配置されて各Workerは6Taskを実行する


# Performance Tuning

https://storm.apache.org/releases/2.1.0/Performance.html

パフォーマンスチューニングのキーとなるのは、レイテンシ、スループット、リソース

## Buffer Size

データを受信するときにコンポーネントが持つキューのサイズ

`topology.executor.receive.buffer.size` executorがもつメッセージキューのサイズ
`topology.transfer.buffer.size` データ転送用のキューサイズ

## Batch Size

ProducerがメッセージのバッチをConsumer queueに書き込むことができるサイズ

`topology.producer.batch.size` 

`topology.transfer.batch.size`

低レイテンシ重視であれば、batch sizeは1にする。つまりバッチ書き込みをしない。この場合、高トラフィックであればスループットに影響が出る

高スループット重視であれば、batch sizeは1より大きく設定する。10,100,1000と増やしていって最適なスループットになる値を見つける

スループットが変化する場合、レイテンシにあまり関心がなければbatch sizeを10程度に小さくし、レイテンシ要件が厳しい場合はbatch sizeを1に設定する

## Flush Tuple Frequency

バッチの定期的なFlushの設定。バッチサイズが大きいときなど長い時間送信に時間がかかってしまうことがあるため、定期的にflush tupleおｗSpoutやBoltに流して問題のあるバッチをflushしている

`topology.flush.tuple.freq.millis` flush tupleを生成する間隔。0ならflush tupleを生成しない

## Wait Strategy

CPU利用をコントロールするためにwaiting strategyが設定できる

Spoutは、nextTuple()メソッドの呼び出し間隔。`topology.max.spout.pending`を指定する。

Boltは、空のキューをチェックするために`topology.bolt.wait.strategy`で選択する

Backpressureは、`topology.backpressure.wait.strategy`で選択する。キューがいっぱいのときに前段からの受け取りをコントロールする

## Max spout pending

spoutが `topology.max.spout.pending` の値を超えた場合、ackされていないtupleの限界となりnextTuple()メソッドが呼ばれなくなってemitされなくなる

値を大きくするとレイテンシが悪化、メモリ消費が増える

## Load Aware messaging

`topology.disable.loadaware.messaging` でtrue, falseを設定

## Sampling Rate

`topology.stats.sample.rate` 1に設定すると全て、0.001に設定すると1000メッセージ毎に統計用のメトリクスを取得できる

サンプリングレートを減らすとスループットやレイテンシが改善できる

## Budgeting CPU core for Executor

Spout Executor, Bolt Executor, Worker Transferを考慮する必要がある

## Garbage Collection

CMSとG1GCの両方がおすすめ

GCスレッドの数もパフォーマンスに影響がある

## Scaling out with Single Worker mode

Worker内のtaskの通信であれば、シリアライズデシリアライズのコストがかからないのでとても速い。できるだけ単一のworkerインスタンスで実行したほうがパフォーマンスが良い

# Guaranteeing Message Processing

Stormのat-least-onceのメッセージ保証について




