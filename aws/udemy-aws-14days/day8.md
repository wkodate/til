Day8 Route 53を使ってドメインを登録する
==

## やること

* DNSについて
* Route 53について
* 使ってみる

## DNSについて

* ドメイン名とIPアドレスの紐付けを行う
* A: ドメインとIPアドレスの対応
* CNAME: ドメインの別名をマッピング
* NS: そのドメインを管理するネームサーバ

## Route 53について

* DNSの機能を簡単に利用できる
    * GUIから設定可能
    * CLAやCloudFormationなどを用いた設定自動化も可能
* SLA100%
* ルーティングポリシー
    * Simple
    * Weighted
        * 重み付けができる
    * Latency
        * レイテンシが最小のリソースが優先
    * Geolocation
        * リクエスト元の位置情報によって優先度が変わる
    * Failover
        * プライマリをセカンダリを指定

## 使ってみる

* freenomでドメインを取得
* Failoverを確認
