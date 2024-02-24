# **6. Scaling an Organization: The Central Role of Software Architecture**

ソフトウェアアーキテクチャとメトリクスについて。企業の組織構造と、組織の中の人々のコミュニケーションがソフトウェアアーキテクチャに影響を及ぼす。

コンウェイの法則: システムを設計する組織は、その組織のコミュニケーション構造がそのまま設計に反映される。

## **6.1 YourFinFreedom Breaks the Monolith / 6.2 Implementing a Distributed Big Ball of Mud**

YourFinFreedomのモノリスをマイクロサービスに変更するプロジェクトの例。より速く動作し、より良い品質を顧客に提供するのが目的。途中でミクロサービスアーキテクチャが会社のビジネスニーズに合っていないんじゃないかと疑問を抱き、改善をしていく話。

モノリスは「巨大な泥団子(big ball of mud)」、マイクロサービスは「分散した巨大な泥団子(distributed bi ball of mud)」になりがち。

巨大な泥団子は、偶発的な複雑さを含む。偶発的な複雑さは、依存関係や可読性の低いコード、テストがされていないコードなどがシステムに混入される複雑さ。

分散した巨大な泥団子は、過剰な設計による認知負荷の限界をもたらす。

## **6.3 Seeking Direction**

組織のソフトウェアアーキテクチャは、そのミッションやKPIと結びついている必要がある。

## **6.4 From Best Effort to Intentional Effort**

ベストエフォートからインテンショナルエフォート(決定を意図的に行う)へ。そのためには、仕事の流れを可視化し、目標達成のためにソフトウェアをどう使うか考える必要がある。

イベントストーミングは、複雑なビジネスプロセスやドメイン知識を共有、可視化、理解するための共同作業ベースのモデリング手法

- [イベントストーミング｜【DDD入門】TypeScript × ドメイン駆動設計ハンズオン](https://zenn.dev/yamachan0625/books/ddd-hands-on/viewer/chapter5_event_storming)
- [ミニマムにはじめるイベントストーミング | | AI tech studio](https://cyberagent.ai/practical-introduction-to-your-first-event-storming)

## **6.5 Increasing Software Architecture Intentionality, Guided by Metric**

イベントストーミングによって、メンバーの集合知を可視化できる。

KPIをマッピングすると、バリューストリームの中の重要度や、組織がどの用に価値を計測することにしたのかを詳しく理解できる。

ホットスポットを分析することは、どこに無駄があるかを理解することにつながる。KPIとホットスポットが含まれた図の例: https://cdn.oreillystatic.com/pdf/Software_Architecture_Metrics_Online_Figure_2.pdf

KPIバリューツリーを使って組織のKPIとソフトウェアアーキテクチャを結びつける。第1レベルは組織KPI、第2レベルはドメインKPI、第3レベルはメトリクス。

有用なメトリクス: 第1症の4つのキーメトリクス(変更のリードタイム、デプロイの頻度、変更時の障害率、サービス復旧時間)、インシデント発生から検出までの平均検出時間(MTTD)、スループット、epmloyee NPS(従業員ネットプロモータースコア)

## **6.6 Managing Expectations with Communication**

ビジネスが変わればソフトウェアアーキテクチャも変わらなければならない。KPI、メトリクスも変わってくる。常にKPIバリューツリーをメンテナンスする。

ソフトウェアアーキテクチャの自然な進化は、メトリクスによって導かれるべき。

## **6.7 Learning and Evolving the Architecture**

自分のコンテキストにあったメトリクスを使う。あるメトリクスの傾向が、そのメトリクス自体よりも重要な場面がある。ドメイン境界を安定させてソフトウェアコンポーネントのオーナーシップを見直す。
