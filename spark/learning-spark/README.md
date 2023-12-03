# Learning Spark - 2nd Edition

# Learning Spark - 1st Edition

Spark1系なので情報が古い。

## 7. クラスタでの動作

### Spark ランタイムアーキテクチャ

DriverはExecutorと呼ばれる大量のワーカーと通信する。このDriverとExecutor群はSparkアプリケーションと呼ばれる。

Sparkアプリケーションは、YARNのようなCluster masterと呼ばれる外部のサービスを使い、複数のマシン上で起動される。

ジョブ → ステージ → タスク

実行のステップ

1. spark-submitでアプリケーションをsubmit
2. spark-submitはドライバプログラムを起動し、main()メソッドを呼び出す。
3. ドライバプログラムはクラスタマネージャに接続し、エグゼキュータを起動するためのリソースをリクエストする。
4. クラスタマネージャがエグゼキュータを起動。
5. ドライバプログラムがユーザアプリケーションを実行する。ドライバはタスクをエグゼキュータに送る。
6. エグゼキュータでタスクが実行され、結果が保存される。
7. ドライバのmain()メソッドが終了すると、ドライバはエグゼキュータを終了させ、クラスタマネージャから取得したリソースを解放する。

### ドライバ

マスター。main()メソッドを実行するプロセス。

Sparkアプリケーションごとに1つ。

Driverの責務

- ユーザプログラムのタスクへの変換
    - SparkプログラムはDAGを生成し、この論理的なグラフは物理的な実行計画に変換される。
    - 実行グラフをStage群に変換。Stageには複数のタスク群が含まれる。
- Executor上のタスクのスケジューリング
    - Executorで実行するタスクのスケジューリング
    - Web UIでSparkアプリケーションの情報を公開。YARNクラスタモードならリソースマネージャを覬覦する必要がある。

### エグゼキュータ

ワーカー。タスクを実行、RDDのデータを保持する。

Executorの責務

- タスクを実行し、結果をDriverに返す。
- ユーザプログラムによってキャッシュされるRDDのためのインメモリストレージを、各Executor内で動作するブロックマネージャと呼ばれるサービスを通じて提供する

### クラスタマネージャ

YARNやMesos、StandaloneなどのクラスタマネージャがDriverとExecutorを起動する。

client modeとcluster modeの2つのデプロイモードがある。

- client mode
    - Driverはspark-submitを実行したクライアントのプロセスで起動する
    - Application masterは、リソースのリクエストのためにだけ使われる。
- cluster modeは
    - DriverはApplication master内で起動する。
    - 実行したクライアントは、アプリケーションの初期化後に終了。fire-and-forget。

`--num-executors` でExecutorの数を設定。

`--executor-memory` で各Executorのメモリ、`--executor-cores` で各Executorのコア数を設定

## 8. Sparkのチューニングとデバッグ

Tuning - Spark 3.5.0 Documentation:  https://spark.apache.org/docs/latest/tuning.html

SparkConfにconfigをセットする。これを使ってSparkContextを作成。spark-dhsubmitで動的に設定することも可能。

デフォルトでは、spark-submitは`-conf/spark=defaults.conf`  から設定を読む。

これらの優先度が高い順は、SparkConfオブジェクトのset() → spark-submitに渡されたフラグ → プロパティファイル内の値 → デフォルト値

Spark Configuration: https://spark.apache.org/docs/latest/configuration.html

### Spark Web UI

Jobs

- アクティブなSparkジョブ、直近に完了したSparkジョブの詳細な実行情報
- ジョブ、ステージ、タスクのパフォーマンスチェックなどで使われる
    - スキューがないか
    - 各フェーズの実行時間。reading, computing, writing

Storage

- 永続化されたRDDの情報
    - persist()が呼ばれたあとに、何らかのジョブでそのRDDが計算されたときに永続化される。
- 各RDDのどの部分がキャッシュされていて、ディスクやメモリにどれだけのデータがキャッシュされているかがわかる。

Executor

- アプリケーションのアクティブなエグゼキュータの情報
- アプリケーションが期待通りの量のリソースを持っているかを確認dケイル。
- Thread Dumpボタンでエグゼキュータからスタックトレースを収集できる。

Environment

- Sparkアプリケーションのプロパティ郡の情報
- 正しい設定がされているか。

### ドライバ、エグゼキュータのログ

`yarn logs -applicationId <app ID>` でログを確認する。

ログレベルを変えたいなら、—filesフラグでlog4j.propertiesを追加できる。

## 9. Spark SQL

Spark SQLの主要な機能

1. さまざまな構造化ソースからのデータロード(JSON, Hive, Parquet)
2. SQLを使ったデータのクエリ。Sparkプログラム内、BIツール、JDBC/ODBCツールからの実行。
3. SQLとPython/Java/Scalaコードの組み合わせを提供

SchemaRDD (1.4からDataFrame)と呼ばれるRDDを提供する。RowオブジェクトからなるRDD。スキーマを使って効率的にデータを保存。

クエリ言語はHiveQLを使うのがおすすめ。

Hiveからロードする場合、Hiveがサポートするすべてのストレージフォーマット(SerDe)をサポートしている。

パフォーマンスチューニングのために、`spark.sql.codegen`、`spark.sql.inMemoryColumnarStorage.batchSize`などのオプションがある。

詳細は2nd Editionの方で確認する。