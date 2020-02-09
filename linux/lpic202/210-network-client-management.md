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
        * DHCPによる自動設定が有効になったクライアントは、ネットワーク上にブロードキャストでDHCPDISCOVERメッセージを送信する
    * DHCP Offer
        * プールにあるIPアドレスをクライアントに提案
        * ブロードキャストを受け取ったDHCPサーバは、自身の管理するアドレスプールから、IPアドレスやサブネットマスク、デフォルトゲートウェイアドレスなどの設定情報をDHCPOFFERメッセージで回答する
    * DHCP Request
        * IPアドレスを使いたい旨をリクエスト
        * クライアントはブロードキャストでDHCPREQUESTメッセージを送信し、サーバに対しての設定値のリクエストを行う
    * DHCP Ack
        * 承諾メッセージを送る
        * DHCPREQUESTを受けとたサーバは、割り当てたアドレスを確定しDHCPACKメッセージを返却する
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
* DHCPクライアントで動作するデーモンは、dhclient, dhcpcd
* LPICでは、標準的なDHCPサーバである ISC DHCP について問われる
    * Internet Software Consortium

### DHCPサーバの設定

* dhcpd
    * DHCPサーバのデーモン
* /etc/dhcpd.conf
    * dhcpdの設定ファイル
    * 宣言文
        * subnet ... netmask ...
            * 設定するサブネットワーク
        * host
            * 設定するホスト
* リース期間
    * IPアドレスをクライアントに貸し出す期間
    * dhcpdが現在貸し出しているIPアドレスは、dhcpd.leasesファイルに記録される

DHCPサーバの設定ファイル「/etc/dhcpd.conf」の書式

```
設定項目;
設定項目;
・・・
宣言文 {
設定項目;
設定項目;
...
}
```

### /etc/dhcpd.confの設定項目

* `option domain-name ドメイン名;`
    * ドメイン名を指定
* `option domain-name-servers IPアドレス [,IPアドレス...];`
    * DNSサーバのIPアドレスを指定
* `option routers IPアドレス;`
    * デフォルトゲートウェイのIPアドレスを指定
* `option subnet-mask IPアドレス;`
    * サブネットマスクを指定
* `option broadcast-address IPアドレス;`
    * ブロードキャストアドレスを指定
* `option ntp-servers IPアドレス;`
    * NTPサーバのIPアドレスを指定
* `option nis-domain ドメイン名`
    * NISドメイン名を指定
* `option nis-servers IPアドレス`
    * NISサーバのIPアドレスを指定
* `option netbios-name-servers IPアドレス`
    * WINSサーバのIPアドレスを指定
* `default-lease-time 秒;`
    * デフォルトの貸し出し期限を指定
* `max-lease-time 秒;`
    * 最大の貸し出し期限を指定
* `range [dynamic-bootp] 開始アドレス [終了アドレス];`
    * クライアントに貸し出すIPアドレスの範囲を指定
    * dynamic-bootpはフラグとして使用される
        * BOOTP(BOOTstrap Protocol)は、IPアドレスなどの情報をクライアントに割り当てるプロトコル
* `fixed-address IPアドレス;`
    * クライアントに割り当てる固定IPアドレスを指定
* `hardware インターフェースタイプ MACアドレス;`
    * クライアントを特定するためのMACアドレスを指定
* `(allow | deny) unknown-clients;`
    * MACアドレス指定のないクライアントにアドレスの割当を許可するかどうか

### DHCPクライアントの設定

* DHCPクライアント
    * dhclient
    * pump
    * dhcpcd

### DHCPリレーエージェント

* 異なるネットワーク感で同一のDHCPを利用するためのプログラム
    * DHCPはブロードキャストを使ってサービスを提供するので、ルータを隔てたネットワークに対してサービスを提供できない
    * リレーエージェントは、別ネットワークのDHCPサーバにDHCPクライアントからのリクエストを転送する
* DHCPリレーエージェントのデーモンはdhcrelay
* 書式
    * `dhcrelay [-i インターフェイス名] [DHCPサーバ]`

### DHCPv6

* IPV6で行われるアドレス設定の代表例
    * ステートレス自動設定
        * IPv6のRAを用いた設定
    * ステートフル自動設定
        * DHCPサーバで状態を管理
    * DHCPv6(DHCPv4との互換なし)
