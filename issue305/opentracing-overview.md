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
* OpenTracingのゴールは、トレーシングや伝搬メカニズムの共通利用

## OpenTracing Benefits

 * 設定を変えるだけで新しいtracerを作れる
 * システムのレイテンシなどのパフォーマンス問題を効率的に検知できる
 * システムでエラーが起きたときに原因特定をしやすい
 * トレーシングデータをロギングできる
 * 最適化に役立つ

## OpenTracing and Distributed Context Propagation

* Context propagationは、リクエストが目的地に到達するまでのトランザクションパスを表す
* メッセージヘッダなどの伝搬されたspan contextを抽出し、traceをジョインして伝搬
  * Span: サービス内の処理
  * Trace: Spanの最初から最後までの集合

## Adoption: Who uses OpenTracing

* アプリケーションがモノリシックからマイクロサービスに代わり、可視化やソフトウェアの振る舞いを観測することは必要不可欠となった
* この複雑なワークフローをデバックするために、いくつかのトレーシングメカニズムを利用したJaegerなどの分散トレーシングが出てきた。
* JaegerはUberが開発。GoogleのDapperがインスパイア元

## Next steps

* [Zipkin as OpenTracing-compatible Distributed Tracer](https://sematext.com/blog/opentracing-zipkin-as-distributed-tracer/)

