# 8. Progressing from Metrics to Engineering

## 8.1 The Path to Fitness Functions

「アーキテクチャ適応度関数とは、アーキテクチャ特性に対する客観的な評価基準を提供するあらゆるメカニズムである」

- アーキテクチャ特性
    - 非機能要件、横断的要件、システム品質特性を指す。具体的にはパフォーマンス、スケーラビリティ、弾力性、可用性などがある。
    - ドメインはソフトウェアを書く動機や問題領域であり、アーキテクチャ特性は設計におけるドメインによらない考慮事項。
    - ドメインのテストは、ユニットテスト、機能テスト、ユーザ受け入れテスト。
    - アーキテクチャ特性のテストは、ビルド時間のチェック、本番環境の監視、フォレンジックなど。
- 客観的な評価基準
    - アーキテクトはどのようなアーキテクチャ特性でもそれを客観的に計測・検証できる必要がある。
    - 複数のアーキテクチャ特性から構成される複合アーキテクチャ特性もある。
- あらゆるメカニズム
    - 単一のテストツールではなく、様々な種類の動作を含む。
    - テストライブラリ、パフォーマンス監視、メトリクス、カオスエンジニアリングなどのさまざまなツールを使って適応機能を実装する必要がある。

## 8.2 From Metrics to Engineering / 8.3 Automation Operationalizes Metrics

デプロイメントパイプラインに適応度関数を追加し、自動化を用いて継続的に検証することによって、メトリクスはエンジニアリングへと変換される。

適応度関数を継続的インテグレーションに関連付けることで、手作業のガバナンスチェックを定期的な完全性の検証に変換できる。

## 8.4 Case Study: Coupling

レイヤードアーキテクチャを採用したが、開発チームがそれを正しく実装できる保証がない。

これをArchUnitを使ってユニットテストで構造テストを定義することで、設計が正しいかを確認することができる。

```java
layeredArchitecture()
    .layer("Controller").definedBy("..controller..")
    .layer("Service").definedBy("..service..")
    .layer("Persistence").definedBy("..persistence..")
  
    .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
    .whereLayer("Service").mayNotBeAccessedByAnyLayer("Controller")
    .whereLayer("Persistence").mayNotBeAccessedByAnyLayer("Service")
```

ArchUnitはJavaのプラットフォームでしか利用できないので、アーキテクトはそのための必要な情報を見つけるコードを書かなければならない。

直近24時間のログからそれぞれの呼び出し先を解析し、呼び出し先がルールと異なる場合は例外を発生させる。

```python
def ensure_domain_services_communicate_only_with_orchestrator
  list_of_services = List.new()
                        .add("orchestrator")
                        .add("order placement")
                        .add("payment")
                        .add("inventory")
  list_of_services.each { |service|
    service.import_logsFor(24.hours)
    calls_from(service).each { |call|
      unless call.destination.equals("orchestrator") 
          raise FitnessFunctionFailure.new()
    }
   }
end
```

## 8.5 Case Study: Zero-Day Security Check

もしすべてのプロジェクトにデプロイメントパイプラインがあり、セキュリティチームがそこに適応度関数をいつでも追加できる状態であるとする。

その場合、パスワードが平文で保存されているかのチェックやゼロデイ攻撃のセキュリティパッチなどをすぐに適用できる。つまり広範で重要なガバナンスタスクを一元的に管理し自動化できる。

## 8.6 Case Study: Fidelity Fitness Functions

GitHubのエンジニアリングブログ [Move Fast and Fix Things - The GitHub Blog](https://github.blog/2015-12-15-move-fast/)  の事例の話。

Gitのマージ機能に変わる新しいマージ機能を導入したい。PRのマージボタンを押したときに実行されるコード。

[Scientist](https://github.com/github/scientist)というツールを作ってユーザがバグにさらされることなくチームが安全に検証できるようにした。

Scientistは `use` と `try` の2つの節を提供し、新旧のコードをそこに含める。 

```ruby
def create_merge_commit(author, base, head, options = {})
  commit_message = options[:commit_message] || "Merge #{head} into #{base}"
  now = Time.current

  science "create_merge_commit" do |e|
    e.context :base => base.to_s, :head => head.to_s, :repo => repository.nwo
    e.use { create_merge_commit_git(author, now, base, head, commit_message) }
    e.try { create_merge_commit_rugged(author, now, base, head, 
        commit_message) }
  end
end
```

`use` は必ず実行されユーザは常に個の結果を受け取る。一定の割合(1%程度)で `try`の新コードも実行される。

`try` で発生した例外はすべて記録される。それぞれのメトリクスからパフォーマンスや正確性、エラー数、それらの類似度がダッシュボードに公開される。

4日間これを動かし続け、問題ないと判断して `try`のコードに切り替わった。
