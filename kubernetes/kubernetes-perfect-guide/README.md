Kubernetes実践ガイド
===

# 第4章 APIリソースとkubectl

## リソース

| カテゴリ | 概要 | リソース | 章 |
| -- | -- | -- | -- |
| Workloads APIs | コンテナの実行に関するリソース | Pod<br>ReplicationController<br>ReplicaSet<br>Deployment<br>DaemonSet<br>StatefulSet<br>Job<br>CronJob | 第5章 |
| Service APIs | コンテナを外部公開するようなエンドポイントを提供するリソース | ClusterIP<br>ExternalIP<br>NodePort<br>LoadBalancer<br>Headless<br>ExternalName<br>None-Selector<br>Ingress | 第6章 |
| Config&Storage APIs | 設定、機密情報、永続化ボリュームなどに関するリソース | Secret<br>ConfigMap<br>PersistentVolumeClaim | 第7章 |
| Cluster APIs | セキュリティやクォータなどに関するリソース | Node<br>Namespace<br>PersistentVolume<br>ResourceQuota<br>ServiceAccount<br>Role<br>ClusterRole<br>RoleBinding<br>ClusterRoleBinding<br>NetworkPolicy | 第8章 |
| Metadata APIs | クラスタ内の他のリソースを操作するためのリソース | LimitRange<br>HorizontalPodAutoscaler<br>PodDisruptionBudget<br>CustomResourceDefinition | 第8章 |

## Namespaceによるクラスタ分離

プロダクション環境、ステージング環境、開発環境はNamespaceではなくクラスタで分離するべき

## CLIツールkubernetes

### config

kubeconfig(デフォルトは`~/.kube/config`)で接続先クラスタの認証を行う

kubeconfigの例

```
clusters: # 接続先クラスタの情報

users: # 認証情報

contexts: # clustersとusersのペアとnamespaceの指定
```

`kubectl config set-cluster`や`kubectl config set-credentials`、`kubectl config set-context`で設定もできる

### リソースの作成、削除、更新

作成
```
$ kubectl create -f sample-pod.yaml
pod/sample-pod created
```
更新。変更があればconfigured。applyではyamlを指定すべきではない模様
```
$ kubectl apply -f sample-pod.yaml
Warning: kubectl apply should be used on resource created by either kubectl create --save-config or kubectl apply
pod/sample-pod configured
```

削除
```
$ kubectl delete -f sample-pod.yaml
pod "sample-pod" deleted
```

リソース作成にも`kubectl apply`を使うべき
* 使い分けが不要
* 混在して使用すると、apply実行時に差分を検出できない

### 再起動

Deploymentに紐づくすべてのPodを再起動する
```
$ kubectl rollout restart deployment sample-deployment
deployment.apps/sample-deployment restarted
```

### アノテーションとラベル

各リソースにメタデータを付与できる。

| 名称 | 概要 |
| -- | -- |
| アノテーション | システムコンポーネントが利用するメタデータ |
| ラベル | リソースの管理に利用するメタデータ |


### コンテナとローカルマシン間でのファイルコピー

ローカルマシンからコンテナ

```
$ kubectl cp sample-deployment-7bf986f9cf-dnsnj:etc/hostname ./hostname

$ cat hostname
sample-deployment-7bf986f9cf-dnsnj
```

コンテナからローカルマシン

```
$ k cp hostname sample-deployment-7bf986f9cf-dnsnj:/tmp/newfile

$ k exec -it sample-deployment-7bf986f9cf-dnsnj -- ls /tmp
newfile
```

### Podが起動しないときのデバッグ

1. `kubectl logs`コマンドを使ってログを確認する
2. `kubetcl describe`コマンドでEventsの項目を確認する
    * リソースが枯渇していてスケジューリングができない
    * スケジューリングポリシーに該当するノードが存在しない
    * ボリュームのマウントに失敗した 
3. `kubectl run`コマンドを使って実際にコンテナのシェルで確認する

```
kubectl run --image=nginx:1.16 --restart=Never --rm -it sample-debug --command -- /bin/sh
```

# 第5章 Workloads APIs

リソース

* Pod
* ReplicaController
* ReplicaSet
* Deployment
* DaemonSet
* StatefulSet
* Job
* CronJob

## リソースの関係

Tier3がTier2を管理し、Tier2がTier1を管理する

| Tier1 | Tier2 | Tier3 |
| -- | -- | -- |
| Pod | ReplicationController | |
| Pod | ReplicaSet | Deployment |
| Pod | DaemonSet | |
| Pod | StatefulSet | |
| Pod | Job | CronJob |

## Pod

Workloadsリソースの最小単位。

1つ以上のコンテナから構成される。多くの場合は1つのPodに1つのコンテナ

