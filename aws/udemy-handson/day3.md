# Day3 VPCを使ってネットワークを構築する(1)

## やること

* AWSのネットワーク
* 実際に構築

## AWSのネットワーク

* AZは東京などのリージョンの中で選べるいくつかの物理的な場所
* リージョン
    * 東京とかアメリカの都市とかのデータセンター
* AZ
    * 物理的に分離されたネットワーク
* VPC(Virtual Private Cloud)
    * AWSで作成するネットワーク
    * AZをまたがって作ることができる
* サブネットはAZに紐付いて作られる。マルチAZで作る
* CIDRで5つのアドレスは予約されている。/28なら2^(32-28)=2^4=16個のうち11個使える
* Internet GWからインターネットに出る、パブリックサブネットから出るときはNAT GWから外に出る
* VPCのサービスを理解していないとサービス設計に失敗する

## 構築

* VPCの作成し、Publicサブネットを追加する
* Apache, PHPをインストールしてGitHubのコードを実行
* クラウド活用資料集
    * https://www.slideshare.net/AmazonWebServicesJapan/20190313-aws-black-belt-online-seminar-amazon-vpc-basic
