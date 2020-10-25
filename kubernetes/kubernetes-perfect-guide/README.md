Kubernetes実践ガイド
===

## 第4章 APIリソースとkubectl

### リソース

| カテゴリ | 概要 | リソース | 章 |
| -- | -- | -- | -- |
| Workloads APIs | コンテナの実行に関するリソース | Pod<br>ReplicationController<br>ReplicaSet<br>Deployment<br>DaemonSet<br>StatefulSet<br>Job<br>CronJob | 第5章 |
| Service APIs | コンテナを外部公開するようなエンドポイントを提供するリソース | ClusterIP<br>ExternalIP<br>NodePort<br>LoadBalancer<br>Headless<br>ExternalName<br>None-Selector<br>Ingress | 第6章 |
| Config&Storage APIs | 設定、機密情報、永続化ボリュームなどに関するリソース | Secret<br>ConfigMap<br>PersistentVolumeClaim | 第7章 |
| Cluster APIs | セキュリティやクォータなどに関するリソース | Node<br>Namespace<br>PersistentVolume<br>ResourceQuota<br>ServiceAccount<br>Role<br>ClusterRole<br>RoleBinding<br>ClusterRoleBinding<br>NetworkPolicy | 第8章 |
| Metadata APIs | クラスタ内の他のリソースを操作するためのリソース | LimitRange<br>HorizontalPodAutoscaler<br>PodDisruptionBudget<br>CustomResourceDefinition | 第8章 |

### Namespaceによるクラスタ分離

プロダクション環境、ステージング環境、開発環境はNamespaceではなくクラスタで分離するべき

### CLIツールkubernetes

#### config

kubeconfig(デフォルトは`~/.kube/config`)で接続先クラスタの認証を行う

kubeconfigの例

```
clusters: # 接続先クラスタの情報

users: # 認証情報

contexts: # clustersとusersのペアとnamespaceの指定
```

`kubectl config set-cluster`や`kubectl config set-credentials`、`kubectl config set-context`で設定もできる

#### リソースの作成、削除、更新

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

#### 再起動

Deploymentに紐づくすべてのPodを再起動する
```
$ kubectl rollout restart deployment sample-deployment
deployment.apps/sample-deployment restarted
```

#### アノテーションとラベル

各リソースにメタデータを付与できる。

| 名称 | 概要 |
| -- | -- |
| アノテーション | システムコンポーネントが利用するメタデータ |
| ラベル | リソースの管理に利用するメタデータ |


#### コンテナとローカルマシン間でのファイルコピー

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

#### Podが起動しないときのデバッグ

1. `kubectl logs`コマンドを使ってログを確認する
2. `kubetcl describe`コマンドでEventsの項目を確認する
    * リソースが枯渇していてスケジューリングができない
    * スケジューリングポリシーに該当するノードが存在しない
    * ボリュームのマウントに失敗した 
3. `kubectl run`コマンドを使って実際にコンテナのシェルで確認する

```
kubectl run --image=nginx:1.16 --restart=Never --rm -it sample-debug --command -- /bin/sh
```