* 割当可能なアドレス
    * ユニークローカルアドレス
    * グローバルアドレス
    * 一時アドレス
* ステートレスアドレス自動設定だけでは設定できない情報を配布できる
* ルータ広告
    * RA: Router Advertisement
    * IPv6の自動設定を行う機能の一部
    * Linuxでルータ広告を行うにはradvdを使用する
        * 設定ファイル radvd.conf

## PAM認証

### PAM

* Pluggable Authentication Modules
* UNIXで認証情報を一元管理する仕組み
* プログラムはユーザ情報が/etc/passwdにあるのか、他のファイルにあるのか、別のホストにあるのか、を意識しなくてよくなる
* 認証方式は設定ファイルを編集するだけで変更可能

### 設定 /etc/pam.dディレクトリ

* PAMの設定ファイルがあるディレクトリ
* /etc/pam.dディレクトリのファイルと/etc/pam.confが両方存在する場合は、/etc/pam.confファイルは無視される
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
        * 失敗した場合は、すぐに認証を拒否してそれ以降のモジュールを実行しない
    * `required`
        * 実行に失敗した場合は、以降の同じタイプのモジュールを実行してから認証を拒否する
    * `sufficient`
        * 成功した場合は、以前の行のrequiredのモジュールの実行が全て成功していればその時点で認証を許可する
        * 失敗した場合は、引き続き以降の行のモジュールを実行する
    * `optional`
        * 成功しても失敗しても、以降の行にあるモジュールの実行を続ける
* モジュール
    * モジュールは /lib/security, /lib64/security ディレクトリ配下に格納されている
    * `pam_console.so`
        * 端末から一般ユーザでログインした際、管理コマンドやデバイスなどの所有権を一時的に一般ユーザ権限に変更
    * `pam_cracklib.so`
        * パスワードの書式を制限し、安全性を向上
    * `pam_env.so`
        * 環境変数を設定
    * `pam_ldap.so`
        * LDAPを使った認証を行う
    * `pam_limits.so`
        * ユーザが利用するリソースを制限
    * `pam_listfile.so`
        * 任意のファイルを用いてプログラムのアクセス制御を行う
    * `pam_nologin.so`
        * /etc/nologinファイルがある場合、一般ユーザのログインを拒否
    * `pam_securetty.so`
        * rootでのログインを/etc/securetty ファイルに記述された端末にのみ許可
    * `pam_sss.so`
        * SSSDを使った認証を行う
    * `pam_unix.so`
        * 通常のパスワード認証を行う
        * 引数にnullokを指定すると空のパスワードを許可する
    * `pam_warn.so`
        * 認証時やパスワードの変更時にログを記録
        * 管理者に警告を送信
    * `pam_wheel.so`
        * root権限のアクセスをwheelグループのメンバーに制限

PAMの設定ファイルの例

```
auth required pam_securetty.so
auth required pam_unix.so nullok
auth required pam_nologin.so
```

## OpenLDAPサーバの設定

### LDAPの仕組み

* Lightweight Directory Access Protocol
* ディレクトリサービスの提供者と利用者の間での情報をやり取りするプロトコル
    * ディレクトリサービスは、各種リソースの名前とその属性をツリー状に系統的にまとめて管理するサービス
    * ユーザやリソースに関する情報を検索したり管理したりすることができる
    * X.500
        * ディレクトリサービスの国際規格
        * DAPプロトコルが定義されており、ここから不要な機能や操作を取り除いて軽量化し、TCP/IP上でも使用できるようにLDAPを開発
* OpenLDAP
    * LinuxのLDAP利用には、一般的にOpenLDAPが利用される
* オブジェクト
    * LDAPではデータをオブジェクトとして扱う
    * 構成要素
        * エントリ
            * LDAPでのオブジェクトの単位
            * 各エントリはオブジェクトクラスに属している
            * オブジェクトクラスによってエントリの実体(エントリが人か、組織かなど)が決定される
        * 属性
            * エントリが持つ情報を表すもの
        * DIT
            * Directory Information Tree。ディレクトリ情報ツリー
            * エントリ間の関係を階層構造で表したもの
            * エントリを識別するための識別子にはDNとRDNがある
            * DN: 識別名。エントリを一意に表す
            * RDN: 相対識別子。名前属性
    * コンテナ
        * 下位のエントリを分類する目的のみに使用するエントリ
