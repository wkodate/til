Directory layout
===

https://docs.ansible.com/ansible/latest/user_guide/playbooks_best_practices.html#directory-layout

## ディレクトリ

トップにサービス毎のplaybookがある

* inventories/
    * 環境毎(dev, productionなど)のサーバのホストとグループをまとめる
    * hosts.ini
        * ホスト情報
    * group_vars/
        * グループ毎の変数情報
* tasks/
    * 共通のタスク。アカウント作成やデーモンインストールなど
* roles/
    * ロール毎の設定。ロールはサービスもしくはその中で分かれるくらいの単位
    * defaults/
        * デフォルトの優先度低めの変数。group_varsで設定すれば上書きされる
    * files/
        * スクリプトファイル。必要があれば
    * meta/
        * 依存するroleの情報。javaのロールとかcommonのロールとかの記述がある
    * tasks/
        * タスクが書かれたファイル
        * カーネルパラメーータの設定、インストール、ディレクトリ作成などの初期セットアップなど
    * templates/
        * テンプレートリソースが置かれるディレクトリ。taskで使われる
        * 拡張子は.j2 
    * vars/
        * ロールに対応する変数
        * group_varsの環境毎の変数も記載されている
