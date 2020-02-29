Google Cloud Platform Big Data and Machine Learning Fundamentals
===

# Week1: Data and Machine Learning on GCP 専門講座の概要

* BigQueryを使用して大規模なデータセットを分析する
* ビッグデータの課題を確認する
* Qwiklabsのラボ環境について理解する
* 組織における重要なデータの役割について学ぶ

## GCPの概要

データ分析の中でも特にデータエンジニアになるためのコース

アジェンダ

* Google Cloudのコアインフラ
    * Compute Power, Storage, Networking, Security
* BigData and ML Products
* GCPでなにができるか
* ユースケース
* 組織におけるデータ関連の役割の違い

### Compute

* データが大量に増えるのでコンピューティング能力の向上が必要になってくる
* 機械学習に特化したCPUであるTPUを数年掛けて開発した。ebayなどで使われている

デモ: Compute EngineでのVM作成

* 仮想マシンを起動して処理を実行、ファイルを取得してCloud Storageにコピー→VMを停止→ファイルを一般公開

### Storage

* コンピューティングとストレージは分けて考える
* 4つのストレージクラス
    * Multi-regional
    * Regional
    * Nearline
    * Coldline
* Cloud Storageにデータをコピーするにはgsutilコマンドを使う

### Network

* エッジネットワークを使って地理的に近い位置からレスポンスを返している

### Security

* Googleの担当範囲で高セキュリティを提供
* データは保存時に暗号化、分散される
* BigQueryのテーブルも鍵もで暗号化されその鍵もエンベロープ暗号化がされている

### ビッグデータMLプロダクト

* GFS(Google File System)
* MapReduce
    * データが増えてコンテンツのインデックスをどうするかを解決
* Cloud Bigtable
    * HBase, MongoDB
* Dremel
    * インフラ管理の課題
    * シャード単位にデータを分割
    * BigQueryのクエリ円陣になった
* Flume, Millwheel, Spanner, TensorFlow, TPU...
* 上記をGoogle Cloudで提供している

## ラボ: BigQuery の一般公開データセットを探索する

BigQueryのユースケース

https://cloud.google.com/bigquery/?hl=ja#bigquery-solutions-and-use-cases

ビッグデータ用ツールを利用しているGoogle Cloudのお客様

https://cloud.google.com/customers/?hl=ja#/products=Big_Data_Analytics

### Google Cloud Platform と Qwiklabs を使ってみる

* publicなdatasetを使ってテーブルを作成、クエリ実行

## ソリューションの適切なアプローチの選択

GCP offers a range of services

Compute

* Compute Engine
* GKE (Google Kubernetes Engine)
* App Engine
* Cloud Functions

Storage

* Cloud Bigtable
* Cloud Storage
* Cloud SQL
* Cloud Spanner
* Cloud Datastore

Big data

* BigQuery
* Cloud Pub/Sub
* Cloud Dataflow
* Cloud Dataproc
* Cloud Datalab

### GCPでできること、ユースケース

* Keller Williamsという不動産会社がAutoML Visionを使って家具を自動認識
* Ocadoというネット専門スーパーが、クレームメールの本文を自然言語処理でで解析して対応が必要な部署にメールを送る
* ベビーフードメーカーのKewpieの変色したポテトを検出する画像認識ソリューション
* ライドシェアのGO-JEKは、5TB/dayのデータを分析。データが増えてレポートが1日遅れで作られるので、データパイプラインをGCPに移行。
    * Data Source→Pub/Sub→Dataflow→BigQuery
    * 需要と供給がマッチしているかをリアルタイムで調べる

### 組織におけるデータ関連の役割の違い

* データアナリスト
    * アナリスト
* データエンジニア
    * データエンジニア
* MLエンジニア
    * 統計学者
    * リサーチャー
    * アプライドMLエンジニア
    * データサイエンティスト
    * ソーシャルサイエンティスト
    * 倫理担当者
* テックリード
    * 意思決定者
    * 分析マネージャー

### テスト

* ビッグデータの課題
    * オンプレミスの既存ワークロードのクラウド移行
    * 大規模なデータセットの分析
    * ストリーミングデータパイプラインの構築
    * データセットへの機械学習の適用