* slapd
    * OpenLDAPサーバデーモン
    * slapdコマンドの起動オプション
        * `-4` IPv4アドレスのみを受け付ける
        * `-6` IPv6アドレスのみを受け付ける
        * `-f ファイル名` 設定ファイルの指定
        * `-u ユーザ名` slapdを起動するユーザの指定
        * `-g グループ名` slapdを起動するグループの指定
* SSSD
    * System Security Service Daemon
    * リモートにある認証システムの情報をキャッシュし、クライアント側で認証できるようにする仕組み
    * LDAPとの通信を管理し情報をキャッシュすることによって、LDAPサーバの負荷軽減やLDAPサーバが停止した際の可用性を高める
    * 設定ファイル
        * /etc/sssd/sssd.conf

### LDIF

* LDAP Data Interchange Format
* ディレクトリ内の情報が記述されたテキストファイルフォーマット。LDAPサーバにエントリを登録、変更するときにLDIFファイルを利用する
* LDAPでは認証(バインド)はエントリのDN(識別子、ユーザ名のようなもの)とパスワードを使用する
* 書式
    * dn: 識別子
属性名: 値
属性名: 値
* 通常UTF-8のテキストで記載。バイナリデータなどを指定する場合は「属性名::属性値」とし、Base64エンコードした値を指定
* 識別子(DN: Distinguished Name)
    * ディレクトリツリー内のエントリを識別する
* LDAPで使用される属性
    * `dn`
        * 識別子名(Distinguished Name)
        * `属性名1=値1, 属性名2=値2, 属性名3=値3, ...`
    * `objectClass`
        * オブジェクトクラス
        * 必須属性。全てのオブジェクトクラスの基底クラスであるtopは、objectClass属性が必須となっている
    * `cn`
        * 一般名称(Common Name)
    * `dc`
        * ドメイン構成要素(Domain Component)
    * `mail`
        * メールアドレス
    * `o`
        * 組織名(Organization)
    * `ou`
        * 部署などの組織単位(Organization Unit)
    * `telephoneNumber`
        * 電話番号
    * `uid`
        * ユーザのログイン名
    * `uidNumber`
        * ユーザID
* changetypeディレクティブ
    * LDIFファイルを利用してエントリの追加、削除、変更を行う
    * 書式
        * dn: 対象エントリの識別名
        * changetype: 変更種別
        * 変更内容
    * 変更種別
        * `add`
            * エントリの追加
        * `delete`
            * エントリの削除
        * `modify`
            * エントリ情報の変更
        * `modrdn`
            * RDNの変更
    * LDIFファイルはテキストファイルだが直接編集は非推奨。編集用のldapmodifyコマンドを使う

#### オブジェクトクラス

* LDAPのオブジェクトの単位であるエントリ(人、組織など)は、必ずオブジェクトクラスに属している
* オブジェクトクラスや属性はスキーマファイルで定義されている
* OID
    * オブジェクトクラスや属性には、オブジェクト識別子(OID: Object Identifier)が割り当てられる
    * はIANA(Internet Assigned Numbers Authority)という機関によって割り当てられる

オブジェクトクラスの属性

* 必須属性
    * 属性値を指定しなければならない
* オプション属性
    * 属性値を指定しなくても良い
* 名前属性
    * エントリの名前を表す属性 (RDN: Relative Distinguished Name)

オブジェクトクラスの種類

* `ABSTRACT` (抽象型)
    * 他のオブジェクトを定義するための基底クラス
    * 抽象型クラスのみでエントリを作成することはできない
* `STRUCTUAL` (構造型)
    * 人や組織等を表すオブジェクトクラス
    * エントリには必ず1つの構造型クラスが適用されている必要がある
    * エントリを作成し直さない限り変更できない
* `AUXILIARY` (補助型)
    * 構造型クラスとともに使用するオブジェクトクラス。構造型クラスでは足りない属性を補う
    * 複数適用可
    * 補助型クラスのみでエントリを作成することはできない

スキーマ

