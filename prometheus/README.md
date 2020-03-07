Prometheus
===

* exporter
    * 監視対象で動くプログラム。APIになっている
    * node_exporter
        * CPU、メモリなどのリソースを取得するためのexporter
* prometheus
    * 監視サーバのプログラム。定期的にexporterをポーリング
* prometheus, alertmanager, pushgatewayの構成
* PushGateway
    * バッチなど短いジョブのデータをprometheusで監視するためのシステム
