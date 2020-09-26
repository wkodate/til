Issue331
==

# The Future of Data Engineering: Chris Riccomini at QCon San Francisco

https://www.infoq.com/news/2019/11/data-engineering-future-qconsf/

スライド

https://qconsf.com/system/files/presentation-slides/qconsf2019-chris-riccomini-future-of-data-engineering.pdf

QConのfuture of data engineeringの話。データエンジニアリングの6つのステージの変化についてWePayのアーキテクチャを参考に説明

"a data engineer's job is to help an organization move and process data". 

"Move" means streaming or data pipelines

"process" means data warehouses and stream processing

decentralizationまでのステージ

* Stage0: None
* Stage1: Batch
* Stage2: Realtime
* Stage3: Integration
* Stage4: Automation
* Stage5: Decentralization

## None

小さい組織。モノリシックなアーキテクチャであり、データウェアハウスを早急に必要としている

Monolith→DBの構成でSQLで問い合わせ

## Batch

まだモノリシックなアーキテクチャを持っているが組織はスケールを必要としている。レポートやチャートなどが求められる

組織が成長につれて、ジョブの負荷が上がったりワークフローのタイムアウト問題やDBオペレーションによる問題が発生してくる

Monolith→DB→Scheduler→DWHの構成

## Realtime

組織がKafkaのようなインフラを持つ

リアルタイムのセットアップに関していろいろなシステムが増えてくる

Monolith→DB→Streaming Platform→DWHの構成

## Integration

システムの数を減らすためにIntegrationが必要になる

WePayは現在このステージ

Service→MySQL,DB,NewSQL→Streaming Platform→Search,DWH,GraphDBの構成

## Automation

アーキテクチャが煩雑になってくるので設定の管理やデプロイに自動化が求められる

## Decentralization

完全に自動化されたリアルタイムデータパイプラインが完成

異なるチームでデータパイプラインやデータウェアハウスを管理できる状態にする。マイクロウェアハウス化する

