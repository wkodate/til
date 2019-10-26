Day2 EC2を使ってサーバを立てる
==

## やること

* EC2について
* EC2インスタンスに接続しミドルウェアを導入する
* Amazon Machine Image(AMI)を取得する
* Elastic IP Address(EIP)を用いてIPアドレスを固定する

## EC2について

* サーバを利用できるサービス
* OSより上のレイヤは自由に設定可能
* EC2の料金表
    * https://aws.amazon.com/jp/ec2/pricing/on-demand/
* EC2を使う上でのポイント
    * OSイメージ
    * インスタンスタイプ
    * ストレージ
    * セキュリティグループ(ファイアウォール)
    * SSHのキーペア
* 作成したインスタンスは、秘密鍵pemを指定して、ec2-userユーザでIPv4パブリックのIPに対してsshしてログインする

## AMIを取得

* AMIはEC2のある断面、EC2インスタンスを作る元になる
* スナップショットはディスクの断面になるのでEBSを作る元になる
* インスタンスを停止させてからAMIを作成、これを利用してインスタンスを生成できる

## IPアドレスを固定

* 再起動した場合にIPアドレスが変わってしまうのでElastic IPを使う
* 実行中のインスタンスと関連付けられていないElastic IPはお金がかかるので注意
* https://aws.amazon.com/jp/premiumsupport/knowledge-center/elastic-ip-charges/
    * 次の条件が満たされている限り、Elastic IP アドレスに料金は発生しません。
        * Elastic IP アドレスが EC2 インスタンスに関連付けられている。
        * Elastic IP アドレスに関連付けられているインスタンスが実行中である。
        * インスタンスに 1 つの Elastic IP アドレスしか添付されていない。
