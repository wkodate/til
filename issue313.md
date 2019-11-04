Issue313
===

# DataOps Principles

https://retina.ai/blog/dataops-principles/

* DataOpsの原則

* DevOpsはソフトウェアのデプロイに責任を持ち、DataOpsはデータのマネジメント方法に責任を持つ
* セルフサービスでデータを提供する
    * 利用者に効率的にデータを使ったり変換させたりさせてあげよう。リクエストデータを用意するときに、新たにSQLクエリを書いたり外部からデータをダウンロードしているのであれば、チームは間違った方向に進んでいる
* 手動プロセスを自動化(Autonomation)
    * 手動でSQLやAPIからのpull->繰り返し部分を自動化、手動でその自動化をモニタリング->モニタリングによって見つかった問題の解決の自動化、手動でメトリクスチェック
    * 自動化するのは、infrastructure as codeやサーバレス、データの稼働率やレイテンシ、スキーマバリデーション、データの品質チェックなど
* 時間のかかる大きな変更より頻繁に小さな変更をしよう
    * 変化を速くすることで価値が導き出される
* すべての環境で再現可能にする
    * トラブルシューティングの際、データパイプラインで同じデータをインプットすると本番と同様に失敗する再現性が欲しい
* データサイエンティストとDataOpsエンジニアは同じチームにいるべき
    * ミスコミュニケーションがプロジェクト失敗の一因。データサイエンティストがどのようにデーを利用しているか、データエンジニアがどのようにデータを集めているかはお互いわからない
* データから生み出される価値が進捗になる
    * データプロジェクトのアウトプットはドキュメントではなくデータベースでもなくデータから得られるインサイトや組織のアクションである
* 技術やデザインへの興味を持ち続ける
    * 質の高いデータインフラを構築することによって、トラブルシューティングにかかる時間を減らし、新しい取り組みにフォーカスできる
* 設計でシステムをシンプルにする
    * よく設計されたデータアーキテクチャは複雑なデータに対してシンプルにアクセスできる、設計のよくないでたアーキテクチャはシンプルなデータへのアクセスを複雑にする
* 自立した組織から最適なアーキテクチャ、リクワイアメント、デザインが生まれる
    * 多様なスキルを持った、データからビジネス価値を生み出すことを明確にした、自発的なチームが未来を創る

# How To Become a Data Engineer

データエンジニアのための良記事集、オンラインコースなどもある

https://github.com/adilkhash/Data-Engineering-HowTo

* How To Become a Data Engineer
* Useful articles
* Algorithms & Data Structures
* SQL
* Programming
* Databases
* Distributed Systems
* Books
* Courses
* Blogs
* Tools
* Cloud Platforms
* Other
* Newsletters & Digests

# Storm 2.0.0 Released

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
