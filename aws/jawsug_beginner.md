# To Learn AWS is

https://speakerdeck.com/shogomuranushi/deng-tan-zi-liao-jaws-ug-chu-xin-zhe-zhi-bu-20190417

* 主にAWSの勉強法
* [最短突破　AWS認定ソリューションアーキテクト　アソシエイト　合格教本](https://www.amazon.co.jp/dp/B07NXTK26V) の著者
* なぜ学ぶのか
    * 一人でできることが増える
    * 少ないリソースで高い価値を出せるか給料増える
* 新しい概念に触れやすい
    * まず触る。本番環境でやることが多くの学びがある
* 触ったあとに認定資格を学ぶ
* エンジニアは以下の認定資格を受けるのが良い
    * ソリューションアーキテクトアソシエイト
    * デベロッパーアソシエイト
    * SysOpsアソシエイト
* なぜAWS認定資格を取るのか
    * 体系的に学べる
    * 世界で通用する技術
    * 取得したい資格ランキング
* 勉強方法
    * AWS InnovationのWell-Architected
    * 試験対策本を3冊とも買う
* [AWS Well Architected](https://www.slideshare.net/AmazonWebServicesJapan/20180807-aws-black-belt-online-seminar-aws-wellarchitected-framework) をベースに試験が作られている
* AWS Innovate
    * AWS初心者向けコンテンツ
    * 認定試験対策講座
    * AWS最新情報

# AWSの学び方

https://speakerdeck.com/takuros/jawsugchu-xin-zhe-zhi-bu-awsfalsemian-qiang-falseshi-fang

* アメリカだとプロフェッショナルレベルを持っていると年収2000万
* AWSが提供している教材
    * AWS公式サイト。分量が多いので概要。新しいサービスは英語
    * BlackBeltシリーズ。おすすめ
    * オンラインセミナー
* developers.ioはaws情報の更新が早い
* 技術書は気になったところだけ読む。あとから参照できるようにしておく
* 題材がないときは[10分間チュートリアル](https://aws.amazon.com/jp/getting-started/tutorials/)をやってみる
* AWS試験の受かり方として、AWS Well-Architetedの考え方に沿って回答する

# AWS NightSchool

* VPC、IAM、AutoScaling
* VPC(Virtual Private Control): AWS上でネットワークを構築
    * webサーバはpublicサブネット、DBはprivateサブネット
    * web層、ap層、db層でセキュリティグループを設定できる
    * VPCは外との通信、IAMはAWS内の通信
* IAM (あいあむと読む)
    * IAMユーザ、IAMロールに対してIAMポリシーで権限を制御
    * IAMのAPIがある
    * denyが優先される
* AutoScaling
    * CouldWatchがとれるメトリクスはハイパーバイザーが取れるメトリクスと同じ
    * Autoscalingの起動停止設定を細かくしすぎると料金が高くつくこともある
* 公式ドキュメントを読む。基本コンセプトを理解したいならイベントに参加する。
    * はじめてのAWS
    * AWS Loft TokyoやJAWSなどのハンズオンイベント
