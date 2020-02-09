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
    * ネットワーク内で一意になる名前をつけて識別
    * Microsoftネットワークで採用されているネットワーク用API
    * NetBIOS名によって通信対象のソフトウェアを区別する
* WINSサーバ
    * Windows Internet Naming Service
    *  NetBIOSとIPアドレスの名前解決を行うサーバ
* SMB
    * Server Message Block
    * Windowsのネットワークサービス(ファイル共有やプリンタ共有)を提供するプロトコル
* CIFS
    * Common Internet File System
    * SMBを拡張したプロトコル
    * Windowsで使われるファイル共有プロトコルについて、SMBと呼んだりSMB/CIFSと呼んだりがある

### Sambaサーバの概要

* Server Message Block
* Windowsでのファイル共有の仕組みを実現するソフトウェア
* Sambaの機能
    * ファイルサーバ機能
    * プリントサーバ位機能
    * WINSサーバ機能
    * ドメインコントローラ(Samba4から)
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
        * Winbind機能を提供
    * Samba4がドメインコントローラとして稼働する場合は、起動するプログラムが`samba`というプログラムに統一されている
* Samba4からの機能
    * Active Directoryドメインのドメインコントローラの構築が可能
        * 認証はKerberosを使用
        * DNSによる名前解決
        * LDAPを内蔵
    * ファイルサービスはSMB2だけでなくSMB3にも対応
* Winbind機能
    * Samba側からWindowsユーザの情報を利用可能にする機能
    * WindowsユーザのSID(Security ID)をLinuxユーザのUID・GIDにマッピングする
    * 動作の概要
        * Sambaサーバへアクセスするとき、Windowsクライアントにログオンしたユーザ名、パスワードがSambaに送信される
        * Sambaは認証情報をドメインコントローラに問い合わせ、ドメインコントローラが認証作業を行う
        * 認証に成功すると、SambaはUIDをWinbind機構に問い合わせる。SambaはWinbind機構からNSSを利用してユーザ情報を取得する
        * 作成されたLinuxユーザのUIDを使用して、Sambaの共有上のファイルへアクセスする
* Inhostsファイル
    * NetBIOS名とIPアドレスの対応が記述されているファイル

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

* `workgroup = ワークグループ名`
    * 所属するワークグループを指定
* `server string = 文字列`
    * ブラウジングの際に表示されるサーバの説明
* `netbios name = NetBIOS名`
    * SambaサーバのNetBIOS名を指定
* `browseable = yes | no`
    * ブラウジングの際に表示するかどうかの設定
    * セクション名の末尾に「$」をつけても該当の共有リソースがブラウジングの際に表示されないようにできる
* `wins support = yes | no`
    * SambaサーバをWINSサーバとして動作させるかどうかの設定
* `wins server = IPアドレス`
    * WINSサーバが存在する場合、IPアドレスを指定
* `hide dot files = yes | no`
    * ドットで始まるファイルを隠すかどうかを指定