* 大企業がリソースを管理しやすくするためには、定義済みの組織、チームやプロダクトのフォルダ、IAMを使用したアクセス制御ポリシーが必要
* Google Cloudのセキュリティのメリット
    * 公開時の安全確保のためにコンテンツポリシーとアクセスポリシーが自動的に管理、選択される
    * アプリケーションとインフラを実行する物理ハードウェアが保護される
    * セキュリティポリシーの管理と設定に役立つCloud IAMのツールが用意されている
* 大規模なデータセットがない場合は、Google Cloudの一般公開データセットやオンライン上にある一般公開データセット、または、独自のデータセットを見つけてBigQueryにアップロードする
* Compute Engineはオンデマンドで割り当てられ、稼働時間に対して料金が発生する

### 学習用教材

* GCPブログ
    * https://cloud.google.com/blog/ja/products
* GCPのビッグデータプロダクトのリスト
    * https://cloud.google.com/products/big-data/?hl=ja
* GCPのお客様事例紹介
    * https://cloud.google.com/customers/?hl=ja#/

SQL

* BigQuery 標準 SQL リファレンス
    * https://cloud.google.com/bigquery/docs/reference/standard-sql/enabling-standard-sql?hl=jp
* Qwiklabs のデータ アナリスト向け BigQuery クエスト
    * https://www.qwiklabs.com/quests/55?locale=ja

ビッグデータインフラストラクチャ

* Compute Engine: https://cloud.google.com/compute/
* Storage: https://cloud.google.com/storage/
* 料金: https://cloud.google.com/pricing/


# Week2: Cloud SQL と Spark を使用した商品のレコメンデーション

## レコメンデーション システム

* レコメンデーションシステムには、データ、モデル、インフラが必要になる
* if文だとスケールしない、コードベースが複雑になるので、MLで自動に学習する
* Machine Learning = Examples, not rules
    * 機械学習はルールではなく、実例で何かをコンピュータに教えること
* ユーザのレーティングを取り込む(暗黙的な評価も含む)→トレーニングをしてユーザ毎の評価を予測→ユーザの他の物件への評価+対象物件への他のユーザからの評価をつかってレコメンデーション
* 評価予測の頻度は、1日に一度や週に一度でよくバッチで良い

## 課題: クラウドへのワークロードの移行

* オンプレでHadoopやSparkをクラウドへ移行する
* Spark MLのジョブをCloud Dataprocで実行し、評価をCloud SQLのRDBMSに移行する
* データが非構造化データの場合、Cloud Strorage
* 構造化データの場合
    * トランザクショナルワークロードの場合
        * SQLの場合
            * 一つのストレージで十分ならCloud SQL
            * スケールが必要ならCloud Spanner
        * No-SQLの場合
            * Cloud DataStore
    * データ分析ワークロードの場合
        * ミリ秒レイテンシの場合、Cloud Bigtable
        * 秒レイテンシの場合、BigQuery
* Cloud SQLはフルマネージドなRDBMS

オンプレミスとクラウド

* SparkジョブをCloud Dataprocで実行
* オンプレだとクラスタの容量が固定されてしまう、GCPなら柔軟に変更できる
* Cloud Dataproc
    * クラスタの管理が不要でHadoopが使える
    * 既に存在するHadoopのワークロードを簡単に置き換えることができる
    * ComputeとStorageを分けるためにGCSに接続する
        * ジョブごとにクラスタを起動停止できる
    * クラスタのサイズは可変
        * オートスケーリングの機能があるのでリソースを柔軟に変更できる
        * プリエンティブルVM(有効期限が短い安いインスタンス)を使用できる

## ラボ: Cloud SQL と Spark ML を使用して商品を推奨する

* 物件レコメンドシステムのMLワークロードをオンプレのHadoopクラスタからクラウドに移行させる
* 流れ
    * Cloud SQLインスタンスを作ってテーブルにデータを入れる
    * Cloud ShellでSQLを使ってレンタルデータを調べる
    * Dataprocの起動
    * レコメンデーションを作るためにPySparkで書かれたモデルでレコメンドを作成
    * Cloud SQLでインサートした行を調べる

### 学習用教材

* Hadoop から Google Cloud Platform への移行
    * https://cloud.google.com/solutions/migration/hadoop/hadoop-gcp-migration-overview?hl=ja
* Cloud SQL のドキュメント
    * https://cloud.google.com/sql/?hl=ja
* Cloud Dataproc のドキュメント
    * https://cloud.google.com/dataproc/?hl=ja

