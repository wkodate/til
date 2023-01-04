
https://www.elastic.co/guide/en/elasticsearch/hadoop/current/performance.html

書き込みパフォーマンスについて
- bulkサイズを減らす
	- taslk数 * ドキュメント数を見ておく。インデキシングに1-2秒以上かかるのであればbulk size減らすことを検討。それ以下であれば少しずつ増やす
- ESに書き込むタスクのmaxを使う
	- hadoopやsparkでタスクが同時にESに書き込むときの同時タスク数を把握しておく
- なぜrejectionが起きたかを理解する
	- クラスタが高負荷の時にrejectされたドキュメントの割合が10-15%以下ならよい
- リトライをする
	- ジョブがabortしているときにリトライしているかを確認する

https://www.elastic.co/guide/en/elasticsearch/reference/current/size-your-shards.html

- クラスタにshardが多くなって不安定になるovershardingの問題がある
- ユースケースに合わせてカスタムのsharding strategyを作るのが良い
	- productionのデータでベンチマークをとるのが良い
- Best practive
	- documentではなくindexを消す
	- shardのサイズは10-65GBにする。
		- 大きすぎるとノードに問題が起きた時のリカバリに時間がかかる
	- GB heap memoryあたり20 shard以下にする
		- ノードが保有できるshardサイズはheapメモリに依存する
		- 30GB heap memoryなら多くても600shard
	- ホットスポットを避ける
		- 一つのノードにたくさんのシャードがあるとホットスポットになる
		- index.routing,allocation.total_shards_per_nodeで1ノードあたりのシャード数を制限できる
	- Shard数が多すぎるクラスタを修正する
		- 長い期間をカバーするtime-based indexを作る
		- 空のindex、不要なindexを消す。shard countを減らす
		- オフピーク時間にindxeをマージする
		- 小さいindexを一緒にする

https://blogs.manageengine.com/application-performance-2/appmanager/2018/10/04/key-metrics-elasticsearch-performance-monitoring.html

- ノード
	- disk IO
	- CPU usage
	- memory usage
	- node health
	- JVM
- インデックスリクエストのパフォーマンス
	- 決まった数のレコードをインデキシングしてindex rateを見る
	- refresh time と merge timeによる影響. refresh timeを減らす(refresh intervalを増やす)、merge timeを速くするのがよい.
	- query latencyの平均、segment timeの平均、file system cache usage、request rate 
- 検索リクエストのパフォーマンス
	- query latencyとrequest rate
	- filter cache
- Network, thread pool
	- ノードはthread poolを使ってCPUとメモリを管理する
	- thrad poolはprocessorの数で決定される
	- thread pool metrics
		- search
		- index
		- merge
		- bulk
	- thread pollの問題は以下を引き起こす
		- 多くのpending requests
		- single slow node
		- thread pool rejection