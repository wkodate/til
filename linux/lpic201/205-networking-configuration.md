ネットワーク構成
===

### 基本的なネットワーク構成

* ifconfig
    * ネットワーク環境の確認、設定を行う
    * 書式
        * ifconfig インターフェース名 操作内容
        * 操作内容
            * 指定がない場合は現在状態を表示
            * up 有効化
            * down 無効化
            * IPアドレスを指定するとインターフェースに対して割当ができる
            * inet6 add IPアドレス IPv6アドレスを追加
            * inet6 del IPアドレス IPv6アドレスを削除
    * IPエイリアシング
        * 複数のIPアドレスをネットワークインターフェースに割り当てる。セカンダリアドレス
        * ifconfig eth0:数字 IPアドレス
        * 数字はエイリアス番号
    * -a 無効になっているインターフェースの情報も表示する
* ip
    * ifconfig, route, arpを置き換えるコマンド
    * フォーマットは、ip [オプション] オブジェクト コマンド
    * オブジェクト
        * link
            * ネットワークデバイス関連、データリンク層
            * ifconfigの置き換え
        * addr(address)
            * ネットワークプロトコル、IPアドレス
            * ifconfigの置き換え
            * インターフェースの指定にはdevが必要
            * コマンド指定を省略するとshow
            * セカンダリアドレスの設定はプライマリアドレスの設定と同じコマンドを使う
        * route
            * ルーティング関連、ルーティングテーブル
            * routeコマンドの置き換え
            * デフォルトゲートウェイの設定
                * ip route [ add | del } default via デフォルトゲートウェイのアドレス
        * neigh(neighbour)
            * ARPキャッシュの表示、操作
            * arpコマンドの置き換え
    * コマンド
        * show、add、delete など
* iwconfig
    * 無線LAN関連の操作を行うコマンド
    * 指定なしの場合は現在状態を表示
    * essid ESSIDを指定
    * key WEPキーを指定
* iw
    * iwconfigを置き換えるコマンド
    * iw dev インターフェース名
        * インターフェースに関する情報の表示や設定を行う
        * connect
            * ESSIDを指定して接続、切断
    * iw phy デバイス名, iw phy# デバイス番号
        * デバイスに対する情報の表示や設定を行う
    * SSID, ESSIDは、無線LANのアクセスポイントを識別するID
* iwlist
    * 無線インターフェースの情報を取得できるコマンド
    * scanninig(scanと省略可)を指定して、使用可能なアクセスポイントをスキャンする
* arp
    * IPアドレスからMACアドレスを知る
    * -i interface インターフェースを指定して表示
    * -a, --all ホスト名を指定して表示、ホスト名がなければすべて表示
    * -s, --set コマンドからIPとMACアドレスの対応関係をキャッシュに追加
    * -f, --file ファイルからIPとMACアドレスの対応関係をキャッシュに追加。指定しない場合はデフォルトで /etc/ethers
* /etc/nsswitch.conf
    * 名前解決の優先順位の設定するファイル
* /etc/networks
    * ネットワーク名とネットワークアドレスの対応関係を記述する設定ファイル
* /etc/sysconfig/network
    * RedHat系のネットワークのホスト名設定、全体設定
* /etc/hostname
    * Debian系のホスト名設定
* /etc/network/interfaces
    * Debian系の全体設定

### 高度なネットワーク構成とトラブルシュート

* ping
    * ICMPパケットを送り、パケットが正しく伝送されるか、パケットの往復にどのくらい時間がかかるかをチェックするためのコマンド
    * -b broadcast ブロードキャストでpingを実行
    * -i interval 送信間隔を指定
    * -s packetsize 送信するパケットデータのサイズを指定する
    * -n numeric output only 名前解決を行わずIPアドレスのまま結果を送信
    * 重複した応答があるとDUP!をつけて表示
* netstat
    * ネットワーク状態についての様々な情報が得られるコマンド
    * -s 各プロトコルの統計情報一覧を表示
    * -r ルーティングテーブルを表示
    * -l LISTENのソケットのみを表示
    * -i ネットワークインターフェースの統計を表示
    * -n ホスト名など名前解決せず、数字のまま表示
