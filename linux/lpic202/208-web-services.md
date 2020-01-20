HTTPサービス
===

## ポイント

#### 理解しておきたい用語と概念

* httpd.confの主要なディレクティブ
* キープアライブ
* Apacheの主要モジュール
* 基本認証
* バーチャルホスト
* SSL
* プロキシサーバSquid
* Nginx
* リバースプロキシ

#### 習得しておきたい技術

* Apacheのインストールと設定
* Apacheのモジュールの利用
* 基本認証によるアクセス制御
* ダイジェスト認証によるアクセス制御
* バーチャルホストの設定
* SSLの特徴と利用方法
* プロキシサーバの役割と機能
* Squidのアクセス制御
* Nginxの基本設定

## Apacheの基本的な設定

### Apacheの設定

* メインの設定ファイルはhttpd.conf
    * ソースからインストール
        * /usr/local/apache2/conf/httpd.conf
    * RedHat
        * /etc/httpd/conf/httpd.conf
    * Debian
        * /etc/apache2/apache2.conf
* 機能ごとに複数の設定ファイルに分割し、httpd.confでインクルードして利用する
* 制御用コマンド
    * apachectl
        * RedHat系
    * apache2ctl
        * Devian系
    * サブコマンド
        * start, stop, restart
        * graceful
            * 安全に再起動
        * configtest
            * 設定ファイルの構文チェック

### ディレクティブ

httpd.confの設定項目はディレクティブと呼ばれる

* DocumentRoot
    * web上で公開するファイルのディレクトリを指定
* ServerTokens
    * HTTPヘッダに出力されるバージョン情報を指定
    * 通常はProd
* ServerRoot
    * httpdが利用するトップディレクトリを指定
* ServerName
    * Apacheが稼働しているホストのホスト名
* ServerAdmin
    * サーバ管理者の連絡先アドレスを指定。エラーページなどに表示される
* Listen
    * 待受ポート
* User
    * http子プロセスの実行ユーザを指定
* Group
    * http子プロセスの実行グループを指定
* Alias
    * ディレクトリとパスを指定してディレクトリのエイリアスを指定
* Redirect
    * 指定したURLへリダイレクト
    * `Redirect [ステータス] URL-path URL`
* VirtualHost
    * 1台のサーバで2つ以上のwebサイトを管理するバーチャルホスト設定
    * `<VirtualHost IPアドレス[:ポート番号]>...</VirtualHost>`
    * 名前ベースのバーチャルホスト
        * ひとつのIPアドレスに複数のドメイン名を設定
        * NameVirtualHostディレクティブでIPアドレスを設定する
    * IPベースのバーチャルホスト
        * 複数のIPアドレスに複数のドメイン名を設定
        * ListenディレクティブでIPアドレスを設定
* ServerAlias
    * サーバのエイリアスを指定

サーバ処理関連のディレクティブ

* Timeout
    * クライアントからリクエストを受け取ってから完了するまでの時間の最大値を指定
* KeepAlive
    * ブラウザサーバ間でTCP接続をキープする、KeepAliveの有効無効を指定
    * 1つのTCP接続を使って複数のHTTP処理リクエストをすることができる
* MaxKeepAliveRequests
    * KeepAlive時に1つの接続を受け付ける最大リクエスト数を指定
* KeepAliveTimeout
    * KeepAlive時にクライアントからのリクエストを完了してから、コネクションを切断せずに次のリクエストを受け取るまでの最大待ち時間を指定
* StartServers
    * 起動時のプロセス数
* MaxSpareServers, MinSpareServers
    * 待機子プロセスの最大最小を設定
* MaxRequestWorkers
    * 最大同時接続数
* MaxConnectionsPerChild
    * htp子プロセスが処理するリクエストの最大数を指定

ログ関連のディレクティブ

* HostnameLookups
    * IPアドレスを逆引きホスト名で記録するかどうか指定
* LogFormat
    * アクセスログに使われる書式を定義
