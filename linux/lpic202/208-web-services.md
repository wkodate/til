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

* Apacheの設定ファイル
    * メインの設定ファイル
        * ソースからインストール
            * /usr/local/apache2/conf/httpd.conf
        * RedHat
            * /etc/httpd/conf/httpd.conf
        * Debian
            * /etc/apache2/apache2.conf
    * 機能ごとに複数の設定ファイルに分割し、httpd.confでインクルードして利用する
* ディレクティブ
    * httpd.confの設定項目はディレクティブと呼ばれる
    * DocumentRoot
        * ドキュメントルートとなるディレクトリを指定
    * ServerTokens
        * HTTPヘッダに出力されるバージョン情報を指定
        * 通常はProd
    * ServerRoot
        * httpdが利用するトップディレクトリを指定
    * ServerName
        * Apacheが稼働しているホストのホスト名
    * ServerAdmin
        * サーバ管理者の連絡先アドレスを指定。エラーページなどに表示される
    * StartServers, MinSpareServers, MaxSpareServers
        * 起動時のプロセス数、待機子プロセスの数
    * MaxRequestWorkers
        * 最大同時接続数
    * KeepAlive
        * ブラウザサーバ感でTCP接続をキープする、KeepAliveの有効無効を指定
    * Listen
        * 待受ポート
    * LogFormat
        * ログに記録する項目と形式を定義
    * Redirect
        * 指定したURLへリダイレクト
* 外部設定ファイル
    * httpd.confファイルのAccessFileNameディレクティブで外部設定ファイル名を指定
    * 通常は.htaccess
* モジュール
    * httpd.confのLoadModuleディレクティブでモジュールをロード
    * apxsコマンドでモジュールを組み込む
* クライアントアクセスの認証
    * 基本認証(Basic認証)
        * htttpd.confにユーザ認証設定を追加し、パスワードファイルを用意する
        * パスワードが平文で流れる
    * ダイジェスト認証
        * チャレンジレスポンス方式の認証のため、盗聴されても直ちにパスワードが漏洩することはない
    * ホストベースのアクセス認証
        * Order, Allow, Deny, Requireディレクティブを使ってホスト名ドメイン名でアクセス制御ができる
* バーチャルホスト
    * 1台のサーバで複数のWebサイトを管理できる
* サーバ情報の取得
    * mod_statusモジュールでサーバの稼働状況の情報をブラウザで表示できる
    * mod_infoモジュールでサーバの設定情報をブラウザで表示できる
* ログファイル
    * アクセスログとエラーログがある
    * /var/log/httpdディレクトリ以下に格納

## HTTPS向けのApacheの設定

* SSL
    * 公開鍵暗号を使ったセキュリティ技術
    * mod_sslモジュールを使用
    * サーバ証明書によってサイトの正当性を証明
    * サーバ証明書を認証局から入手する手順
        * 公開鍵と暗号鍵を作成
        * 公開鍵を認証局へ送付
        * 返送された証明書をwebサーバにインストール

## キャッシュプロキシとしてのSquidの実装

* SquidはLinuxで最もよく利用されているプロキシサーバ
* /etc/squid/squid.conf が設定ファイル
* アクセス制御の設定
    * acl
        * ホストやプロトコルの集合にACL名をつける
    * http_access

## Nginxの実装

* Nginxは高速で動作し負荷に強いWebサーバ。リバースプロキシサーバ、メールプロキシサーバの機能も有している
* マスタープロセスと複数のワーカープロセスから構成される
* 設定ファイル
    * /etc/nginx/nginx.conf
    * /etc/nginx/, /etc/nginx/conf.d/ 以下に複数のファイルを配置してnginx.confに読み込んで利用
* リバースプロキシ
    * コンテンツをキャッシュしてクライアントに提供
    * アクセス元からHTTPヘッダを転送する必要がある
    * proxy_set_header
    
