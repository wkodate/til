ファイル共有
===

## ポイント

### 理解しておきたい用語と概念

* Sambaの仕組みとsmb.conf
* Sambaの構成デーモン
* ワークグループ
* ドメインコントローラ
* マスターブラウザ
* NFSの仕組みと/etc/exports
* RPCサービス

### 習得しておきたい技術

* Sambaの設定
* Sambaクライアントコマンドの操作
* NFSの利用
* NFS特有のマウントオプションの利用
* portmapのアクセス制御

## Sambaサーバの設定

### Microsoftネットワーク

* ワークグループ
    * Microsoftネットワークの中のコンピュータのグループの単位
* マスターブラウザ
    * ネットワークのホストのリストを管理しているホスト
* ドメインコントローラ
    * ドメインを管理する認証サーバ
    * NTドメイン
        * Windows NTで利用されるユーザ管理の仕組み
    * ADドメイン
        * Active Directory Domain
        * Windows 2000 Server以降に採用されているディレクトリサービス
        * 認証にKerberosを使用
* NetBIOS
    * ネットワーク用API
* SMB
    * Server Message Block
    * Windowsのネットワークサービス(ファイル共有やプリンタ共有)を提供するプロトコル
* CIFS
    * Common Internet File System
    * SMBを拡張したプロトコル
* WINSサーバ
    * Windows Internet Naming Service
    *  NetBIOSとIPアドレスの名前解決を行うサーバ

### Sambaサーバの概要

* Server Message Block
* Windowsでのファイル共有の仕組みを実現するソフトウェア
* Sambaの機能
    * ファイルサーバ機能
    * プリントサーバ位機能
    * WINSサーバ機能
    * ドメインコントローラ
    * ADメンバーサーバ
    * Microsoftネットワーククライアント
* Sambaのサーバプロセス
    * smbd
        * ファイルやプリンタの共有、ユーザに対する認証
        * 139/tcp, 445/tcp
    * nmbd
        * ブラウズ機能の提供とNetBIOSによる名前解決
        * 137/udp, 138/udp
    * winbindd
        * Winbind機能
            * Samba側からWindowsユーザの情報を利用可能にする機能
            * WindowsユーザのSID(Security ID)をLinuxユーザのUID・GIDにマッピングする

### Sambaサーバの設定

/etc/samba/smb.conf

* smb.confは全体設定と共有定義から構成されている。
* global, homes, printersの3つのセクションは特別なセクションであり、それ以外は任意の名前でセクションを作成できる
* globalセクション
    * Samba全般の設定
    * 設定変更後にサーバの再起動が必要
* homesセクション
    * UNIXユーザの各ホームディレクトリを一括して共有
* printersセクション
    * 共有プリンタに関する設定
* 任意の名前を指定して個々の共有設定ができる
* testparmコマンド
    * `-s` 構文をチェックした後に内容を自動的に表示
    * `-v` 構文チェック後にsbm.confに記述されていないパラメータも表示

### globalセクションの設定

全般設定

* `server string = 文字列`
    * ブラウジングの際に表示されるサーバの説明
* `browseable = yes | no`
    * ブラウジングの際に表示するかどうかの設定
    * セクション名の末尾に「$」をつけても該当の共有リソースがブラウンジグの際に表示されないようにできる
* `wins support = yes | no`
    * SambaサーバをWINSサーバとして動作させるかどうかの設定
* `wins server = IPアドレス`
    * WINSサーバが存在する場合、IPアドレスを指定
* `hide dot files = yes | no`
    * ドットで始まるファイルを隠すかどうかを指定

認証設定に関わる設定

* `security = user | domain | ads`
    * 認証方法を設定する
    * domain NTドメインにおいて、ドメインコントローラを使用し認証
    * `user`
        * ユーザレベルのセキュリティ
    * `domain`
        * ドメインコントローラでの認証
    * `ads`
        * Active Directoryでの認証
* `passdb backend = 認証方式`
    * security=userの場合smbpasswd, tdbsam, ldapsamなどの認証方式を指定
    * smbpasswd
        * テキスト形式のパスワードファイル。smbpasswdファイル
        * smbpasswdコマンド
            * `smbpasswd [オプション] [Sambaユーザ名]`
            * `-a` Sambaユーザの作成
            * `-d` Sambaユーザの無効化
    * tdbsam
        * バイナリ形式のデータベース。TDBファイル
    * ldapsam
        * LDAPサーバを使用した認証
* `password server = NetBIOS名 | IPアドレス`
    * security = user | domain | ads の場合、認証を別のサーバで行う際のパスワード

