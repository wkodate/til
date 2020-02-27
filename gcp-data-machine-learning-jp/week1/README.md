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

## 課題: クラウドへのワークロードの移行

## ラボ: Cloud SQL と Spark ML を使用して商品を推奨する

## テスト

### 学習用教材

# Week3: BigQuery ML で訪問者の購入を予測する

# Week4: Cloud Pub/Sub と Cloud Dataflow を使用してストリーミング データ パイプラインを作成する

# Week5: Vision API と Cloud AutoML を使用して、構築済みのモデルで画像を分類する

# Week6 まとめ
