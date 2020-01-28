メールサービス
===

## ポイント

#### 理解しておきたい用語と概念

* Postfixとmain.cf
* メールエイリアスとメール転送
* procmailとprocmailrc
* Sieve
* Dovecot

#### 習得しておきたい技術

* main.cfを使ったPostfixの設定
* メールのエイリアス設定
* メールの転送設定
* メールキューの管理
* メールスプール
* メール関連のログ
* Sieveによるフィルタリング設定
* procmailrcの設定
* Dovecotの設定

## 電子メールサーバの使用

### メール配送の仕組み

* 流れ
    * MUA→MTA→...→MTA→MDA→MRA→MUA
* MUA
    * Mail User Agent, メールユーザエージェント
    * メールクライアントソフトウェア。パソコン等で使用されるメーラ
    * 作成されたメッセージはSMTPを使用してMTAに渡される
* MTA
    * Message Transfer Agent, メッセージ転送エージェント
    * メールの配信、受信をするプログラム。サーバサイド
    * SMTPサーバ
    * Postfix, sendmail, exim
* MDA
    * Mail Delivery Agent, ローカルメール配送エージェント
    * MTAから受信したメールを各ユーザのメールspoolに配送するプログラム
    * procmail
* MRA
    * Mail Retrieval Agent
    * ユーザのメールボックスに配信されたメールにリモートからアクセスできるようにするソフトウェア
    * Dovecot

### Postfixの設定

* Postfix
    * 広く使われているMTA(SMTPサーバ, メールサーバ)
    * 複数のプログラムが協調して動作

/etc/postfix/main.cf

* MTAの基本設定ファイル
* 設定項目
    * `myhostname`
        * 自サーバのホスト名を指定
    * `mydomain`
        * 自サーバのドメイン名を指定
    * `inet_interfaces`
        * メールを受け取るネットワークインターフェースを指定
    * `mydestination`
        * ローカル配送を行うドメインを指定
    * `home_mailbox`
        * ユーザのホームディレクトリ配下のメールスプールディレクトリを指定
    * `disable_vrfy_command`
        * SMTPコマンドのVRFYの使用を無効化(yes), 有効化(no)を指定
* TLS設定
    * `smtpd_use_tls`(2.3以前)
        * TLSの有効化
    * `smtpd_enforce_tls` (2.3以前)
        * クライアント側にTLSを強制
    * `smtpd_tls_security_level`
        * `may` TLSを有効化/適用は任意
        * `encrypt` TLSを有効化/強制適用
    * `smtpd_tls_cert_file`
        * サーバ証明書ファイルを指定
    * `smtpd_tls_CAfile`
        * CAの証明書ファイルを指定
    * `smtpd_tls_key_file`
        * サーバ秘密鍵ファイルを指定

/etc/postfix/master.cf

* Postfixを構成するプロセスの動作を設定
* TLS設定
    * `smtps`
        * コメントを外して設定を有効化

### Postfix管理コマンド

* postfixコマンド
    * Postfixを制御
    * `flush`
        * メールキュー内にあるメールを直ちに再送する
    * `check`
        * 設定ファイルの構文をチェックする
* postconfコマンド
    * Postfixの全設定値を表示

### sendmail

* 設定ファイルは /etc/mailディレクトリに配置される

### メールのリレー

* MTA間のメールの中継
* リレーの適切な設定
    * リレーを許可したいLAN内からのアウトバウンドメールは許可する
    * リレーを許可したいドメインのメールを許可する
    * 外部からのアウトバウンドメールを拒否する

### エイリアス

* /etc/aliasesでメールアカウントのエイリアスを設定
* /etc/aliasesファイルをr変更したらnewaliasesコマンドで反映

### SMTPサーバの運用と管理

* メールキュー
    * 処理待ちメールが一時的に保持される場所
    * メールキューの保存場所
        * Postfix: /var/spool/postfixディレクトリ以下のサブディレクトリ
        * sendmail: /var/spool/mqueueディレクトリ以下に格納される
    * メールキューの表示
        * Postfix: mailqコマンド
        * sendmail: postqueue -p コマンド
    * sendmailコマンド
        * `-bp mailq` メールキューを表示する
        * `-q` メールキュー内にあるメールを直ちに再送する
    * mailqコマンド
        * メールキューを表示するコマンド
* メールボックス
    * メールサーバが受け取ったメールの格納場所
    * Postfix
        * ホームディレクトリ以下にmbox形式、Maildir形式
    * sendmail
        * /var/spool/mailディレクトリ以下にあるユーザ名のテキストファイル
* SMTPコマンド
    * HELOまたはEHLO
        * SMTPセッションを開始
    * QUIT
        * SMTPセッションを終了

## 電子メール配信の管理

### Sieve
* メールのフィルタリングを行うためのプログラミング言語
* Dovecotと連携しサーバ側でフィルタリング処理をする
* 条件と条件にマッチした場合の処理を記述する
* 主なコマンド
    * fileinto
        * 指定したメールボックスに配送
    * reject
        * メールを拒否する

### Procmail

* メールをフィルタリングするMDA
* .procmailrc ファイルにフィルタリングの設定


## メールボックスアクセスの管理

* MRA
    * POPやIMAPプロトコルを使ってメールボックスからメールを取り出すソフトウェア
    * Dovecot
    * Courier IMAP

### Dovecot

* ダヴコット
* POP3,IMAPプロトコルに加え、SSL/TLSに対応したMRA

### /etc/dovecot.conf

* Dovecotの設定
* 項目
    * listen
        * 接続を待ち受けるアドレスを指定
    * protocols
        * 使用するプロトコルを指定
    * verbose_proctitle
        * psコマンドの出力に詳細を表示する

### Dovecot管理コマンド

* doveconfコマンド
    * Dovecotの設定内容を出力
* doveadmコマンド
    * Dovecotの管理用コマンド
    * 書式
        * `doveadm サブコマンド`
    * サブコマンド
        * reload
            * 設定を再読み込み
        * stop
            * Dovecotを停止
        * mailbox
            * メールボックスを管理
        * who
            * サーバにログイン中のユーザ情報を表示