Pod単位でIPアドレスが割り当てられる

### Podのデザインパターン

|  | 役割 | 例 |
| -- | -- | -- |
| サイドカーパターン | メインコンテナに機能を追加する | ・特定の変更を検知した際に動的に設定を変更するコンテナ<br>・Gitリポジトリとローカルストレージを同期するコンテナ<br>・アプリケーションのログファイルをオブジェクトストレージに転送するコンテナ |
| アンバサダーパターン | 外部システムとのやり取りの代理を行う | ・メインコンテナがlocalhostを指定してDBへ接続(環境差を考慮) |
| アダプタパターン | 外部からのアクセスのインターフェースとなる | ・Prometheus exporter|

## Deployment

複数のReplicaSetを管理することで、ローリングアップデートやロールバックなどを実現するリソース

### アップデート戦略

#### Recreate

一度すべてのPodを削除してから再度Podを作成。早い。余分なリソースを使用しない。ダウンタイムあり

#### RollingUpdate

ダウンタイム無しでアップデート

余分なリソースを使用しないように不足Pod数や超過Pod数を指定できる

アップデートパラメータ。`spec`以下で設定する[]

| 設定 | 説明 |
| -- | -- |
| `minReadySeconds` | PodがReady状態になってから次のPodの入れ替えが可能と判断するまでの秒数  |
| `revisionHistoryLimit` | Deploymentが保持するReplicaSetの数。ロールバック可能な履歴数  |
| `progressDeadlineSeconds` | Recreate/RollingUpdate処理のタイムアウト時間。超過した場合は自動でロールバック  |

## Job

コンテナを利用して一度限りの処理を実行させるリソース

ReplicaSetとの違いは、起動するPodを停止することを前提にして作られているかどうか。Podの停止が正常終了と期待される用途

## CronJob

Jobを管理するリソース

Kubernetes1.4まではScheduledJobという名称だった。

# 第7章 Config&Storage APIs

リソース

* Secret
* ConfigMap
* PersistentColumeClaim

## Kubernetesの環境変数

以下で観光変数を埋め込むことが可能

* 静的設定
* Podの情報
* コンテナの情報
* Secretリソースの機密情報
* ConfigMapリソースの設定値

### 静的設定

`spec.containers[].env`に指定

### Podの情報

`fieldRef`で参照できる

### コンテナの情報

`resourceFieldRef`で参照できる

## Secret

ユーザ名やパスワードなどの機密情報をコンテナに渡す。

Secretの分類

| type | 概要 |
| -- | -- |
| `Opaque` | 一般的な汎用用途 |
| `kubernetes.io/tls` | TLS証明書用 |
| `kubernetes.io/basic-auth` | Basic認証用 |
| `kubernetes.io/dockerconfigjson` | Dockerレジストリの認証情報用 |
| `kubernetes.io/ssh-auth` | SSHの認証情報用 |
| `kubernetes.io/service-account-token` | Service Accountのトークン用 |
| `bootstrap.kubernetes.io/token` | Bootstrapトークン用 |

### 一般的な汎用用途のSecret (Opaque)

作成方法のパターン

* kubectlでファイルから値を参照して作成する(--from-file)
* kubectlでenvfileから値を参照して作成する(--from-env-file)
* kubectlで直接値を渡して作成する(--from-literal)
* マニフェストから作成する(-f)

#### ファイルから値を参照して作成(--from-file)

base64でエンコードされて保存される

```
$ echo -n "root" > ./username
$ echo -n "rootpassword" > ./password
$ kubectl create secret generic --save-config sample-db-auth --from-file=./username --from-file=./password
secret/sample-db-auth created

$ kubectl get secret sample-db-auth -o json | jq .data
{
  "password": "cm9vdHBhc3N3b3Jk",
  "username": "cm9vdA=="
}
```

### Secretの利用

* 環境変数として渡す
* Volumeとしてマウントする

#### 環境変数として渡す

特定のkeyを渡す場合は、`spec.containers[].env`の`valueFrom.secretKeyRef`を使って指定する

すべてのkeyを渡す場合は、`spec.containers[].envFrom`の`secretRef`を使って指定

#### Volumeとしてマウントする

特定のkeyをマウントする場合は、`spec.volumes[]`の`secret.items[]`を使って指定する

すべてのkeyをマウントする場合は、`spec.volumes[]`の`secret.secretName`を使って指定

## ConfigMap

設定情報などのKey-Valueで保持できるデータを保存しておくリソース

### ConfigMapの作成

* kubectlでファイルから値を参照して作成する(--from-file)
* kubectlで直接値を渡して作成する(--from-literal)
* マニフェストから作成する(-f)

### ConfigMapの利用