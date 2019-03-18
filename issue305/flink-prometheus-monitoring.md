Flink and Prometheus: Cloud-native monitoring of streaming applications
===

https://flink.apache.org/features/2019/03/11/prometheus-monitoring.html

* FlinkのモニタリングにPrometheusを使う話　
* 基本的なPrometheusのコンセプトと、FlinkでPrometheusを使うと良い理由について説明
* 詳細はFlinkForward Berlin 2018の資料
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
* `PrometheusExampleJob`はランダムの数を生成し、mapでカウント、ヒストグラムを生成するアプリケーション

## Configuring Prometheus with Flink

1. `PrometheusReporter`のjarをFlinkクラスタが参照可能なクラスパスに追加
2. flink-conf.yamlにreporterの設定を追加
3. prometheus.ymlのPrometheusのscrape設定に追加

```
scrape_configs:
- job_name: 'flink'
  static_configs:
  - targets: ['job-cluster:9999', 'taskmanager1:9999', 'taskmanager2:9999']
```

* port 9249,9250,9251などでJob Manager, TaskManagerのメトリクスが見れる
* 通知はAlertmanagerを使ってメールやSlackに飛ばす。メトリクス可視化はGrafanaで

## Conclusion

* Prometheusを使うと簡単に効率的にFlink Jobのモニタリング、アラーティングができる

