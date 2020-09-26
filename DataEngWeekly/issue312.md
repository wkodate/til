Issue312
==

# Highlights from the new Apache Avro 1.9.0 release

https://blog.godatadriven.com/apache-avro-1-9-release

* 2017/05/31の1.8.2からの2年ぶりの1.9.0へのアップデート
* Avroのイントロダクションは[こちら](https://binx.io/blog/2018/12/09/apache-avro/?__hstc=153823591.4817983881c0864fef830a24b415bac0.1559053124386.1559053124386.1559053124386.1&__hssc=153823591.1.1559053124387&__hsfp=3596187438)
* Joda timeライブラリのdeprecated、Java8のData and Time APIを使う
* Jacksonバージョンアップ
* Zstandard compressionをサポート
* Hadoop1系のサポートなし。Hadoop3系は公式にはまだサポートされていない
* guavaやparanamerなどいくつかの依存関係を排除
* Java11サポート

# Maintainable ETLs: Tips for Making Your Pipelines Easier to Support and Extend

Stitch Fixが書いた、メンテナンス可能なデータパイプラインのベストプラクティス

* ETLを簡単に理解し、継続して利用してくためのポイント
    * Building a chain of simple tasks.
    * Using a workflow management tool.
    * Leveraging SQL where possible.
    * Implementing data quality checks.

## Building a Chain of Simple Tasks

* 大きく複雑な処理をを小さなタスクに細分化しよう
* メリットは、個別のタスクや全体の処理が簡単に把握できる、バリデートが簡単、モジュール化して再利用可能、中間結果を観れる、パイプラインの信頼性向上
* 高い値段のアイテムの割合はどのくらいか、を計算するためのパイプラインの例

```
WITH added_threshold as (
  SELECT
    items.item_price,
    thresh.high_price_threshold
  FROM shipped_items as items
  LEFT JOIN thresholds as thresh
    ON items.client_segment = thresh.client_segment
      AND items.item_category = thresh.item_category
), flagged_hp_items as (
  SELECT
    CASE
      WHEN item_price >= high_price_threshold THEN 1
      ELSE 0
    END as high_price_flag
  FROM added_threshold
) SELECT
    SUM(high_price_flag) as total_high_price_items,
    AVG(high_price_flag) as proportion_high_priced
  FROM flagged_hp_items
```

## Use Workflow Management Tool

* 信頼できるワークフローマネジメントとスケジュールエンジンは生産性をあげる。Airflow, Ooozie, Luigi, Pinballなどが有名
* ワークフローツールはDAGの定義や実行スケジューリング、リトライ、失敗時のアラーティングなどを担ってくれる

## Leverage SQL Where Possible

* SQLはどのロールの人でも利用できる共通の言語なので使おう

## Implement Data Quality Checks

* 自動ユニットテスト
* データソースが更新されているかチェックに利用できる
