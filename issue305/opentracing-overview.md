OpenTracing Overview: Distributed Tracing’s Emerging Industry Standard
==

https://sematext.com/blog/opentracing-distributed-tracing-emerging-industry-standard/

* OpenTracingのイントロダクション記事。全5シリーズの内の最初の1つ目の記事
* OpenTracingの[公式サイト](https://opentracing.io/)
* [Javaのチュートリアル](https://github.com/yurishkuro/opentracing-tutorial/tree/master/java)もある


## What is OpenTracing?

* OpenTracingは、分散トレーシングのためのAPI仕様とそのライブラリのこと
* 分散トレーシングは、マイクロサービスのような分散したシステムのリクエストを分析する
* CNCFに入っている
* OpenTracingのゴールは共通のトレーシングのメカニズムを使うこと

## OpenTracing Benefits

 * 設定を変えるだけで新しいtracerを作れる
 * レイテンシなどのパフォーマンス問題を効率的に検知できる
 * エラーが起きたときに特定しやすい
 * tracingデータをロギングできる

## OpenTracing and Distributed Context Propagation

* Context propagationは、リクエストが目的地に到達するまでのパスを表す

## Adoption: Who uses OpenTracing

* アプリケーションがモノリシックからマイクロサービスに代わり、可視化やソフトウェアの振る舞いを観測することは必要不可欠になる
* 複雑なワークフローをデバックするために、いくつかのtracingメカニズムを利用した分散トレーシングが出てきた
  * Zipkin
  * Jaeger
  * AppDash
* 運用者にとって、システムの問題に対する原因究明や最適化に役立つ

## Next steps

* [Zipkin as OpenTracing-compatible Distributed Tracer](https://sematext.com/blog/opentracing-zipkin-as-distributed-tracer/)

