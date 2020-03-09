Flink Training
===

## transformation

* mapは1対1の変換、flatmapは1対0-nの変換
* keyedByはキー毎にデータを集約。
* keydStreamでmaxなどの集約関数やreduceなどのカスタムaggregationが使える
* RichFlatMapFunction
    * FilterFunction, MapFunction, FlatMapFunctionなどのインターフェース
    * open(Configuration c)
        * オペレータの初期化で一度だけ呼ばれる
    * close()
        * 同様に最後に呼ばれる
    * getRuntimeContext()
        * stateの作成やアクセスなど
* keyedState
    * keyed streamで動いているとき、Flinkはstateでkey/valueストアを保持する
    * ValueState
        * シンプルなkeyed state。各keyに対してひとつのオブジェクトを保持する。そのkeyの平均値など
    * ListState,MapStateなどがある
* Connected Streams
    * ストリームのジョイン。同じkeyでジョインされる
    * RichCoFlatMapFunction
        * FlatMapFunctionのひとつ
        * flatMap1
            * connect元のstream
        * flatMap2
            * connect対象のstream
