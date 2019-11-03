Day10 CLIによるAWSの操作とシステム監視
==

## やること

* AWS CLIについて
* システム監視について

## AWS CLIについて

* コンソールからコマンドで操作できる
* `aws help` でコマンドを確認できる
* `--profile アカウント` でそのユーザ権限でコマンドを実行
* CLIからS3にバケットを作成

参照

```
% aws s3 ls
```

バケットを作成

```
% aws s3 mb s3://wkodate-udemy-aws-cli
make_bucket: wkodate-udemy-aws-cli

% aws s3 ls
2019-11-03 09:50:47 wkodate-udemy-aws-cli
```

ファイルをS3にアップロード

```
% aws s3 cp udemy-aws-14days/README.md s3://wkodate-udemy-aws-cli
upload: udemy-aws-14days/README.md to s3://wkodate-udemy-aws-cli/README.md

% aws s3 ls s3://wkodate-udemy-aws-cli
2019-11-03 09:52:24        853 README.md
```

ファイルを削除

```
% aws s3 rm s3://wkodate-udemy-aws-cli --recursive
delete: s3://wkodate-udemy-aws-cli/day1.md
delete: s3://wkodate-udemy-aws-cli/day2.md
delete: s3://wkodate-udemy-aws-cli/day4.md
delete: s3://wkodate-udemy-aws-cli/day6.md
delete: s3://wkodate-udemy-aws-cli/day3.md
delete: s3://wkodate-udemy-aws-cli/day7.md
delete: s3://wkodate-udemy-aws-cli/day5.md

% aws s3 ls s3://wkodate-udemy-aws-cli
%
```

## システム監視について

* CloudWatchは運用監視のマネージドサービス
* CloudWatchからSNSと呼ばれるPub/Sub メッセージングサービスに送られる
    * それぞれの要素を疎結合にできる
* 独自定義するカスタムメトリクスだとお金がかかる
* CloudWatchの設定
    * CloudWatchでメトリクスとしきい値を設定
    * SNSの通知先を設定
