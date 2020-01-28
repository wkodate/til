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
    * ユーザがMUAでメールを送信→メールサーバはMTAでメールを受信→MDAにメールを渡し、メールサーバにメールを配送→MUAからMRAにアクセスしてメールを取得
* MUA
    * Mail User Agent, メールユーザエージェント
    * メールクライアントソフトウェア。パソコン等で使用されるメーラ
    * 作成されたメッセージはSMTPを使用してMTAに渡される
    * Outlookなど
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
* リレー
    * MTA間のメールの中継
    * リレーのための適切な設定
        * リレーを許可したいLAN内からのアウトバウンドメールは許可する
        * リレーを許可したいドメインのメールを許可する
        * 外部からのアウトバウンドメールを拒否する
    * オープンメールリレー
        * リレー対象を制限せずオープンに受け付けるMTA
        * Postfixではデフォルトで禁止されている
        * RBL
            * Real-time Blackhole List
            * 受け取り拒否のブラックリスト
            * DNSの仕組みを使って動作
    * ~/.forward ファイル
        * 一般ユーザが自身あてのメールを別のメールアドレスに転送

### Postfix

* 広く使われているMTA(SMTPサーバ, メールサーバ)
* 複数のプログラムが協調して動作

### Postfixの設定

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
    * `mynetworks`
        * リレーを許可するクライアントを指定
    * `mail_spool_directory`
        * メールスプールディレクトリを指定
    * `home_mailbox`
        * ユーザのホームディレクトリ配下のメールスプールディレクトリを指定
        * mail_spool_directoryと療法で指定した場合は、home_mailboxの記述が優先される
    * `mailbox_command`
        * ローカル配送を行うプログラム(MDA)を指定
    * `disable_vrfy_command`
        * SMTPコマンドのVRFYの使用を無効化(yes), 有効化(no)を指定
* TLS設定
    * Postfix2.2でTLSを強制するためには、smtps, smtpd_use_tls, smptd_enforce_tlsの設定が必要
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
* 各フィールド
    * `chroot`
        * chroot jail環境で動作させるかどうかの指定
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
    * Postfixの設定値を表示
    * `-n` デフォルトから変更されている設定項目を表示

### Postfixのcanonical機能

* 送信者、受信者のアドレスを書き換える機能
* 有効にする作業
    * main.cfの書き換えルールの参照先(検索先)を指定
    * 検索テーブルの作成、更新
* 書き換えルールのパラメータ
    * canonical_maps
        * 送受信時の書き換えで参照する検索テーブル
    * sender_canonical_maps
        * 送信者アドレスの書き換え時に参照する検索テーブル
    * recipient_canonical_maps
        * 受信者アドレスの書き換え時に参照する検索テーブル

### sendmail

* sendmailの設定ファイルは複数あり、/etc/mailディレクトリに配置される

### エイリアス

* /etc/aliases
    * メールアカウントのエイリアスを設定するファイル
    * メーリングリストなどで使用される
* 書式
    * `アカウント名： 受け取りユーザ名[,受け取りユーザ名 ...]`
* 別名
    * `|コマンド`
        * 指定したコマンドにメールのメッセージを渡す
    * `user@domain`
        * 指定したメールアドレスにメールを転送
    * `:include:/ファイルのパス`
        * 指定したファイルに記述された内容を別名として読み込む
* newaliasesコマンド
    * /etc/aliasesファイルの設定を反映

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
        * `-bp`, `mailq` メールキューを表示する
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
    * MAIL FROM
        * 差出人を指定
    * RCPT TO
        * 宛先を指定
    * DATA
        * 本文を入力
    * QUIT
        * SMTPセッションを終了

## 電子メール配信の管理

* Maildir形式
    * cur, tmp, newというサブディレクトリに分類し、1メール1ファイルとして保存する

### Sieve

* シーブ
* メールのフィルタリングを行うためのサーバサイドのプログラミング言語
* Dovecotと連携しサーバ側でフィルタリング処理をする
* 条件と条件にマッチした場合の処理を記述する
* 書式
    * `if 条件 { 条件がマッチした場合の処理; }`
    * 条件に「テスト」と複数の文字列が入る
    * 条件がマッチした場合の処理は、「アクション」と文字列が入る
* 主なコマンド
    * コントロール
        * `if`
            * 条件分岐を記載
        * `require`
            * 拡張機能を指定。アクションコマンドのfileintoなど
        * `stop`
            * 処理を停止。暗黙のkeepが含まれる
    * テスト
        * `address`
            * アドレスを評価
        * `header`
            * メールヘッダを評価
        * `size`
            * メールのサイズを評価
        * `allof`
            * 論理積 AND
        * `anyof`
            * 論理和 OR
    * アクション
        * `keep`
            * ユーザのメールボックスにメッセージを保管(デフォルト)
        * `fileinto`
            * 指定したメールボックスに配送
        * `reject`
            * メールを拒否する
        * `vacation`
            * 長期休暇などの不在時に自動返信する

### Procmail

* MTAで受信したメールをそれぞれのユーザに配信するソフトウェアであり、MDAのひとつ
* .procmailrc ファイルにフィルタリングの設定


## メールボックスアクセスの管理

* MRA
    * Mail Retrieval Agent
    * POPやIMAPプロトコルを使ってメールボックスからメールを取り出すソフトウェア
    * Dovecot
    * Courier IMAP(クーリエ アイマップ)
        * IMAPをサポートするMRA
* プロトコルとポート
    * SMTP: 25
    * POP3: 110
    * IMAP: 143
    * POPS3: 995
    * IMAPS: 993

### Dovecot

* ダヴコット
* POP3 ,IMAPプロトコルに加え、SSL/TLSに対応したMRA

### /etc/dovecot.conf

* Dovecotの設定
* 設定項目
    * `listen`
        * 接続を待ち受けるアドレスを指定
    * `protocols`
        * 使用するプロトコルを指定
    * `verbose_proctitle`
        * psコマンドの出力に詳細(ユーザ名、IPアドレス)を表示する
    * `mail_location`
        * メール格納方法および格納場所を指定

### /etc/dovecot/conf.d/10-ssl.conf

* TLSの設定
* 設定項目
    * `ssl`
        * yesでSSL/TLS有効化
    * `ssl_cert`
        * 「<サーバ証明書のパス」 でサーバ証明書を指定
    * `ssl_key`
        * 「<サーバ秘密鍵のパス」 で秘密鍵を指定

### Dovecot管理コマンド

* doveconfコマンド
    * Dovecotの設定内容を出力
* doveadmコマンド
    * Dovecotの管理用コマンド
    * 起動や再起動はできない
    * 書式
        * `doveadm サブコマンド`
    * サブコマンド
        * `reload`
            * 設定を再読み込み
        * `stop`
            * Dovecotを停止
        * `mailbox`
            * メールボックスを管理
        * `pw`
            * パスワードを生成する
        * `who`
            * サーバにログイン中のユーザ情報を表示

