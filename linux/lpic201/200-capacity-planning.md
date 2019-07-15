Capacity-Planning
===

# コマンド

* sar
  * https://www.atmarkit.co.jp/ait/articles/0303/15/news004_2.html
* top
  * https://thinkit.co.jp/free/article/0710/5/2/
* mpstat
  * CPUの使用率を表示する
* htop
  * https://orebibou.com/2016/05/htop%E3%82%B3%E3%83%9E%E3%83%B3%E3%83%89%E3%81%A7%E8%A6%9A%E3%81%88%E3%81%A6%E3%81%8A%E3%81%8D%E3%81%9F%E3%81%84%E4%BD%BF%E3%81%84%E6%96%B911%E5%80%8B/
  * 全体およびプロセス毎のCPUやメモリの使用状況を一定時間毎に更新して表示するコマンド
  * topをカラーにしてファンクションキー使える高機能なツール
* free
  * https://tech.nikkeibp.co.jp/it/article/COLUMN/20060227/230780/
* swapon
  * スワップ領域を有効にするコマンド。-sを使用するとスワップ領域の利用状況を確認できる
* vmstat
  * https://tech.nikkeibp.co.jp/it/article/COLUMN/20060228/230989/
  * 仮想メモリやCPU、ディスクIOの統計情報を表示するコマンド
* iotop
  * https://linux.die.net/man/1/iotop
  * プロセス毎のディスクIOを監視するツール
* iostat
  * https://thinkit.co.jp/free/tech/23/4/
  * i/oデバイスの使用状況を表示
* nfsiostat
* cifsiostat
* lsof
  * https://www.atmarkit.co.jp/ait/articles/0305/22/news001.html
  * list open files
* netstat
  * https://tech.nikkeibp.co.jp/it/article/COLUMN/20060227/230845/
  * ssコマンドはnetstatコマンドの代替
    * https://ytooyama.hatenadiary.jp/entry/2016/04/26/222631
    * https://www.skyarch.net/blog/?p=2834
* iptraf
* uptime
* w
* ps
* pstree
* https://tech.nikkeibp.co.jp/it/article/COLUMN/20071204/288730/

# CPU使用率

* CPU = ユーザプロセス + システムプロセス(カーネル) + I/O待ち
* CPU使用率を表示するコマンド
  * top
  * vmstat
  * iostat
  * sar -u | -P 番号またはALL
  * mpstat [-P 番号|ALL]
    
# メモリ

利用状況確認コマンド

* top
* vmstat
* free
* sar -r 物理メモリ
* sar -S スワップ領域
* swapon -s

# sar 

sarコマンド

```
sar [オプション] [表示間隔(秒) [回数]]
```

sadfコマンド

```
sadf [オプション] [対象データ] -- [sarのオプション]
```

* sadcコマンドがcronの設定によって定期的に収集し/var/log/sa/saXXにバイナリ形式で保存
* -f で読み込むログファイルを指定する
* sa1は、sadcの定期的に実行するスクリプト。/usr/lib/sa/sa1
* sa2は、sarの定期的に実行するスクリプト。/usr/lib/sa/sa2
* sysstatは、sa,sadc等を含む管理ツールの名前
* sadfは、sarコマンドのオプションを指定して私、結果の出力を整形するコマンド

# リソース需要の予測

* リソース状況を可視化するツール
  * Icinga2
    * Nagios互換の監視ツール
  * Nagios
    * サーバの死活監視やネットワークサービスの状態やリソースの使用状況などを総合的に監視するソフトウェア
  * collectd
    * サーバの状態を監視するデーモン
    * /etc/collectd.confが設定ファイル。LoadPluginで使用するプラグインを指定
  * MRTG(Multi Router Traffic Grapher)
    * ネットワークやリソースの使用状況を監視しグラフ化するツール。死活監視はできない
  * Cacti
    * MRTGの代替。ネットワークやリソースの使用状況を監視し、グラフ化するツール。ブラウザで設定可能でMRTGより設定が容易。死活監視はできない
* SNMP(Simple Network Management Protocol)
  * ネットワーク経由で機器を監視・生業するためのプロトコル
* リソース状況を監視する理由
  * https://www.atmarkit.co.jp/ait/articles/0303/15/news004.html
  * 問題調査のため
  * キャパシティプランニングのため
  * 需要予測のため
