Issue305
===

# Flink and Prometheus: Cloud-native monitoring of streaming applications

https://flink.apache.org/features/2019/03/11/prometheus-monitoring.html

* FlinkのモニタリングにPrometheusを使う話　
  * 基本的なPrometheusのコンセプトと、FlinkでPrometheusを使うと良い理由、試してみた
* 詳細はFlinkForward Berlin 2018の資料にも書いてある
  * [スライド](https://www.slideshare.net/MaximilianBode1/monitoring-flink-with-prometheus)
  * [ビデオ](https://www.ververica.com/flink-forward-berlin/resources/monitoring-flink-with-prometheus)

## Why Prometheus?

* コンセプト
  * Metrics: メトリクスを時系列情報として定義
  * Labels: key-valueモデル
  * Scrape: pullベースのシステムであり、メトリクスデータをHTTPでfetch(scrape)する
  * PromQL: Prometheusのクエリ言語
* 大規模システムで使う場合にはPrometheusは良い選択
* 多くのサードパーティのexporterが用意されていている。exporterは、監視対象ホスト毎の統計情報を取得

## Prometheus and Flink in Action

* [デモ用ソースコード](https://github.com/mbode/flink-prometheus-example)  をDockerで実行可能
* アプリケーション`PrometheusExampleJob`をモニタリングしてみる
  * ランダムの数を生成
  * mapでカウント
  * ヒストグラムを生成

## Configuring Prometheus with Flink

設定は以下の手順でできる

1. `PrometheusReporter`のjarをFlinkクラスタが参照可能なクラスパスに追加
2. flink-conf.yamlに`PrometheusReporter`の設定を追加
3. prometheus.ymlにPrometheusのscrape設定を追加

```
scrape_configs:
- job_name: 'flink'
  static_configs:
  - targets: ['job-cluster:9999', 'taskmanager1:9999', 'taskmanager2:9999']
```

* port 9249,9250,9251などでJob Manager, TaskManagerのメトリクスが見れる
* 通知はAlertmanagerを使ってメールやSlackに飛ばす。メトリクス可視化はGrafanaでする

## Conclusion

* Prometheusを使うと簡単に効率的にFlink Jobのモニタリング、アラーティングができる

# OpenTracing Overview: Distributed Tracing’s Emerging Industry Standard

https://sematext.com/blog/opentracing-distributed-tracing-emerging-industry-standard/

* OpenTracingのイントロダクション記事。全5シリーズの内の最初の1つ目の記事
* OpenTracingの[公式サイト](https://opentracing.io/)
* [Javaのチュートリアル](https://github.com/yurishkuro/opentracing-tutorial/tree/master/java)もある


## What is OpenTracing?

* OpenTracingは、分散トレーシングのためのAPI仕様とそのライブラリのこと
* 分散トレーシングは、マイクロサービスのような分散したシステムのリクエストを分析する
* CNCFに入っている
* OpenTracingのゴールは、トレーシングや伝搬メカニズムの共通利用

## OpenTracing Benefits

 * 設定を変えるだけで新しいtracerを作れる
 * システムのレイテンシなどのパフォーマンス問題を効率的に検知できる
 * システムでエラーが起きたときに原因特定をしやすい
 * トレーシングデータをロギングできる
 * 最適化に役立つ

## OpenTracing and Distributed Context Propagation

* Context propagationは、リクエストが目的地に到達するまでのトランザクションパスを表す
* メッセージヘッダなどの伝搬されたspan contextを抽出し、traceをジョインして伝搬
  * Span: サービス内の処理
  * Trace: Spanの最初から最後までの集合

## Adoption: Who uses OpenTracing

* アプリケーションがモノリシックからマイクロサービスに代わり、可視化やソフトウェアの振る舞いを観測することは必要不可欠となった
* この複雑なワークフローをデバックするために、いくつかのトレーシングメカニズムを利用したJaegerなどの分散トレーシングが出てきた。
* JaegerはUberが開発。GoogleのDapperがインスパイア元

## Next steps

* [Zipkin as OpenTracing-compatible Distributed Tracer](https://sematext.com/blog/opentracing-zipkin-as-distributed-tracer/)

# Structured Logging: The Best Friend You’ll Want When Things Go Wrong

https://engineering.grab.com/structured-logging

## Introduction

* 複数のサービスを持つバックエンドシステムを維持するためにはObservabilityが重要
* 良いログが状況の理解、メンテナンス、デバッグをより簡単にする
* Structured loggingと呼ばれるロギングの形式を説明

## What are Logs?

* ログの特徴
  * Log Format
    * syslogのようなシンプルなkey-valueや構造化されたJSONのようなフォーマットがある
    * 構造化されたログはサイズが大きくなるが、検索しやすくリッチな情報が含まれる
  * Levelled logging(Log Level)
    * 重要度に合わせたロギング
  * Log Aggregation Backend
    * Kibana, Splunkなど別のバックエンドシステムで集約
  * Casual Ordering
    * 正確なタイムスタンプで保存されイベントの順番を決定
  * Log Correlation
    * 特徴のあるリクエストやイベントをログと付き合わせ

## The State of Logging at Grab

## Why Change?

## Structured Logging

## But Why Structured Logging?

