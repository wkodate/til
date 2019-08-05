ネットワーク構成
===

### 基本的なネットワーク構成

* ifconfig
    * ネットワークインターフェースに関する操作を行うコマンド
    * フォーマットは、ifconfig インタフェース名 操作内容
    * https://www.atmarkit.co.jp/ait/articles/0109/29/news004.html
* ip
    * ifconfig, route, arpを置き換えるコマンド
    * フォーマットは、ip [オプション] オブジェクト コマンド
    * オブジェクト
        * link: ネットワークデバイス関連
        * addr(address): ネットワークプロトコル
        * route: ルーティング関連
        * neigh(neighbour): ARP等
* iwconfig
    * 無線LAN関連の操作を行うコマンド
* iw
    * 無線デバイスや無線インターフェースの設定を行うコマンド
* iwlist
    * 無線LANのリンク品質や信号強度を確認できるコマンド
    * scanninig(scanと省略可)を指定して、使用可能なアクセスポイントをスキャンする
    * https://www.suse.com/ja-jp/documentation/sles11/singlehtml/book_sle_admin/cha.wireless.wlan.html#sec.wireless.wlan.tant.stab
* arp
    * IPアドレスからMACアドレスを知る
    * https://www.atmarkit.co.jp/ait/articles/0111/29/news003.html
* /etc/sysconfig/network
    * RedHat系のネットワークの全体設定を行うファイル
    * http://www.turbolinux.com/products/server/11s/user_guide/netconffile.html
* /etc/network/interfaces
    * Debian系OSで各インターフェースを設定するファイル
* /etc/nsswitch.conf
    * 名前解決の優先順位の設定するファイル
* /etc/networks
    * ネットワーク名とネットワークアドレスの対応関係を記述する設定ファイル

### 高度なネットワーク構成とトラブルシュート

* ping
    * -b: ブロードキャストでpingを実行
    * 重複した応答があるとDUP!をつけて表示
* netstat
    * ネットワーク状態についての様々な情報が得られるコマンド
    * -rオプションで、ルーティングテーブルを表示
    * -lオプションで、LISTENのソケットのみを表示
    * -iオプション絵、ネットワークインターフェースの統計を表示
* route
    * ルーティングテーブルの表示、追加、削除
    * デフォルトゲートウェイの設定は、route add default gw デフォルトゲートウェイのIPアドレス
* tcpdump
    * パケットキャプチャができるコマンド
    * 出力をフィルタリングできる
    * https://thinkit.co.jp/article/730/1?page=0,1
    * https://prev.net-newbie.com/tcpip/tcp/tcpdump.html
* dig
    * IPアドレスとホスト名の変換を行うコマンド。nslookupよりも詳しい情報が得られる
* mtr
    * 通信の可・不可や経路を確認することができるコマンド。IPv６対応
    * Mytraceroute
* hostnane
    * 自ホストのホスト名を表示
    * -a オプションで自ホストのエイリアスを表示
* nmap
    * LISTENポートをスキャンするツール
    * https://www.atmarkit.co.jp/ait/articles/0801/08/news127.html
* TCPラッパーは、ネットワークサービスに対するアクセス制御を行うプログラム。デーモン名はtcpd
    * /etc/hosts.allow, /etc/hosts.denyでアクセス許可、拒否の設定を行う
* デフォルトゲートウェイは外部と接続したルータのIPアドレス