ログ設定に関わる設定

* `log file = ファイル名`
    * ログファイルを指定
* `max log size = サイズ`
    * ログファイルの最大サイズを指定

パスワードに関わる設定

* `map to guest = Never | Bad User | Bad Password`
    * Sambaユーザとして認証できなかった場合の対応を指定
    * `Never`
        * ゲスト認証を許可しない
    * `Bad User`
        * 存在しないユーザの場合はゲスト認証とする
    * `Bad Password`
        * パスワードミスの場合もゲスト認証とする
* `encrypt passwords = yes | no`
    * 暗号化されたパスワードを使用するかどうかを指定
* `unix password sync = yes | no`
    * LinuxとSambaのパスワードを同期させるかどうかを指定
* `passwd program = プログラム名`
    * Samba側でパスワードを変更した際に実行するプログラム
* `passwd chat = 文字列`
    * パスワードプログラム実行時の応答内容を指定
* `null passwords = yes | no`
    * 空のパスワードを許可するかどうかを指定

Windbind関連の設定

* `idmap config * : backend = バックエンド`
    * idmap機構で使用するバックエンドの指定。tbd, ldapなど

### homes, printers, 任意の名前のセクションの設定

全般の設定

* `path = ディレクトリ名`
    * 共有ディレクトリのパスを指定
* `writable = yes | no`
    * 書き込みを許可するかどうかを指定
* `read only = yes | no`
    * 読み取り専用にするかどうかを指定
* `valid users = ユーザ名 | @グループ名`
    * アクセス可能なユーザを指定
* `guest ok = yes | no` または `public = yes | no`
    * ゲストログインを許可するかどうか
* `hide files = /ファイル名/`
    * 表示させたくないファイルやディレクトリを指定。アクセスは可能
* `veto files = /ファイル名/`
    * 表示させたくないファイルやディレクトリを指定。アクセスも不可

パーミッション関連の設定

* `directory mask = 値`
    * ディレクトリに適用可能なパーミッションを指定

### Samba管理コマンド

* smbstatusコマンド
    * Sambaサーバの状況を書くにするコマンド
    * Sambaサーバに接続されているクライアント、使用中の共有、ロックされているファイルを表示
* nmblookupコマンド
    * マスターブラウザの検索、ワークグループ内のホストの検索、NetBIOS名の問い合わせなどを行うコマンド
    * `-M` マスターブラウザを調べる
    * `-A` 引数をIPアドレスとみなす
* smbcontrolコマンド
    * デーモンにメッセージを送る
    * 書式
        * `smbcontrol [対象] [メッセージタイプ]`
    * 対象
        * all
            * smbd, nmbd, winbinddの全てのプロセスにブロードキャスト
    * メッセージタイプ
        * close-share
            * 指定した共有をクローズ
            * 対象はsmbdのみ
        * kill-client-ip
            * 指定したIPアドレスのクライアントを切断
            * 対象はsmbdのみ
        * reload-config
            * 指定したデーモンに設定の再読み込みを指せる
        * ping
            * 指定した対象にpingし応答が来た対象のPIDを表示
* smbclientコマンド
    * Windowsネットワーク上の共有リソースにアクセスできるコマンド
    * `-N` 認証を行わない
* samba-toolコマンド
    * Samba4での管理のメインツール。ドメインの管理、DNSの管理、セキュリティ関連の操作などができる
    * 書式
        * `samba-tool サブコマンド`
        * `domain` ドメイン管理を行う
* CIFS Supportが有効になっていれば、mountコマンドを使って共有ディレクトリをLinuxをマウントすることができる。オプションは「-t cifs」を使用する

## NFSサーバの設定

### NFS

* Network File System
* Unix系OSで利用されるファイル共有システム
* クライアント側にportmap、サーバ側にportmap、nfsd、mountedが必要
* RPC
    * Remote Procedure Call
    * あるホストの機能をネットワーク上にある別のホストから使えるようにする仕組み
    * /etc/rpc
        * portmap(サービスとRPCプログラム番号)の対応

### 設定

* /etc/exports
    * エクスポート(NFSサーバが特定のディレクトリを公開)するディレクトリを記述するファイル
    * exportfsコマンドで/etc/exportsの変更を反映
    * オプション
        * no_root_squash
            * root権限を剥奪しない。rootアクセス時にroot権限で実行
        * root_squash
            * root権限を剥奪。アクセス時に匿名アカウント権限で実行
            * デフォルト設定
* NFSクライアント
    * NFSを使ってリモートファイルシステムをマウントするにはmountコマンドを使う
    * マウントオプション