* ss
    * netstatの後継となるコマンド
    * netstatとの違い
        * -i
            * netstatは、インターフェースの統計を表示
            * ssは、内部TCPの情報を表示
        * -r
            * netstatは、ルーティングテーブルを表示
            * ssは、名前解決を実行
* route
    * ルーティングテーブルの表示、追加、削除
    * 追加
        * route add [-host|-net] 宛先アドレス gw ゲートウェイのIPアドレス
    * 削除
        * route del [-host|-net] 宛先アドレス [gw ゲートウェイのIPアドレス]
    * デフォルトゲートウェイの設定
        * route add default gw デフォルトゲートウェイのアドレス
* tcpdump
    * パケットキャプチャができるコマンド
    * 出力をフィルタリングできる
    * src, dstに続けてhost, port, IPアドレスを指定して送信元、送信先のパケットを対象にフィルタイリングする
    * andで条件を複数指定できる
    * -i 監視するインターフェースを指定
    * -n 名前解決を行わずIPアドレスのまま表示
    * -X 16進数とASCIIの表でパケットの内容を表示
    * 結果の書式
        * 送受信時刻 プロトコル 送信元 > 送信先: 内容
* dig
    * IPアドレスとホスト名の変換を行うコマンド。nslookupよりも詳しい情報が得られる
* mtr
    * ICMPで通信の可・不可や経路を確認することができるコマンド。IPv6対応
    * Mytraceroute
* hostname
    * 自ホストのホスト名を表示
    * -a, --alias エイリアスを表示
    * -f, --fqdn FQDNを表示
    * -d, --domain DNSドメインを表示
    * -i, --ip-address IPアドレスを表示
* nmap
    * LISTENポートをスキャンするツール
    * オプションでスキャンタイプを指定できる。デフォルトはTCPスキャン
    * -F 有名ポートを対象にした高速スキャン
    * -O 対象ホストのOS識別
    * -p ポート指定
    * -sのうしろにTUPがつくと、TCPスキャン、UDPスキャン、Pingスキャンになる
* NetworkManager
    * 動的にネットワークの設定を行うプログラム
* TCPラッパー
    * ネットワークサービスに対するアクセス制御を行うプログラム。デーモン名はtcpd
    * /etc/hosts.allow, /etc/hosts.deny ファイルの書式
        * サービス名: ホスト名|IPアドレス
    * /etc/hosts.allowのみでアクセス制御するため場合の書式
        * サービス名: ホスト名|IPアドレス: ALLOW|DENY
    * 192.168.10.0/24のようなネットワークを指定する場合は、192.168.10. または 192.168.10.0/255.255.255.0と記述する
    * アクセス制御の順番
        * /etc/hosts.allow, /etc/hosts.denyでアクセス許可、拒否の設定を行う
        * /etc/hosts.allowの条件にマッチすればその時点でアクセスを許可
        * マッチしなかった場合は/etc/hosts.denyの条件にマッチすればアクセスを拒否
        * それにもマッチしなければ許可
    * ファイルの変更を反映させるためにtcpdの再起動は不要
* デフォルトゲートウェイは外部と接続したルータのIPアドレス
* ネットワーク関連の設定ファイル
    * /etc/nsswitch.conf
        * 名前解決の優先順位の設定
        * name service switchの略
    * /etc/hosts
        * ホスト名とIPアドレス対応の設定
    * /etc/resolv.conf
        * 名前解決に使うDNSサーバの設定
    * /etc/networks
        * ネットワーク名とネットワークアドレス対応の設定
    * /etc/hostname
        * Debian系のホスト名設定
    * /etc/sysconfig/network
        * RedHat系全体・ホスト名設定
        * NETWORKING=yes ネットワークを使用するかどうかの設定
        * NETWORKING_IPV6=yes IPv6アドレスの使用を有効化
    * /etc/sysconfig/network-scripts/ifcfg-インターフェース名[0-9]
        * RedHat系のデバイス別設定
