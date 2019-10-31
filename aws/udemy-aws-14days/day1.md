Day1 AWSことはじめ
==

## やること

* AWSアカウントの作成
* アカウントを作ったらやること
    * 作業用のIAM(アイアム)ユーザを作成する
    * Cloud Trailを設定する
    * 料金アラートを設定する
* AWS料金の見積もりについて

## 作業用のIAMユーザを作成

*  ルートユーザ
    * 特権ユーザ
    * アカウントの設定変更やサポートプランを変個数る
* IAMユーザ
    * IAMポリシーで許可されたAWSサービスを利用者ごとに払い出し
    * 基本的な操作はこっちでやる
    * AdministratorAccess権限で作れば大抵のことができる

## CloudTrailを設定する

* CloudTrailはAWSの操作ログを自動取得するサービス
* デフォルトで90日のログを取得、S3で永続化できる

## 料金アラートを設定する

* CloudWatchで設定
* しきい値とメールアドレスを指定する
* 料金アラート
    * 料金はAWSの公式ページを参考にする。「AWS (サービス名) pricing」で検索する
    * Simple Monthly Calculator
        * https://calculator.s3.amazonaws.com/index.html