* オブジェクトクラスや属性を定義する
* スキーマファイルは`/etc/openldap/schema`ディレクトリに配置される
* スキーマファイルはあらかじめ用意されている
* OpenLDAP2.2までは、使用するスキーマファイルは、OpenLDAPの設定ファイルslapd.confでincludeディレクティブを利用して読み込まれる
* OpenLDAP2.3以降はLDIFファイルで行われる
* `core.schema`
    * cnやouなどの基本的な属性を定義
    * 必須
* `cosine.schema`
    * X500で定義された属性などを定義
* `inetorgperson.schema`
    * inetOrgPersonクラスなどを定義
* `nis.schema`
    * NIS関連のオブジェクトクラスや属性などを定義

ホワイトページ

* ホワイトページ(個人別電話帳)をLDAPで構築するにはオブジェクトクラス「inetOrgPerson」を使用する
* OutlookやThunderbirdでアドレス帳として使用できる
* inetOrgPerson
    * ホワイトページを作成する場合に使用するオブジェクト
    * 構造型
    * 基底クラスはorganizationalPerson
    * personで必須属性となっているcd,snの設定が必須
* organizationPerson
    * 基底クラスはperson
* person
    * 構造型
    * 属性
        * 必須属性となっているcnとsnを設定する必要がある
        * `cn`
            * 一般名称(Common Name)
        * `sn`
            * 名字(SurName)

### 設定 slapd.conf、slapd-config

* slap = Standalone LDAP Daemon
    * スタンドアロン型のLDAPデーモン
* OpenLDAP2.3以降、テキストの設定ファイルから起動時に設定を読み込む一般的な方法(slapd.conf)から、LDAPを使った動的な設定方法(slapd-config)に変更されている

/etc/openldap/slapd.conf

* slapd.conf
* OpenLDAPサーバの設定ファイル
* slapd起動コマンド
    * /usr/local/etc/libexec/slapd
    * /etc/init.d/slapd start
    * systemctl start slapd.service

### slapd.confのディレクティブ

グローバルセクションで使用するディレクティブ

* `argsfile ファイル名`
    * slapdデーモン起動時のコマンド引数を格納するファイルを指定
* `pidfile ファイル名`
    * slapdのプロセスIDを格納するファイルを指定
* `include ファイル名`
    * 読み込む設定ファイルを指定
* `logfile ファイル名` 
    * デバッグログの出力先ファイルを指定
* `idletimeout 秒数`
    * アイドル状態のクライアント接続を強制的に切断するための秒数を指定
* `timelimit 秒数`
    * slapdが検索リクエストのレスポンスに使う最大秒数を指定

データベースセクションで使用するディレクティブ

* `database 種類`
    * バックエンドデータベースの種類を指定
* `suffix DN`
    * ディレクトリのトップとなるDNを指定
* `rootdn DN`
    * データベース管理者のDNを指定
* `rootpw パスワード`
    * データベース管理者のパスワードを指定
* `index 属性名 種類`
    * 属性に作成するインデックスの種類を指定
* `directory ディレクトリ`
    * データベースファイルを格納するディレクトリを指定

slapd.confファイルの例

```
# グローバルセクション
include /etc/openldap/schema/core.schema
include /etc/openldap/schema/cosine.schema
include /etc/openldap/schema/inetorgperson.schema

pidfile /var/run/openldap/slapd.pid
argsfile /var/run/openldap/slapd.args

# データベースセクション（databaseディレクティブから開始）
database bdb
suffix "dc=my-domain,dc=com"
rootdn "cn=Manager,dc=my-domain,dc=com"
rootpw secret
directory /var/lib/ldap

access to * by * read

index objectClass eq,pres
index ou,cn,mail,surname,givenname eq,pres,sub
```

### slapd.confのアクセスディレクティブ

* バインドしたユーザに対してエントリや属性に対してアクセス制御を設定
* access toパラメータを使って設定
* 書式
    * `access to アクセス制御対象 by 接続元 アクセスレベル by ...`
    * コメントは行頭に「#」をつける。途中にコメントを入れることはできない
    * by以降は順番に処理され、条件にマッチするとそれ以降の条件は処理されない
    * `by anonymous auth` はLDAPサーバにアクセスする際は必ず認証を行う必要があるので必要。ないとそもそも認証ができなくなる
