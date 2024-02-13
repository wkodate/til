# 5. Private Builds and Metrics: Tools for Surviving DevOps Transitions

プライベートビルドとメトリクスを使用して、どのようにDevOpsへの移行を乗り切ったか。そのケーススタディ。

## **5.1 Key Terms**

不具合の発見が遅いほど修正にコストがかかる。早期に発見するためにCI/CDの自動化されたパイプラインが必要。

Continuous Delivery vs Continuous Deployment ([Continuous Delivery - Fowler](https://martinfowler.com/bliki/ContinuousDelivery.html))

- Continuous Delivery: 頻繁なデプロイメントを実行できる状態。デプロイメントを含まない場合もある。
- Continuous Deployment: すべての変更がパイプラインを通じて自動的に本番環境へ導入される。これを行うためにはContinuous Deliveryを行う必要がある。

DevOps(Development and Ops Cooperation)の考え方

- プロセス: システム管理者や運用チームは、開発チームと完全に統合されるべき。
- ツール: 開発者と運用者でツールを決定して互いに共有する
- 文化: 開発チームと運用チームのしごとの仕方に一貫性をもたせる。運用に関わる作業もコードと同じ用にバージョン管理システムにいれる。

DevOpsの主要な焦点は文化であり、ツールや自動化はそれに続くものである。

## **5.2 The “Ownership Shift”**

DevOpsは「自動化されたモダンなシステム管理」のことではない。

運用チームが自動化ワークフローを作り、ビルドパイプラインが開発チームから離れ、開発チームは詳細を知らずに検証などをする。サイロ化が進み本番環境との環境の不一致が起こる。

このアンチパターンを「オーナーシップシフト」と呼ぶ。

## **5.3 Empowering the Local Environment Again**

ビルドが壊れたときに開発チームはコントロールをできない。DevOpsチームに依頼するが、優先度に合わせて対応される。対応までの間、デリバリープロセスはブロックされる。

バリデーションのプロセスを開発プロセスの外に移してはいけない。バリデーションがフォーカスされるのは問題が導入されたローカル環境であるべき。

## **5.4 The Private Build / 5.5 Case Study: The Unstable Trunk / 5.6 Case Study: The Blocked Consultant**

開発者はユニットテストを完了した後、各自のワークステーション上で統合ビルドを実行すべき。

自動化や検証が開発チームから切り離されている場合は特に重要になる。

共有のメインラインに変更が入る前に、プライベートの環境でビルドが実行される必要がある。

## **5.7 Metrics / 5.8 Metrics in Practice**

開発フローを評価する際に考慮すべきメトリクス

- フィードバックまでの時間
- 回避可能な統合課題の数。CIやQAで見つかったバグの数、プライベートビルドで検出可能だったバグの数。
- トランクが安定になるまでにかかった時間