* CustomLog
    * アクセスログのファイル名とLogFormatで定義された書式を指定
* LogLevel
    * エラーログに記録するログのレベルを指定
* ログファイルは/var/log/httpdディレクトリ以下に格納

外部設定ファイル関連のディレクティブ

* AccessFileName
    * 外部設定ファイル名を指定
    * デフォルトは.htaccess
* AllowOverride
    * 外部設定ファイルのhttpd.confの上書きを許可
    * <Directory>セクション内でのみ使用できる
    * パラメータ
        * AuthConfig
        * All
            * すべての設定の変更を許可
        * None
            * すべての設定の変更を禁止

モジュール関連のディレクティブ

* LoadModuleディレクティブ
    * モジュールをロード
    * apxsコマンドでApacheの動的モジュールのコンパイルとインストールを行う
* 主なモジュール
    * mod_authn_file
        * .htaccessでのユーザ認証機能を提供
        * データベースからユーザを検索するために利用される
    * mod_auth_basic
        * BASIC認証のフロントエンド
    * mod_auth_digest
        * ダイジェスト認証のフロントエンド
    * mod_authz_host
        * ホストベースのアクセス制御を提供
    * mod_access_compat
        * ホストベースのアクセス制御
    * mod_perl
        * Perlの機能を提供

### クライアントアクセスの認証

基本認証(BASIC認証)

* htttpd.confにユーザ認証設定を追加し、パスワードファイルを用意する
* パスワードが平文で流れる
* 導入に必要な作業
    * htpasswdコマンドを使用しパスワードファイルの作成及びユーザの登録を行う
    * 必要であれば、グループファイルの作成及びグループの登録を行う
    * Apacheの設定ファイルhttpd.confまたは、外部設定ファイル.htaccessでユーザ認証によるアクセス制御を加えたいディレクトリの設定を行う
* htpasswdコマンド
    * BASIC認証のためのユーザ管理のコマンド

ダイジェスト認証

* チャレンジレスポンス方式の認証
* 盗聴されても直ちにパスワードが漏洩することはない
* 現在ではほとんどのブラウザが対応している
* htdigestコマンド
    * ダイジェスト認証のためのユーザ管理のコマンド
    * 書式
        * `htdigest [オプション] ファイル名 領域 ユーザ名`
    * `-c` パスワードファイルの新規作成
    * `-D` ユーザを削除

ホストベースのアクセス認証

* Order, Allow, Deny, Requireディレクティブを使ってホスト名ドメイン名でアクセス制御ができる
* Apache2.4では非推奨なのでRequireディレクティブを使う

認証のディレクティブ

* AuthType
    * 認証方式を指定
    * BASIC認証の場合はBasic, ダイジェスト認証の場合はDigestを指定
* AuthName
    * 認可領域名を指定
* AuthUserFile
    * 作成したパスワードファイル名を指定
* AuthGroupFile
    * 作成したグループファイル名を指定
* Order Deny, Allow
    * Denyディレクティブで広く拒否する範囲を指定し、Allowディレクティブで一部アクセスを許可する範囲を指定する
    * デフォルトすべて許可
* Order Allow, Deny
    * Allowディレクティブで広く許可する範囲を指定し、Denyディレクティブで一部アクセスを拒否する範囲を指定する
    * デフォルト全て拒否
* Require
    * 認証対象とするユーザまたはグループを指定

Requireディレクティブ

* 書式
    * `Require [not] エンティティ 値`
* エンティティ
    * all granted
        * 全て許可
    * all denied
        * 全て拒否
    * env
        * 指定した環境変数が設定されていると許可
    * method
        * 指定したhttpメソッドに合致すると許可
    * expr
        * 指定した表現に合致すると許可
    * ip
        * 指定したIPアドレスを許可
    * user
        * 指定したユーザを許可
    * all
    * method