# Week3: BigQuery ML で訪問者の購入を予測する

## BigQuery の概要

* BigQueryの特徴
    * サーバレス
    * 従量課金モデル
    * 保存データはデフォルトで暗号化
    * 地理的な保存場所を指定、IAMで特定の行のみのACL設定が可能
    * AIやMLワークロードの基盤になる
* GitHubのデータを使って、開発者がタブとスペースどちらを好むのかをBigQueryを使って集計するデモ

## 大規模なデータセットを SQL で探索、分析する

* BigQueryのサービス
    * BigQuery Storage Service。データ用のフルマネージドストレージ
    * BigQuery Query Service。高速SQLクエリエンジン
* セキュリティとしてView, Editor, Ownerなどのロールがある

## BigQuery にデータセットを取り込んで保存する

* データセットのストレージとメタデータを管理
* BigQueryの外部のデータソース(例えばCloud Storage)にあるデータに対してもクエリを実行できる
* ストリーミングで挿入する行の最大サイズは1MB、最大スループットは10万レコード/秒
* 列に構造型のデータ型でデータを保存できる。この場合JOIN不要

## BigQuery ML で SQL を使用して機械学習を適用する

モデルの種類の選択

* 過去のデータから正しい答えが取得できそうな場合は教師あり学習
    * ラベル列が数値データなら予測
        * 線形回帰
    * ラベル列が文字列データなら分類
        * 路地スティク回帰、2高ロジスティック回帰
    * レコメンデーション
        * 行列分解(Matrix Factorization)
* 過去のデータから正しい答えが取得できない場合は教師なし学習
    * クラスタリング

学習

* モデルによって顧客の生涯価値(LTV)を予測するシナリオ
* featureカラムがモデルの入力になる
* モデルの作成にはすごく時間がかかるが、BigQuery内の構造化データセットでMLを操作できる
    * SQLでモデルを作成する
    * SQLで予測クエリを書く
    * 結果を得る

BigQuery ML(BQML)のフェーズ

* BigQueryにデータを取り込みでETL
* 特徴の選択、前処理
* BigQueryにモデルを作成
* モデルのパフォーマンスを評価
* 予測に使用

BigQuery MLの主な機能

* `CREATE MODEL` モデルを作成
* `ML.WEIGHTS`　重みの重要度を確認
* `ML.EVALUATE`　パフォーマンスを評価
* `ML.PREDICT`　予測

BigQuery MLチートシート

* Label
    * labelという名前のカラムをつくるもしくはinput_label_colsを使う
* Feature
    * CREATE文の後のSELECT文で`ML.FEATURE_INFO`でデータ列としてモデルに渡す
* Model
    * BigQueryで作られるオブジェクト
* Model Types
    * 線形回帰、ロジスティック回帰。`CREATE MODEL`でモデルを作成
* Training Progress
    * `ML.TRAINING_INFO`でトレーニングの進捗を確認
* Inspect Weights
    * `ML.WEIGHTS`で予測対象のラベルに関連してモデルに学習させた各特徴の重要度を確認できる
* Evaluation
    * `ML.EVALUATE`でモデルの性能を確認
* Prediction
    * `ML.PREDICT`で予測

## ラボ: BigQuery ML で訪問者の購入を予測する

## 学習用教材

* Cloud Pub/Sub のドキュメント
    * https://cloud.google.com/pubsub/?hl=ja
* Cloud Dataflow のドキュメント
    * https://cloud.google.com/dataflow/?hl=ja
* データポータルのドキュメント
    * https://developers.google.com/datastudio/?hl=ja
* ブログ: BigQuery での Google スプレッドシートの使用
    * https://cloud.google.com/blog/products/g-suite/connecting-bigquery-and-google-sheets-to-help-with-hefty-data-analysis

# Week4: Cloud Pub/Sub と Cloud Dataflow を使用してストリーミング データ パイプラインを作成する

## データパイプラインの構築

## Cloud Pub/Subによるメッセージ指向アーキテクチャ

## スケーリングに対応したストリーミング データ パイプラインを設計、実装する

## 分析情報をダッシュボードで可視化する

## ラボ: Cloud Dataflow を使用してストリーミング データ パイプラインを作成する

## 学習用教材

# Week5: Vision API と Cloud AutoML を使用して、構築済みのモデルで画像を分類する

# Week6 まとめ
