Storm 2.0.0 Released
===

* Stormのメジャーバージョンアップ

https://storm.apache.org/2019/05/30/storm200-released.html

## New Architecture Implemented in Java

* ClojureからJavaで実装し直した

## New High Performance Core

* コア部分を刷新してパフォーマンス向上
    * threadingモデル
    * 高速メッセージングサブシステム
    * 軽量back pressure
* Storm2.0は1msecレイテンシとなる最初のストリームエンジン

## New Stream API

* Stream APIでspoutやboltをハイレベルAPIで提供

## Windowing Engancements

* state backendにstateを保存できるようになり、継続的なwindow処理をサポート

## Kafka Integration Changes

* 1系からあったstorm-kafkaを削除
* storm-kafka-clientからsubscriptionインターフェースが削除され、TopicFilter, ManualPartitionerインターフェースの利用を推奨
* storm-kafka-clientのexample https://github.com/apache/storm/tree/master/examples/storm-kafka-client-examples

## その他

* 1.0.xはEOLになりメンテナンス対象外
* Java7サポート外、2系からはJava8必須
* 今後2系の様々な機能についてブログをポスト予定
    * SQL enhancements
    * Metrics improvements
    * New security features such as nimbus admin groups, delegation tokens, and optional impersonation
    * Module restructuring & dependency resolution improvements
    * API improvements
    * Lambda support
    * Resource Aware Scheduler enhancements
    * New admin commands for debugging cluster state
