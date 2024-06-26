# B. Cloud Networking

## B.1 Cloud Network Topology

データエンジニアは、自分が構築したデータシステムに対するクラウドネットワークトポロジの影響を理解しておく必要がある。

クラウドネットワークトポロジーとは、クラウド内のコンポネートがどのように相互接続されているか。クラウドサービス、ネットワーク、ロケーション (ゾーン、リージョン)

### B.1.1 Data Egress Charges

クラウドへのデータ転送は無料だが、クラウドからインターネットへの外向きの転送(データエグレス)には課金している。

### B.1.2 Availability Zones

高いスループットを必要とするワークロードは、1つのゾーン内に配置されたクラスタで実行するべき。

VPC内の通信でもパブリックIPアドレスを使うとデータエグレス料金がかかる。

### B.1.3 Regions

多数のリージョンが提供するので、ユーザは物理的に近い位置にリソースの配置をすると、ネットワーク性能が高くなる。

ゾーン間ネットワークはゾーン内ネットワークより遅く、リージョン間ネットワークは更に遅い。

### B.1.4 GCP-Specific Networking and Multiregional Redundancy

複数のリージョンから構成されるマルチリージョンがある。US, EU, ASIAに分けられ、リージョン障害があってもアクセスできるようになる。GCPでは他のクラウド授業者と比較して遥かに多くのグローバルネットワーク資源を保有している。

### B.1.5 Direct Network Connections to the Clouds

クラウド事業者は、のネットワークとクラウドリージョン内やVPCとを直接統合する、高度なネットワーク接続オプションを提供している。AWS Direct Connectなど。

## B.2 CDNs

CDNを用いると、データ資産を提供する際の性能を劇的に向上させ、大幅な価格低下を実現できる。

同じデータを繰り返し配信する際に効率的に機能する。

CDNはどこでも使えるわけではないので契約書をしっかり読み込もう。

## B.3 The Future of Data Egress Fees

データエグレス料金はデータ移行の大きな妨げになっている。

Zoomはクラウド事業者としてデータエグレス料金が安いOracleを選択した。GCP, AWS, Azureは数年以内にデータエグレス料金を大幅に減額し、クラウドビジネスモデルに大きな転換をもたらすだろう。