* マッチする条件がない場合は、`access to  * by * none`が適用され、アクセスが拒否される
* アクセス制御の設定を何も記述しない場合は、`access to * by * read`と同じ条件が適用され、全てのデータを全てのユーザが参照できるようになる
* アクセス制御対象
    * `*`
        * すべてのエントリ
    * `dn=識別名`
        * 指定したエントリ
    * `attrs=属性名`
        * 指定した属性
    * `filter=検索フィルタ`
        * 検索フィルタにマッチするエントリ
        * 検索条件は、`(属性名＝値)`のように指定
* 接続元
    * `*`
        * 全ての接続(全てのユーザ)
    * `anonymous`
        * 認証されていない接続(匿名ユーザ)
    * `users`
        * 認証された接続(認証済みのユーザ)
    * `self`
        * エントリ自身の接続(認証済みのユーザ自身)
    * `dn=識別名`
        * 指定した識別名による接続
* アクセスレベル
    * `none`
        * アクセスできない
    * `auth`
        * 認証が可能
    * `compare`
        * 比較できる(+authの権限)
    * `search`
        * 検索が可能(+compareの権限)
    * `read`
        * 値の参照が可能(+searchの権限)
    * `write`
        * 値の更新が可能(+readの権限)

### slapd-configで使用されるディレクティブ

* olc = Online Configuration
* 設定全体のルートのDNは「cn=config」で定義され変更できない
* LDAPのデータベースに保存されている設定データを追加・変更・削除することで、動的に設定を更新
* 設定変更による再起動等が不要

ディレクティブ

* `olcLogLevel`
    * syslogに出力するレベルを指定
* `olcInclude: <filename>`
    * includeファイルを指定
    * `cn=include`でslapd.confをinclude
* `olcAttributeTypes`
    * 属性型を定義
    * `cn=schema`で組み込みのスキーマを定義
* `olcObjectClasses`
    * オブジェクトクラスを定義
* `olcBackend=<type>`
    * バックエンドを指定
    * olcBackendConfig=<type>は必須オブジェクトクラス
    * type
        * `bdb` Berkeley DB
        * `config` Slapd configuration backend
        * `ldap` LDAP backend
        * `ldif` LDIF backend
        * `passwd` passwdファイルへの読み取りアクセス

### LDAPサーバの管理コマンド

* slapaddコマンド
    * エントリの追加
    * `-l` 追加するエントリが記述されているファイルを指定
* slapcatコマンド
    * データベースからデータLDIFファイルとして出力
    * `-l` 出力するLDIFファイルを指定。ldifファイル
* slaptestコマンド
    * slapd.confの構文をチェック
* slapindexコマンド
    * slapd.confで設定したインデックスを作成
* slappasswdコマンド
    * 管理者パスワードをハッシュ化して作成
    * `-s パスワード` secret, ハッシュ化するパスワードの指定
    * `-h ハッシュ方式` hash, ハッシュ方式の指定
* 共通オプション
    * `-v` 詳細情報の表示
    * `-d 数` デバッグレベルの指定
    * `-c` エラー発生時も処理を継続
    * `-n 数` 使用するデータベースをDB番号で指定
    * `-b DN` 使用するデータベースをDNで指定
        * DNはsuffixディレクティブで使用されているもの

## LDAPクライアントの利用方法

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
        * AND検索を行う場合は`(&(条件1)(条件2))`のように指定
    * オプション
        * `-h ホスト` k円作を行うLDAPサーバを指定
        * `-x` SASLを使用せずに簡易認証を行う
        * `-b` 検索を開始する位置を指定
        * `-L` 検索結果をKDIFv1形式で表示
        * `-LL` 検索結果をコメントなしで表示。バージョンは表示する
        * `-LLL` 検索結果をコメントなしで表示。バージョンは表示しない
* ldappasswdコマンド
    * パスワードを変更する
    * 書式
        * `ldappasswd [-x] [-h LDAPサーバ] -D 認証に利用する識別名 [-w 認証ユーザのパスワード] -s 新パスワード 変更するユーザ名`
        * 対話的に新パスワードを入力した場合は、-Sオプションのみを指定
        * `-w`パスワード
        * `-s` 新しいパスワード。set new passwd



