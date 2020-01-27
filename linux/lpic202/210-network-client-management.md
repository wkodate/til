ネットワーククライアント管理
===

## ポイント

#### 理解しておきたい用語と概念

* DHCP
* リレーエージェント
* PAM認証
* LDAP
* 識別名
* LDIF
* SSSD

#### 習得しておきたい技術

* dhcpdの設定
* PAMの設定
* LDAPサーバの構築
* LDAPクライアントコマンド

## DHCPの設定

### DHCPの仕組み

* Dynamic Host Configuration Protocol
* サーバがプールしているIPアドレスを自動的にクライアントに割り当てる
* 流れ
    * DHCP Discover
        * ブロードキャストUDPパケットを創出しネットワーク情報を要求
    * DHCP Offer
        * プールにあるIPアドレスをクライアントに提案
    * DHCP Request
        * IPアドレスを使いたい旨をリクエスト
    * DHCP Ack
        * 承諾メッセージを送る
* DHCPサーバがクライアントに割り当てる情報
    * IPアドレス
    * ドメイン名
    * DNSサーバのアドレス
    * デフォルトゲートウェイ
    * サブネットマスク
    * ブロードキャストアドレス
    * NTPサーバのアドレス
    * NISドメイン名
    * NISサーバのアドレス
    * WINSサーバのアドレス
* DHCPリレーエージェント
    * 異なるネットワーク感で同一のDHCPを利用するためのプログラム
    * DHCPはブロードキャストを使ってサービスを提供するので、ルータを隔てたネットワークに対してサービスを提供できない
* DHCPv6
    * ステートレス自動設定
    * ステートフル自動設定
    * DHCPv6(DHCPv4との互換なし)
* LPICでは、標準的なDHCPサーバである ISC DHCP について問われる
    * Internet Software Consortium

### DHCPサーバの設定

* dhcpd
    * DHCPサーバのデーモン
* /etc/dhcpd.conf
    * dhcpdの設定ファイル
* リース期間
    * IPアドレスをクライアントに貸し出す期間
    * dhcpdが現在貸し出しているIPアドレスは、dhcpd.leasesファイルに記録される

### /etc/dhcpd.confの設定項目

* `option routers IPアドレス;`
    * デフォルトゲートウェイのIPアドレスを指定
* `option subnet-mask APアドレス;`
    * サブネットマスクを指定
* `option broadcast-address IPアドレス;`
    * ブロードキャストアドレスを指定
* `option ntp-servers IPアドレス;`
    * NTPサーバのIPアドレスを指定
* `option netbios-name-servers IPアドレス`
    * WINSサーバのIPアドレスを指定
* `max-lease-time 秒;`
    * 最大の貸し出し期限を指定
* `fixed-address IPアドレス;`
    * クライアントに割り当てる固定IPアドレスを指定
* `hardware インターフェースタイプ MACアドレス;`
    * クライアントを特定するためのMACアドレスを指定

### DHCPクライアントの設定

* DHCPクライアント
    * dhclient
    * pump
    * dhcpcd

## PAM認証

### PAM
* Pluggable Authentication Modules
* UNIXで認証情報を一元管理する仕組み
* プログラムはユーザ情報が/etc/passwdにあるのか、他のファイルにあるのか、別のホストにあるのか、を意識しなくてよくなる
* 認証方式は設定ファイルを編集するだけで変更可能

### 設定 /etc/pam.dディレクトリ

* PAMの設定ファイルがあるディレクトリ
* /etc/pam.confと両方存在する場合は、/etc/pam.confファイルは無視される
* ディレクトリ以下にプログラム名と同じファイル名のファイルが置かれている
* 設定ファイルの書式
    * `モジュールタイプ コントロール モジュールのパス 引数`
* モジュールタイプ
    * その行に記述されているモジュールに何をさせるのか、どのようなタイプの認証を行うのかを指定
    * `auth`
        * ユーザ認証を行う
    * `account`
        * ユーザ認証ができるか確認する
    * `password`
        * パスワードの設定と変更
    * `session`
        * ユーザ認証の前後に実行すべき処理を指定(ログの記録など)
* コントロール
    * 認証が成功、失敗したときにどうするかを指定する
    * `requisite`
        * モジュールの実行に失敗した場合は、すぐに認証を拒否してそれ以降のモジュールを実行しない
    * `required`
        * モジュールの実行に失敗した場合は、以降の同じタイプのモジュールを実行してから認証を拒否する
    * `sufficient`
        * モジュールの実行に成功した場合は、以前の行のrequiredのモジュールの実行が全て成功していればその時点で認証を許可する
        * 失敗した場合は、引き続き以降の行のモジュールを実行する
    * `optional`
        * モジュールの実行に成功しても失敗しても、以降の行にあるモジュールの実行を続ける
