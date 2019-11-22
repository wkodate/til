Issue286
==

# Apache Hadoop Ozone – Object Store Architecture

開発者向けのHadoop Ozoneの記事。主にシステムアーキテクチャについて。

* Hadoop Ozoneは分散KVSあり、大きさの異なるファイルでも効率的に管理できるストレージで猿。S3のようなオブジェクトストレージをHadoopで実現する
* OzoneのIntroductionの記事
    * [Introducing Ozone](https://jp.hortonworks.com/blog/introducing-apache-hadoop-ozone-object-store-apache-hadoop/)
    * [Ozone Overview](https://jp.hortonworks.com/blog/apache-hadoop-ozone-object-store-overview/)
* Ozoneをスケールさせるためには、namespaceとblock mapがボトルネックになっている
* Ozoneを構成するコンポーネント
    * Storage Container
        * KVS(RocksDB)でレプリケーションの単位
    * RAFT consensus protocol
        * 分散コンセンサスプロトコル
    * Storage Container Manager(SCM)
        * Container管理のサービス
    * Hadoop Distributed Data Store(HDDS)
        * 分散ストレージ層
    * Ozone Manager(OM)
        * namespaceを管理

## Putting it Together
1. Storage Container
    * blockのkey-valueでRocksDBに保存
2. RAFT Replication
    * PutBlockでContainerをネットワークをまたいでレプリケーションする
    * 各Containerは3つのレプリカを保持。RAFT leaderがfollowerにレプリケートする
    * ContainerレプリカはDataNodeに保存される
3. Storage Container Manager(SCM)
    * DataNodeから定期的にレポートを受け取り、SCMでContainerがクラスタのどこに保存されているかをセントラライズする
4. Containers + RAFT + SCM = HDDS
    * 上記3つを使って分散ブロックストレージ層を作成
5. Adding a Namespace - Ozone Manager
    * キー名から対応するブロックをマッピング
