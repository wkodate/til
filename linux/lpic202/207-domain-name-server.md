ドメインネームサーバ
===

## ポイント

#### 理解しておきたい用語と概念

* DNS
* ゾーン
* /etc/named.conf
* ゾーンファイルとレコード
* ゾーン転送
* chroot
* DNSSEC
* TSIG
* DANE

#### 習得しておきたい技術

* /etc/named.confの設定
* ゾーンファイルの設定
* 名前解決コマンド
* rndcコマンド
* BINDのセキュリティ
* dnssec-keygenコマンド
* dnssec-signzoneコマンド

## DNSの基本

* DNSサーバ
    * ゾーン
        * DNSサーバが管轄するドメインの範囲
        * ゾーンを管理できる権限を権威、下位のDNSサーバに管理を移すことを移譲
    * マスタースレーブ
        * マスター
            * ゾーンファイルを所有するDNSサーバ
        * スレーブ
            * マスターのゾーン情報をコピーするDNSサーバ
        * ゾーン転送
            * マスターからスレーブへのコピー
    * 再起問い合わせ
* DNSサーバソフトウェア
    * BIND
        * 代表的なDNSサーバでありLinuxで利用される
        * namedデーモンが起動
    * dnsmasq
        * DNSサーバ機能とDHCPサーバ機能を提供するソフトウェア
    * djbdns
        * 機能を分割して安全性を高めたDNSサーバ
    * PowerDNS
        * コンテンツサーバおよびキャッシュサーバ機能を提供するソフトウェア
        * MySQL, RDBを利用できる
* クライアントコマンド
    * nslookup
    * host
    * dig

## BINDの基本設定

* /etc/named.conf
    * いくつかのステートメントとオプションから構成される
    * ステートメント
        * acl
            * ACLの定義
        * controls
            * namedを操作できるホストの指定
        * include
            * 外部ファイルの読み込み
        * key
            * 認証情報の設定
        * zone
            * ゾーンの定義
        * options
            * namedの動作に関する詳細設定
            * directory
                * namedの作業ディレクトリを指定
            * recursion
            * recursive-clients
            * max-cache-size
            * forward [only|first]
                * 問い合わせ転送の失敗時の動作を設定
            * forwarders
            * allow-recursion
                * 再起問い合わせを受け付けるホストを指定
    * named-checkconf
        * named.confの構文チェック
* rndcコマンド
    * remote named daemon control
    * namedの操作
    * サブコマンド
        * status
        * reload
        * halt
        * stop

## ゾーンファイルの管理

* ゾーンに関する情報は、ゾーンごとにゾーンファイルに記述する
* named.confのoptionsステートメントでゾーンファイルに配置するディレクトリを指定
* named.confのzoneステートメントでゾーンファイル名を指定する
* ゾーンファイル
    * $ORIGINディレクティブ
        * ドメイン名が明示されていないレコードで補完するドメインを指定
    * $TTLディレクティブ
        * 他のDNSサーバがゾーンデータをキャッシュの保存しておく時間を指定
    * リソースレコード
        * ゾーンの情報を記述
* リソースレコードタイプ
    * SOAレコード
    * NSレコード
    * MXレコード
    * Aレコード
        * ホスト名に対応するIPアドレスを定義
    * AAAAレコード
    * CNAMEレコード
    * PTRレコード

## DNSサーバのセキュリティ

* 一般的なセキュリティオプション
    * ゾーン転送の制限
    * DNS問い合わせの制限
    * バージョン番号の隠蔽
    * root以外によるnamedの実行
* DNSSEC
    * DNS Security
    * ゾーン情報に公開鍵暗号方式の電子署名を行う
    * ゾーン情報が改ざんされていないこと、DNS応答が正当な管理者によって行われたものであることを保証
    * DNSサーバ、クライアントの双方が対応している必要がある
* TSIG
    * Transaction Signatures
    * ゾーン転送の安全性を向上させる仕組み
    * マスターDNSで共通鍵を使ってゾーンデータに署名し、スレーブDNSサーバでそれを検証する
    * dnssec-keygenコマンドで共通鍵を生成
    * /etc/named.confにkeyステートメントとして共通鍵を設定
