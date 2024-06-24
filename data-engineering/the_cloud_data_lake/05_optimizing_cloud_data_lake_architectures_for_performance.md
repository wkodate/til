# 5. Optimizing Cloud Data Lake Architectures for Performance

クラウドデータレイクアーキテクチャのパフォーマンス。

## 5.1 Basics of Measuring Performance

タスクのパフォーマンスの指標には、レスポンスタイムとスループットがある。

一貫生産方式よりも生産ライン方式の方が柔軟にチューニングできる。

## 5.2 Cloud Data Lake Performance

パフォーマンスの高いデータレイクのアーキテクチャを設計するための第一歩は、こなさなければならないジョブの性質とジョブが満たさなければならない要件を理解すること。

SLA: Service-level agreement。顧客に化鳴らす保証すると約束する事項。SLAが満たせなかれば特約条項が適用されたり、金銭的保証が発生することになる。

SLO: Service-level objective。顧客レベルの指標をシステムレベルの指標に翻訳した形で計測される。

SLI: Service-level indicator。システムが実際にどのように動作しているかの計測値

## 5.3 Drivers of Performance

コピージョブのパフォーマンスを上げるためのパラメータ

- ワーカーの数とCPUやメモリ
- ネットワーク帯域幅
- データのサイズと形式

Sparkジョブのパフォーマンスを上げるためのパラメータ

- Sparkクラスタの構成
- Sparkジョブの構成
- データレイクストレージのパフォーマンス (IOPS)、スループット、他のジョブとの競合
- データのプロファイル: 数、サイズ、形式、パーティショニング

## 5.4 Optimization Principles and Techniques for Performance Tuning

パフォーマンスチューニングの原則

- 計画を立てて十分なリソースを確保する
- 最適なデータアクセスのためのベストプラクティスを取り入れる
- 適切な構成を身につけるための時間を確保する

### 5.4.1 Data Formats

Apache Parquetは広く採用されているデータ形式の一つ。

列指向で読み出しが最適化される、データ圧縮に適している。オプションにより高い圧縮率を実現できる。最近のCPUアーキテクチャに適している。

ストレージシステムでは1個の表を複数のファイルに格納する。

Parquetファイルのフッターを読み出し、行グループと列チャンクがファイル内のどこにあるかを調べて行グループをフィルタリング。そこから特定の列だけを読み出す。

[File Format | Parquet](https://parquet.apache.org/docs/file-format/)

### 5.4.2 Data Organization and Partitioning

ファイルサイズが大きければメタデータ処理のオーバーヘッドが最大限に抑えられ、読み出し処理が効率的になる。

大きなデータをネットワークを介して読み出したあとにコンピューティングエンジンでフィルタリングするのではなく、データの選択方法を改善することによって、ネットワークを介して転送されるデータを最小限に抑えられる。

データのパーティショニングは、必要なデータを簡単に取り出せるようにオブジェクト内のデータを整理すること。

### 5.4.3 Choosing the Right Configurations on Apache Spark

Apache Sparkのチューニングできるパラメータ

- データのシリアライズ
- メモリのチューニング: データのために必要なメモリの量のチューニング
- メモリ管理: 実行やストレージへの利用可能メモリの配分のチューニング

Sparkのパフォーマンスチューニング: [Performance Tuning - Spark 3.5.1 Documentation](https://spark.apache.org/docs/latest/sql-performance-tuning.html)

Sparkジョブのリソース利用状況を理解するためにパフォーマンスモニターを使用する。

## 5.5 Minimize Overheads with Data Transfer

データ転送の重要なオーバーヘッド

- コンピューティングとストレージの間のネットワーク呼び出し
- リージョン境界を超えた読み書きの削減

## 5.6 Premium Offerings and Performance

クラウドサービスのプレミアム版に意味があるのは、適切な問題の解決のために使ったときだけ。

プレミアムにしたからパフォーマンスとスケーラビリティが上がってなんでも問題が解決するわけではない。
