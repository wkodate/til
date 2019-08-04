高度なストレージ管理
===

### RAIDの構成

* RAID0
    * ストライピング。複数のディスクを利用
    * 利用可能な容量は、全ディスクの合計
    * https://www.data-sos.com/raid/raid07.html
* RAID1
    * ミラーリング。他のディスクにコピーして冗長化
    * 利用可能な容量は、1台分
    * https://www.data-sos.com/raid/raid08.html
* RAID5
    * 3台以上で構成し、パリティ領域と呼ばれるデータ復旧用のデータを各ディスクに分散
    * ディスク１台分をパリティ領域として利用
    * 利用可能な容量は、合計から1台引いた分
    * https://www.data-sos.com/raid/raid09.html
* mdadmコマンド
    * RAIDを構築・管理するコマンド
    * あらかじめfdiskコマンドで使用するデバイスのパーティション「0xfd」を設定しておく
    * 設定ファイル /etc/mdadm.conf 
        * http://www.obenri.com/_raid_build/mdadm_rebuild.html
    * Createモード
        * RAIDの新規作成
    * Manageモード
        * RAIDを構成するデバイスの追加・削除をするコマンド
    * Miscモード
        * RAIDの状態を表示、詳細を表示、停止をオプションで指定
    * 他にもAssembleモードなどがある
    * https://open-groove.net/lpic/linux-create-raid-arrays-mdadm/
* /proc/mdstat
    * RAIDの状態をチェック。RAIDアレイの状態を確認

### 記憶装置へのアクセス方法の調整

* デバイスファイル
    * システムに接続されている物理的なデバイスにアクセスする方法として、/dev内の仮装ファイル(デバイスファイル)がある
    * IDE接続のハードディスクはhdで始まるデバイスファイル。最近は名前が統合されhdもsdになっている
    * SCSI/SATA接続のハードディスクはsdで始まるファイルデバイス
    * SATAはATAよりも高速な転送が行える
    * https://www.debian.org/releases/etch/sparc/apcs04.html.ja
* SSD
    * フラッシュメモリ、ハードディスクの速度を大幅に改善
    * Trimは、事前に消去動作を行なってSSDの速度低下を緩和する。fstrimコマンドを利用
* NVMe(NVM Express)
    * PCI ExpressバスからSSDに接続するための規格
* SCSI(Small Compute System Interface)、iSCSI
    * PCやサーバに接続されたハードディスクなどの周辺機器を制御し、データ転送を行うための規格
    * iscsiadm
        * iscsidをデバイスとして使用するコマンド
    * iscsid.confは、iscsidデーモンとiscsiadmコマンドの設定ファイル
    * _netdev は、対象となるデバイスがネットワークを通して利用可能になるまでマウントを待つマウントオプション
    * http://www.usupi.org/sysad/171.html
* hdparm
    * ハードディスクに関するパラメータを取得・設定するコマンド
    * オプション
        * -W1 ライトキャッシュをオン
        * -c1 32bit I/Oをオン
* WWID(World Wide Idenitifier)
    * ファイバーチャネルやSCSIによるSANにおいて記憶装置を一位に識別する
* LUN(Logical Unit Number)
    * 記憶装置を識別するための論理的な単位
    * https://www.atmarkit.co.jp/ait/articles/0808/25/news120.html

### 論理ボリュームマネージャ

* LVM(Logical Volume Manager)
    * 物理ボリュームをまとめて仮想的な領域(ボリュームグループ)ろし、そこから仮想的なパーティション領域(論理ボリューム)を切り出して柔軟に記憶領域を管理できる仕組み
    * LVMを構成する手順
        * fdiskコマンドによるLVM用パーティションの作成
        * pvcreateコマンドによるPV(物理ボリューム)の作成
        * vgcreateコマンドによるVG(ボリュームグループ)の作成
        * lvcreateコマンドによるLV(論理ボリューム)の作成
        * mkfsコマンドによるファイルシステムの作成
        * ファイルシステムのマウント
    * LVMボリュームの円作フィルタ lvm.conf
    * /dev/VG名/LV名 がその論理ボリュームを表すデバイスファイル
    * https://tech.nikkeibp.co.jp/it/article/Keyword/20071012/284413/
    * https://www.itmedia.co.jp/enterprise/0307/11/epn01.html
    * http://pantora.net/pages/linux/lvm/3/
    * https://users.miraclelinux.com/technet/document/linux/training/2_2_3.html#training2_2_3_1
* スナップショット
    * ある瞬間の論理ボリュームの状態を記録する機能。差分を利用した仕組み
