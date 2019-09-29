# キャパシティプランニング

### リソース使用率とトラブルシューティング

総合的なリソース監視

* top
* vmstat
    * 仮想メモリやCPU、ディスクIOの統計情報を表示するコマンド
    * proc, memory, swap, io, system, cpuに分かれる
    * proc
        * r 実行待ちプロセス数
        * b 割り込み不可のプロセス
* iostat
    * i/oデバイスの使用状況を表示
* iotop
    * プロセス毎のディスクIOを監視するツール
* sar
    * system admin reporter
    * sadcコマンド
        * system activity data collector
        * cronの設定によって定期的に収集し、`/var/log/sa/saXX`にバイナリ形式で保存
    * 定期的な実行
        * `/usr/lib/sa/sa1` sadcを定期的に実行するスクリプト
        * `/usr/lib/sa/sa2` sarを定期的に実行するスクリプト
    * sysstat
        * sa,sadc等を含む管理ツールの名前
    * `-f` 読み込むログファイルを指定
* sadf
    * sar data format
    * sadcのログをTSVやXML形式で出力
    * -- をつけてsarコマンドのオプションを渡す
    * `-j` JSON形式で出力
    * `-x` XML形式で出力
* uptime
* w
* htop
    * 全体およびプロセス毎のCPUやメモリの使用状況を一定時間毎に更新して表示するコマンド
    * topをカラーにしてファンクションキー使える高機能なツール

CPU、メモリ、ディスクIO、ネットワークなど特定のリソースの確認コマンド

* ps
* pstree
* mpstat
    * CPUの使用率を表示する
* lsof
    * list open files
    * ファイルを開いているプロセスを表示
* free
    * 詳細は`/proc/meminfo`でも確認できる
* swapon
    * スワップ領域を有効にするコマンド。-sを使用するとスワップ領域の利用状況を確認できる
    * `-s`, `--summary` サマリを表示
* df
* netstat
* ss
    * netstatコマンドの代替コマンド
    * `-a` すべてのソケットを表示
    * `-n` 数字で表示
    * `-t` TCPを表示
    * `-u` UDPを表示
* iptraf

CPU使用率

* CPU使用率 = ユーザプロセス + システムプロセス(カーネル) + I/O待ち
* CPU使用率を表示するコマンド
    * top
    * vmstat
    * iostat
    * sar -u | -P 番号またはALL
    * mpstat [-P 番号|ALL]


ネットワーク

* eth0などはインターフェース名。対応するネットワークドライバが正常にロードされていればOSが自動的に認識してethのあとに番が号う生かされる
* loはローカルループバックと呼ばれる特別な仮想インターフェース。IPアドレスとして127.0.0.1が割り当てられていて外部と通信することができない
* nfsiostat
    * NSFは、Linuxで利用されているネットワークファイル共有の機能
* cifsiostat
    * CIFSは、SMBを拡張したプロトコルで、Windows独自のプロトコル

### リソース需要の予測

* リソース状況を可視化するツール
    * collectd
        * サーバの状態を監視するデーモン
        * `/etc/collectd.conf`
            * 設定ファイル。LoadPluginで使用するプラグインを指定
    * Nagios
        * サーバの死活監視やネットワークサービスの状態やリソースの使用状況などを総合的に監視するソフトウェア
    * Icinga2
        * Nagios互換の監視ツール
        * アイシンガと読む
    * MRTG
        * Multi Router Traffic Grapher
        * ネットワークやリソースの使用状況を監視しグラフ化するツール。死活監視はできない
    * Cacti
        * ネットワークやリソースの使用状況を監視し、グラフ化するツール。ブラウザで設定可能でMRTGより設定が容易。死活監視はできない
        * MRTGの代替
* SNMP(Simple Network Management Protocol)
    * ネットワーク経由で機器を監視・生業するためのプロトコル
* リソース状況を監視する理由
    * 問題調査のため
    * キャパシティプランニングのため
    * 需要予測のため