* `logon script = スクリプトファイル名
    * ログオン後に実行されるスクリプトファイルを指定
* `hosts allow`, `hosts deny`
    * ホストのアクセス制限を行う
    * global以外のセクションにも指定できる

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
    * Saｍba3.0以降、security=userの場合、smbpasswd, tdbsam, ldapsamなどの認証方式を指定して変更
    * 例）
        * `passdb backend = tdbsam:/etc/samba/passdb.tdb`
    * smbpasswd
        * テキスト形式のパスワードファイル。smbpasswdファイル
    * tdbsam
        * バイナリ形式のデータベース。TDBファイル
    * ldapsam
        * LDAPサーバを使用した認証
* `smb passwd file = ファイル名`
    * security=user の場合、smbpasswdで認証を行う際のパスワードファイルの指定
    * 例）
        * `smb passwd file = /etc/samba/smbpasswd`
* `password server = NetBIOS名 | IPアドレス`
    * security = user | domain | ads の場合、認証を別のサーバで行う際のパスワード

ログ設定に関わる設定

* `log file = ファイル名`
    * ログファイルを指定
* `log level = レベル`
    * ログレベルを指定。数字が大きいほど詳細に表示される
* `max log size = サイズ`
    * ログファイルの最大サイズを指定

パスワードに関わる設定

* `username map = ファイル名`
    * ユーザ名のマッピング情報を格納したファイルを指定。WindowsユーザをLinuxユーザにマッピング
    * 書き方は「Linuxユーザ名 = Windowsユーザ名」
* `guest account = ゲストユーザ名`
    * ゲスト認証を許可する場合、ゲストユーザのユーザ名を指定
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

マスターブラウザ関連の設定

* `os level = 数値`
    * ブラウザ選定時に宣言する優先度を指定。数値が大きいほど優先度は高い
* `local master = yes | no`
    * ブラウザ選定に参加するかしないかを指定
* `preferred master = yes | no`
    * ブラウザ選定を要求するかしないかを指定

Winbind関連の設定

* idmapはWindowsユーザとLinuxのユーザのマッピングを行う機能
* `idmap config * : backend = バックエンド`
    * idmap機構で使用するバックエンドの指定。tbd, ldapなど
* `idmap config * : range = 最小UID・GID - 最大UID・GID`
    * Linuxユーザに割り当てるUID・GIDの範囲の指定

### homes, printers, 任意の名前のセクションの設定

全般の設定

* `comment = コメント`
    * ブラウジングした際に表示される説明を指定
* `path = ディレクトリ名`
    * 共有ディレクトリのパスを指定
* `writable = yes | no`
    * 書き込みを許可するかどうかを指定
* `read only = yes | no`
    * 読み取り専用にするかどうかを指定
* `write list = ユーザ名 | @グループ名`
    * writableなどで書き込みが拒否されている場合でも、書き込みができるユーザを指定
* `valid users = ユーザ名 | @グループ名`
    * アクセス可能なユーザを指定
* `guest ok = yes | no` または `public = yes | no`
    * ゲストログインを許可するかどうか
    * yesの場合はパスワード入力なしでゲストとしてログインされる
* `hide files = /ファイル名/`
    * 表示させたくないファイルやディレクトリを指定。アクセスは可能
* `veto files = /ファイル名/`
    * 表示させたくないファイルやディレクトリを指定。アクセスも不可
* `force user = ユーザ名`
    * 認証したユーザに関係なく、共有内では指定したユーザの権限で作業を行わせる
* `force group = ユーザ名`
    * 認証したユーザの所属グループに関係なく、共有内では指定したグループの権限で作業を行わせる

パーミッション関連の設定

* `create mask = 値`
    * ファイルに適用可能なパーミッションを指定
* `force create mode = 値`
    * 必ずファイルに適用されるパーミッションを指定
* `directory mask = 値`
    * ディレクトリに適用可能なパーミッションを指定
* `force directory mode = 値`
    * 必ずディレクトリに適用されるパーミッションを指定

プリンタ設定

* `printable = yes | no` `print ok = yes | no`
    * /etc/samba/smb.conf で共有プリンタの設定を行うための設定

その他

* プリンタドライバは[print$]共有セクションに格納する。[print$]はWindowsで固定

### Active Directoryドメインへの参加設定

* `workgroup = ドメイン名`
    * NetBIOS名の指定(短いドメイン名)
* `realm = レルム名`
    * ドメイン名(レルム名)の指定大文字でFQDNを指定
* `security = ads`
* `server role`
    * Sambaの動作モードを指定
    * `active directory domain controller`(ADドメインのコントローラ)、`standalone`(スタンドアロンサーバ)、`member server`(ドメインのメンバサーバ)などを指定
    * Samba4からの設定

### Samba管理コマンド

* smbstatusコマンド
    * Sambaサーバの状況を書くにするコマンド
    * Sambaサーバに接続されているクライアント、使用中の共有、ロックされているファイルを表示
* nmblookupコマンド
    * マスターブラウザの検索、ワークグループ内のホストの検索、NetBIOS名の問い合わせなどを行うコマンド
    * `nmblookup [オプション] NetBIOS名|IPアドレス|ワークグループ名`
    * `-M` マスターブラウザを調べる
    * `-A` 引数をIPアドレスとみなす
* smbcontrolコマンド
    * デーモンにメッセージを送る
    * 書式
        * `smbcontrol [対象] [メッセージタイプ]`
    * 対象
        * `all`
            * smbd, nmbd, winbinddの全てのプロセスにブロードキャスト
        * `デーモン名` または `PID`
            * いずれか指定されたデーモンのpidファイルに記載されたプロセスID、または直接指定したプロセスIDにメッセージが送られる
    * メッセージタイプ
        * `close-share`
            * 指定した共有をクローズ
            * 対象はsmbdのみ
        * `kill-client-ip`
            * 指定したIPアドレスのクライアントを切断
            * 対象はsmbdのみ
        * `reload-config`
            * 指定したデーモンに設定の再読み込みを指せる
        * `ping`
            * 指定した対象にpingし応答が来た対象のPIDを表示
* smbclientコマンド
    * Windowsネットワーク上の共有リソースにアクセスできるコマンド
    * Samba4ではCIFS(Common Internet File System)を使用してアクセスする。mount.cifsコマンド, mount -t cifs を使用しても同様に
    * `-N` 認証を行わない
    * `-L`,`--list`指定したホストで利用可能な共有リソースを表示
    * `-U`,`--user` 接続するユーザを指定
* samba-toolコマンド
    * Samba4での管理のメインツール。ドメインの管理、DNSの管理、セキュリティ関連の操作などができる
    * 書式
        * `samba-tool サブコマンド`
        * サブコマンド
            * `dns` DNS管理を行う
            * `domain` ドメイン管理を行う
            * `testparm` 設定ファイルの構文チェックを行う
            * `user` ユーザ管理を行う
* smbpasswdコマンド
    * Sambaユーザの管理
    * `smbpasswd [オプション] [Sambaユーザ名]`
    * `-a` ユーザの作成
    * `-d` ユーザの無効化
    * `-e` ユーザの有効化
    * `-x` ユーザの削除
* pdbeditコマンド
    * Sambaのユーザ管理
    * Samba3.0系から使える
    * `-L` ユーザの一覧を表示
    * `-a` ユーザの追加
    * `-x` ユーザの削除
* Sambaユーザを追加する場合は、Linuxシステム上に同名のユーザが存在している必要がある
* net
    * リモートのWindowsマシンを管理できる
    * `net [オプション] [プロトコル] サブコマンド [オプション]`
    * `net ads join`コマンドでActive Directoryドメインに参加
    * `net ads testjoin`コマンドで、Active Directoryドメインに参加しているかどうかを確認することができる
* CIFS Supportが有効になっていれば、mountコマンドを使って共有ディレクトリをLinuxをマウントすることができる。オプションは「-t cifs」を使用する

## NFSサーバの設定

### NFS

* Network File System
* Unix系OSで利用されるファイル共有システム
* デーモン(v3)
    * portmap
        * RPCプログラムとポート番号の対応
        * サーバ側とクライアント側で必要
    * nfsd
        * ファイルシステムのエクスポートやNFSクライアントからのリクエストなどを処理する、NFSの中心となるデーモン
        * 全バージョンのサーバ側で必要
    * mountd
        * NFSクライアントからのマウントおよびアンマウントリクエストを受け付けるデーモン
        * サーバ側で必要
* RPC
    * Remote Procedure Call
    * あるホストの機能をネットワーク上にある別のホストから使えるようにする仕組み
    * /etc/rpc
        * portmapの対応
    * rpcinfoコマンド
        * RPCサービスのプログラム番号、プロトコル、ポート番号などを調べることができる
        * `-p` 指定したホストで動作しているRPCサービスの一覧を表示する
* 疑似ファイルシステムを使って、複数のファイルシステムをクライアント側から1つのツリーとしてマウントできるようになる
    * サーバ側のツリーのルートとなるファイルシステムにfsid=0を設定する
    * ツリーのはいかに見せたい他のファイルシステムをバインドマウントする

### NFSサーバの設定

/etc/exports

* エクスポート(NFSサーバが特定のディレクトリを公開)するディレクトリを記述するファイル
* ファイルの書式
    * `ディレクトリ名 ホスト名（オプション） ホスト名（オプション） ...`
    * すべてのホストを指定する場合は、ホスト名を記載しない、もしくは「*」を使って表すこともできる
    * オプションを指定しない場合は、roとroot_squashがデフォルトで使用される
* オプション
    * `ro`
        * 読み取り専用でエクスポート
    * `rw`
        * 読み書きを許可してエクスポート
    * `no_root_squash`
        * root権限を剥奪しない。rootアクセス時にroot権限で実行
    * `root_squash`
        * root権限を剥奪。アクセス時に匿名アカウント権限で実行
        * デフォルト設定
    * `all_squash`
        * すべてのリクエストを匿名アカウント権限で実行
    * `sync`
        * サーバとクライアント側の書き込みを同期
    * `fsid`
        * fsid=0で、疑似ファイルシステムのルートディレクトリであることを示す
        * file system id

### NFSサーバのコマンド

* exportfsコマンド
    * 現在のエクスポート状況を表示したり、/etc/exportsの変更を反映させるコマンド
    * オプション
        * `-a` 全てのディレクトリをエクスポートまたアンエクスポート
            * export or unexport all directories
        * `-r` すべてのディレクトリを再エクスポート
            * `/etc/init.d/nfs reload`と同じ
            * reexport
        * `-u` ディレクトリをアンエクスポート
            * unexport
        * `-v` 詳細なエクスポート状況を表示。引数を指定しない場合でもエクスポート状況を表示できる
* showmountコマンド
    * マウントしているNFSクライアントを調べるコマンド
    * オプション
        * `-a`,`--all`
            * NFSクライアントのホスト名とマウントされているディレクトリを表示
            * NFSサーバ側で使用するコマンド
        * `-e NFSサーバ名`,`--exports`
            * 指定したサーバでエクスポートしているディレクトリを表示
            * NFSクライアント側で使用するコマンド
* nfsstatコマンド
    * NFSの統計情報を確認するコマンド

### NFSクライアントの設定

* NFSを使ってリモートファイルシステムをマウントするにはmountコマンドを使う
* /etc/fstabファイルでNFSクライアント起動時に自動的にマウント
* ソフトマウント
    * トラブル発生時にタイムアウトしてプログラムを終了
    * retransでタイムアウトまでの再試行回数を指定
* ハードマウント
    * NFSサーバの応答があるまで再試行を続ける
* mountコマンド
    * NFSサーバでエクスポートされているディレクトリをマウントするコマンド
    * `mount [-t nfs] [-o マウントオプション] NFSサーバ名：エクスポートディレクトリ マウントポイント`
    * マウントオプション
        * `nolock`
            * ファイルをロックしない
        * `soft`
            * ソフトマウントする
        * `hard`
            * ハードマウントする
        * `retrans`
            * ソフトマウント時の再試行回数を指定
            * retransmissions
        * `intr`
            * ハードマウント時の割り込みを受け付け
            * interrupt
        * `rsize`
            * 読み取りのブロックサイズを指定
            * read size
        * `wsize`
            * 書き込みのブロックサイズを指定
            * write size
    * NFSサーバnfsserverの/home/shareディレクトリを/mntにソフトマウントする例。再試行回数を5とする
        * `mount -t nfs -o soft,retrans=5 nfsserver:/home/share /mnt`
    * NFSサーバnfsserverの/home/shareディレクトリを/mntにハードマウントする例。割り込みを受け付ける
        * `mount -t nfs -o hard,intr nfsserver:/home/share /mnt`

### NFS v3とNFS v4

* rpcbind
    * NFS v3は必須
    * portmapはIPv6対応となりrpcbindに書き換えられた。
* rfsd
    * すべてのバージョンのサーバ側で必要
* rpc.mounted
    * すべてのバージョンのサーバ側で必要
* rpc.idmapd
    * NFS v4のみ必要
    * /etc/idmapd.confの設定により、クライアントのユーザ名、グループ名とUID,GIDのマッピングを行う