* モジュールが提供するエンティティ
    * mod_authz_core
        * 下記以外
    * mod_authz_host
        * ip, host
    * mod_authz_user
        * user, group, valid-user
* 複数の条件をしたい場合のディレクティブ
    * RequireAll
        * すべての条件に合致したら真
    * RequireAny
        * いずれかの条件に合致したら真
    * RequireNone
        * すべての条件に合致しなかったら真

### サーバ情報の取得

* mod_statusモジュールでサーバの稼働状況の情報をブラウザで表示できる
* mod_infoモジュールでサーバの設定情報をブラウザで表示できる

## HTTPS向けのApacheの設定

### SSL

* 公開鍵暗号を使ったセキュリティ技術
* mod_sslモジュールを使用

### サーバ証明書

* サイトの正当性を証明
* サーバ側に設定
* サーバの公開鍵、証明書を発行した認証局の情報、その署名、が含まれている

### 中間CA証明書

* 自身の公開鍵
* サーバ側に設定
* 証明書を発行した認証局の情報とその署名

### ルートCA証明書

* 自身の公開鍵
* ブラウザに内蔵

### サーバ証明書を認証局から入手する手順

* 公開鍵と暗号鍵を作成
* 公開鍵を認証局(CA)へ送付
* CAが証明書を発行して返送
* 返送された証明書をwebサーバにインストール

### 関連ファイル

* server.key
    * サーバ秘密鍵
* server.csr
    * 認証局に対する証明書発行要求書
* server.crt
    * サーバ証明書

### ApacheでSSL/TLSを利用する流れ

* 秘密鍵を作成
* サーバ証明書を認証局に作成してもらうため、CSR(Certificate Signing Request 証明書の署名要求)を作成する
* 認証局にCSRを提出し、その後、中間CA証明書とサーバ証明書が認証局より発行される
* 秘密鍵と中間CA証明書、サーバ証明書をサーバの所定の場所で設置し、ApacheのSSL/TLS用の設定ファイル「ssl.comf」でそれぞれのファイルを指定する

### ディレクティブ

* SSLCertificateKeyFile
    * サーバ秘密鍵ファイル

## キャッシュプロキシとしてのSquidの実装

### プロキシサーバの利点

* クライアントからのアクセス制御
* キャッシュによるアクセスの高速化、ネットワークトラフィックの削減

### Squid

* Linuxで最もよく利用されているプロキシサーバ
* /etc/squid/squid.conf 設定ファイル
    * cache_mem
        * メモリ上のキャッシュサイズ
    * auth_param
        * ユーザ認証の方式等を設定

### アクセス制御の設定

* acl
    * ホストやプロトコルの集合にACL名をつける
    * 書式
        * `acl ACL名 ACLタイプ 文字列もしくはファイル名`
    * ACLタイプ
        * src
            * クライアントのIPアドレス
        * srcdomain
            * クライアントのドメイン名
        * arp
            * MACアドレス
* http_access
    * アクセス制御を設定する
    * 書式
        * `http_access allow|deny ACL名`

## Nginxの実装

### Nginx

* Nginxは高速で動作し負荷に強いWebサーバ。リバースプロキシサーバ、メールプロキシサーバの機能も有している
* マスタープロセスと複数のワーカープロセスから構成される

### 設定

* 設定ファイル
    * /etc/nginx/nginx.conf
    * /etc/nginx/, /etc/nginx/conf.d/ 以下に複数のファイルを配置してnginx.confに読み込んで利用
    * `-t` nginx.confファイルの構文をチェック
* nginx.confのディレクティブ
    * http
        * httpサーバとしての設定
    * index
        * インデックスとして返すファイル名の指定
    * fastcgi_pass
        * FastCGIの設定
    * fastcgi_param
        * FastCGIにわたすパラメータ設定の指定

### リバースプロキシの設定

* コンテンツをキャッシュしてクライアントに提供
* アクセス元からHTTPヘッダを転送する必要がある
* proxy_set_header
