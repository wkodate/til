Apache Airflow Quick Start
===

DAGでワークフローを作るとこができるジョブスケジューラ

## アーキテクチャ

Webserver, Scheduler, Worker

各モジュールは管理DBを介して共有

### Webserver

管理画面表示。Flaskで実装

### Scheduler

Job実行のスケジュール管理

### Worker

Jobを実行する

分散キューフレームワークCeleryを使用

### Metadata Database

メタデータ用データベース

### airflow.cfg

airflowの設定ファイル。WebserverやScheduler、Workerが参照する

## ジョブ構成

実行順、依存関係などのワークフローはDAGで定義

Job本体はOperatorで記述されている。PythonOperator, BashOperator, RDMS用のOperatorなど様々なOperatorが用意されている。これらを継承したりして必要なOperatorを追加することもできる。

## Quick Start

pipでインストール

```bash
$ pip install apache-airflow
$ which airflow
/usr/local/bin/airflow
```

DBの初期化、ユーザ作成

```bash
$ airflow db init

$ airflow users create --username admin --role Admin --firstname admin --lastname admin --email admin
[2021-01-24 20:42:38,167] {manager.py:727} WARNING - No user yet created, use flask fab command to do it.
Password:
Repeat for confirmation:
Admin user admin created
```

Webサーバの起動

```bash
$ airflow webserver
  ____________       _____________
 ____    |__( )_________  __/__  /________      __
____  /| |_  /__  ___/_  /_ __  /_  __ \_ | /| / /
___  ___ |  / _  /   _  __/ _  / / /_/ /_ |/ |/ /
 _/_/  |_/_/  /_/    /_/    /_/  \____/____/|__/
[2021-01-24 20:42:57,187] {dagbag.py:440} INFO - Filling up the DagBag from /dev/null
Running the Gunicorn Server with:
Workers: 4 sync
Host: 0.0.0.0:8080
Timeout: 120
Logfiles: - -
Access Logformat:
=================================================================
[2021-01-24 20:42:59 +0900] [83733] [INFO] Starting gunicorn 19.10.0
[2021-01-24 20:42:59 +0900] [83733] [INFO] Listening at: http://0.0.0.0:8080 (83733)
```

スケジューラの起動

```bash
$ airflow scheduler
  ____________       _____________
 ____    |__( )_________  __/__  /________      __
____  /| |_  /__  ___/_  /_ __  /_  __ \_ | /| / /
___  ___ |  / _  /   _  __/ _  / / /_/ /_ |/ |/ /
 _/_/  |_/_/  /_/    /_/    /_/  \____/____/|__/
[2021-01-24 20:43:45,864] {scheduler_job.py:1241} INFO - Starting the scheduler
[2021-01-24 20:43:45,864] {scheduler_job.py:1246} INFO - Processing each file at most -1 times
[2021-01-24 20:43:45,871] {dag_processing.py:250} INFO - Launched DagFileProcessorManager with pid: 83824
```

サンプルジョブを実行(Web UIからも可能)

```bash
$ airflow tasks run example_bash_operator runme_0 2015-01-01
```

実行すると「Browse」→「Jobs」からジョブの実行状態を確認できる。
