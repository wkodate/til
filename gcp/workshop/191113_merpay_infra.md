merpay インフラ Talk #5 ~GCP関連の技術編~
==

# Spannerの窯問題について

* データベース共有はしていない
* Spannerは高い、1ノード8万の最低ノード24万でそれをマイクロサービス毎に使うからものすごく高価になってしまう
* Spannerをどうやって運用するか、監視するか、トレーニングをやっている
* レイテンシ問題が起きた話
* 1時間毎に遅くなる問題があった。1-30間くらい遅くなってしまう
* バックオフ=リトライ しているログが見つかった
* 1秒トークンリフレッシュ処理が一部原因でそれは解消
* gRPCは一回TCP接続したらコネクション張りっぱなし、gRPCを多重化していない
* SpannerはgRPCで1時間に1回コネクションを落とすらしいとサポートに言われた
* 再接続時に名前解決に時間がかかっていた
* トータル3ヶ月くらいかかった
* まとめ
    * 複合的な問題によって1時間毎に遅くなる問題を解決しました。
    * ライブラリもバグが有る

# SpannerのSingleRegion・MultiRegionの話

* GCPしかやっていない、Spannerのことばありやっている
* Single Region vs Multi Regionの話
* Strong read request, Stale read request
    * https://cloud.google.com/spanner/docs/reads
    * Strong readは現在のデータ
    * Stale readは古いデータ
* ApplicationとSpannerのLeaderが同じRegionにいないとレイテンシが上がる
* Multi RegionはSingle Regionよりレイテンシが上がるし、料金も10倍くらいの値段になる
* Multi Regionの中で大陸がちがってもそんなにレイテンシに差はない
* mutation
    * 挿入、更新、削除の単位
    * INSERTなどの更新系では1回のトランザクションあたりのmutationの数に制限がある
* まとめ
    * Multi-Regionのレイテンシを許容できるか考える必要がある
    * 結局コストとの兼ね合い

# Spanner Autoscaler

* Spannerオートスケールしない問題とKubernetes Operatorのを使ってAutoscalerを作った
* Kubernetes Controllerを自作
