Issue332
==

# GokuL: Extending time series data storage to serve beyond one day

Pinterestの時系列データストアGokuについての詳細

PinterestではStatsboardを使って開発者がシステムのモニタリングやアラートをしている。そのバックエンドでGokuが使われている。

長期データを扱うためにGokuLを導入。GokuLは1日以上の長期保存用でdiskベースのGoku。LはLong-termのL

## データのロールアップ

5分間隔でsumするなどのaggregate処理ができる

## アーキテクチャ

* Gokuは1日分のデータをAWS EFSに書き込む
* Goku CompactorはEFSから1日分のデータを読み込んでS3に書き込む
* GokuLはS3からデータを読んでローカルのRocksDBに書き込む
* StatsboardはGokuLからデータを読み込む

## Tieredデータ

データはタイムレンジの異なるデータをそれぞれ圧縮してバケットに分けておく。Tier0のデータは2時間単位で1つのバケット、Tier1はこれらをコンパクションした6時間毎のバケットにそれぞれ書き込まれる。これらのサイズはインターバルは調整可能

## Goku Compactorサービス

短い期間のGokuのデータをEFSから取得し、tier1バケットにマージ後、S3にアップロードするサービス

コンパクションには多くのCPU,メモリリソースを使う

## データリテンション

tier毎に異なるリテンションを設定可能

