Day13 アプリケーション開発を支援するCodeシリーズ
==

## やること

* Codeシリーズについて
* 使ってみる

## Codeシリーズ

* CodeCommit
    * フルマネージドなソースコード管理サービス、Gitリポジトリをホストすることができる
    * SourceTreeなどのサードパーティ製ツールも利用可能
    * PR機能も利用できる
* CodeBuild
    * フルマネージドなソースコードのビルド、テスティングサービス
    * buildspec.ymlに定義
* CodeDeploy
    * デプロイ自動化サービス
    * S3にあるビルド済みのアーティファクトをEC2やオンプレミスサーバにデプロイする
    * appspec.ymlに定義
    * デプロイ方式を選択可能
    * デプロイ先にエージェントをインストールする必要がある
* CodePipeline
    * 継続的デリバリーをサポートするサービス
    * CodeCommit, CodeBuild, CodeDeployの流れをパイプラインとして定義し、ソースコードがコミットされたらデプロイをまで自動化する
    * 本番デプロイ前に承認するなどのルールを定義することもできる

## CodeCommit

* 利用の流れ
    * CodeCommitのリポジトリを作成する
    * 初期セットアップからコードのpushまで
* 事前にcredentialに登録したIAMユーザ情報を利用してCodeCommitに接続する
    * `git config --global credential.helper '!aws --region ap-northeast-1 codecommit credential-helper $@'`
    * `git config --global credential.UseHttpPath true`

## CodeBuild

* 利用の流れ
    * 成果物格納用のS3バケットを作成する
    * buildspec.ymlを作成する
    * CodeBuildプロジェクトを作成する
    * ビルドを実行する

## CodeDeploy

* 利用の流れ
    * EC2インスタンス用のIAMロールを作成
    * EC2インスタンスにCodeDeployエージェントを導入
    * CodeDeploy用のIAMロールを作成
    * CodeBuildでビルドを実施
    * CodeDeployでデプロイを実施

## CodePipeline

* 利用の流れ
    * CodePipeline用のIAMロールを作成する
    * ソース元の指定(CodeCommit)
    * ビルド方式の指定(CodeBuild)
    * デプロイ方式の指定(CodeDeploy)
