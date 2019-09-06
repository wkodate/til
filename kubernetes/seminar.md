# はじめに

### なぜコンテナか

* プロセスを起動するだけなので軽量、起動が早い
* 同じ環境を再現できる
* devとopsの分離。devがイメージを作ってopsがデプロイ
* ライブラリのバージョン依存を気にしなくてよくなる。言語ごとに別のコンテナでデプロイできるため

### サービス公開の流れ

* コンテナ化したアプリケーションを作成(Docker)
* コンテナ化したアプリケーションをKubernetesにデプロイ
* Ingress, Serviceでサービスを外部公開

# Docker演習

* docker daemonが常駐プロセスとしてコンテナの実行を管理している

### コンテナの実行

* `docker pull` イメージを取得
* `docker run` 実行
* `docker ps` コンテナ状態の確認
* `docker stop` コンテナを停止

### イメージの作成
* `docker build` Dockerfileからイメージをビルド
* `docker image` ビルドされたことを確認
* RUN, COPY, ADD を使うとレイヤが増える、レイヤが増えるとイメージサイズを増えてしまうので注意
* 可能な限りRUNをまとめる
* マルチステージビルドは、ビルドを複数回に分けて実行する。ステージを分けることで、アプリの実行に必要な環境だけを含むイメージを作成できる

### レジストリへの登録

* `docker tag` タグを切る
* `docker push` レジストリへの登録
* Dockerfileのベストプラクティス
  * https://docs.docker.com/develop/develop-images/dockerfile_best-practices/

# Kubernetes演習

* kubetenesはコンテナアプリケーションのオーケストレーションツール
* kubeクラスタは管理者がkubectlを使ってクラスタを操作する
* リソースには、Pod、ReplicaSet、Deployment、Namespace、Service、Ingressなどがある
* Namespaceはクラスタ内での分割単位

### Pod

* `kubectl create` Podのデプロイ
* `kubctl get pods` podの一覧表示、kubectl describe で詳細を表示
* `kubectl logs` ログを参照。標準出力と標準エラー出力
* `kubectl delete` 削除

### Deployment

* DeploymentはReplicaSetを管理する、ReplicaSetはPodの数を管理する
  * Deployment - ReplicaSet - Pod
* `kubectl apply` Deploymentのデプロイ
* `kubectl diff` デプロイ済みのDeploymentと変更したマニフェストファイルの差分を確認できる
* `kubectl rollout` ロールバックできるけど、gitでマニフェストファイルを戻して再度applyする方法が推奨されている

### Ingress, Service
* ユーザ→Ingress→Service→Pod
* Ingressはバーチャルホストやパスによって振り分けるL7ロードバランサ
* ServiceはPodにアクセスを振り分けるL4ロードバランサ。VIPの役割
* ServiceTypeで外部への公開方法を指定。
* サービス名が自動でDNSに登録される
* kubectlの便利な補完機能
    * https://kubernetes.io/docs/tasks/tools/install-kubectl/#enabling-shell-autocompletion