* モジュール
    * `pam_env.so`
        * 環境変数を設定
    * `pam_limits.so`
        * ユーザが利用するリソースを制限
    * `pam_listfile.so`
        * 任意のファイルを用いてプログラムのアクセス制御を行う
    * `pam_securetty.so`
        * rootでのログインを/etc/securetty ファイルに記述された端末にのみ許可
    * `pam_sss.so`
        * SSSDを使った認証を行う
    * `pam_unix.so`
        * 通常のパスワード認証を行う
        * 引数にnullokを指定すると空のパスワードを許可する


## LDAPクライアントの利用方法

### LDAPの仕組み

* Lightweight Directory Access Protocol
* ディレクトリサービスの提供者と利用者の間での情報をやり取りするプロトコル
    * ディレクトリサービスは、各種リソースの名前とその属性をツリー状に系統的にまとめて管理するサービス
    * ユーザやリソースに関する情報を検索したり管理したりすることができる
* ディレクトリ情報ツリー
    * DIT: Directory Information Treeという階層構造に情報が格納される
* 識別名
    * DN: Distinguished Name
    * ディレクトリツリー内のエントリを識別する
* LDIF
    * LDAP Data Interchange Format
    * ディレクトリ内の情報をテキストファイルで表現
    * 属性名と属性値を列挙
* スキーマ
    * データを格納するためのオブジェクトクラスや属性の定義
* オブジェクト識別子
    * OID: Object Identifier

## OpenLDAPサーバの設定

* LinuxのLDAP利用には、では一般的にOpenLDAPが利用される
* slapd
    * OpenLDAPサーバデーモン
* LDIF
    * LDAP Data Interchange Format
    * ディレクトリ内の情報が記述されたテキストファイルフォーマット
    * 書式
        * dn: 識別子
属性名: 値
属性名: 値
    * 主な属性名
        * dn
            * Distinguished Name
            * 識別名
        * objectClass
            * オブジェクトクラス
* slapd.conf
    * /etc/openldap/slapd.conf
    * OpenLDAPサーバの設定ファイル
    * slap = Standalone LDAP Daemon
        * スタンドアロン型のLDAPデーモン
    * 主な設定項目
        * inlude
            * スキーマファイルなどの別ファイルを読み込む
    * アクセス制御
        * エントリや属性に対してアクセス制御を設定
        * access toパラメータを使って設定
        * 書式
            * access to アクセス制御対象 by 接続元 アクセスレベル
        * アクセス制御対象
            * `*` すべてのエントリ
            * `dn=識別名`  指定したエントリ
        * 接続元
        * アクセスレベル
            * `compare` 比較できる
    * 管理コマンド
        * slapd起動
            * /usr/local/etc/libexec/slapd
            * /etc/init.d/slapd start
            * systemctl start slapd.service
        * slappasswdコマンド
            * 管理者パスワードを作成
            * `-s` ハッシュ化するパスワードの指定
* SSSD
    * System Security Service Daemon
    * LDAPとの通信を管理し情報をキャッシュすることによって、LDAPサーバの負荷軽減やLDAPサーバが停止した際の可用性を高める
    * 設定ファイル
        * /etc/sssd/sssd.conf

### LDAPクライアント

* ldapaddコマンド
    * エントリを追加
    * 書式
        * ldapadd [オプション]
    * オプション
        * `-h ホスト` LDAPサーバを指定
        * `-x` SASLを使用せずに簡易認証を行う
        * `-D 識別名` 認証に利用する識別名を指定
        * `-W` パスワード入力を対話的に行う
        * `-w パスワード` パスワードを指定
        * `-f ファイル名` LDIFファイルを指定
* ldapsearchコマンド
    * エントリを検索
    * 書式
        * `ldapsearch [オプション] 検索条件 [出力属性]`
        * 検索条件は、`(属性名＝値)`のように指定
        * OR検索を行う場合は`(|(条件1)(条件2))`のように指定
    * オプション
        * `-h ホスト` k円作を行うLDAPサーバを指定
        * `-x` SASLを使用せずに簡易認証を行う
        * `-b` 検索を開始する位置を指定
        * `-L` 検索結果をKDIFv1形式で表示
        * `-LL` 検索結果をコメントなしで表示。バージョンは表示する
        * `-LLL` 検索結果をコメントなしで表示。バージョンは表示しない
* ldappasswdコマンド
    * パスワードを変更する



