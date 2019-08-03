ファイルシステムとデバイス
===

### Linuxファイルシステムの操作と保守

* ファイルシステム
    * /proc/filesstem
        * 利用可能なファイルシステムの一覧が書かれているファイル
        * https://www.itmedia.co.jp/enterprise/articles/0803/04/news006_2.html
* ファイルシステムの種類
    * ext2
        * Linuxで標準的に使われる。ジャーナリング機能をもたない。Second Extended Filesystem
    * ext3
        * ext2にジャーナリング機能を加えたもの
    * ext4
        * ext3の後継で、大容量のファイルシステムをサポート
        * RHEL6, Ubuntuのルートファイルシステムタイプ
    * btrfs
        * サブボリューム(subvolume)を構成しスナップショットなどの機能を持つ。従来のファイルシステム+LVM。データの破損も検出、破棄、修復が可能。ZFSを参考として開発された
    * xfs
        * 古くからあり、ジャーナリング機能を持つ
        * RHEL7及びその派生OS(CentOS, Oracle Linuxなど)のルートファイルシステムタイプ
    * iso9660
        * CDROMで使われる
    * FAT/VFAT
        * フラッシュメモリで利用
    * ntfs
        * 近年のwindowsで利用
    * nfs
        * ネットワークファイルシステム。ネットワークを介したリモートのファイルにアクセス
    * tmpfs
        * メモリ上に配置(RAMディスク)
        * https://www.atmarkit.co.jp/flinux/rensai/linuxtips/277usetmpfs.html
    * swap
        * 物理メモリのスワップ先
    * ZFS
        * OracleのSolarisで使用
* マウント、アンマウント
    * デバイスとその上に構築されているファイルシステムをOSに認識させ、指定のディレクトリに割当、そのディレクトリ以下のパスでファイルシステム内にアクセスできるようになる
    * mountコマンド
        * 
    * unmountコマンド
        * -aオプションで/etc/mtab にあるファイルシステムをすべてアンマウント
    * /etc/fstab
        * ファイルシステムのマウントの設定が書かれているファイル
        * デバイス、マウント先、種類、オプション、dumpフラグ、fsckフラグ の書式
        * https://www.infraeye.com/study/linuxz24.html
    * マウント状況を確認するコマンド
        * /etc/mtab
        * /proc/mounts
    * http://www.turbolinux.com/products/server/10s/manual/command_guide/command_guide/mount.html
* パーティション
    * ハードディスクの領域を分割
    * パーティション分割のメリット
        * ファイルのバックアップやシステムのアップデータが必要な場合に便利
        * 障害に強い
        * HDDを有効活用できる
    * パーティション分割のデメリット
        * 見積もりミスによってパーティションの使用率に偏りが発生する(かもしれない)
    * MBR形式とGPT形式がある。MBR形式は、基本パーティションと拡張パーティションがあり、基本パーティションは4つまで作成可能、それ以上分割するには拡張パーティションに割り当てて論理パーティションにする
    * fdisk
        * パーティション操作
    * 新しいハードディスクをLinuxで利用するための流れ
        * パーティションを切る(基本パーティション、拡張パーティションを作成)
        * 各パーティションをフォーマットし、ファイルシステムを作る
        * 作ったファイルシステムをマウント
    * http://www.miloweb.net/partition.html#1
* スワップ領域
    * 物理メモリを超えたデータを退避するためのハードディスク上の領域
    * スワップ領域の有効化手順
        * スワップ領域として使いたいサイズのファイルを作成する
        * mkswapコマンドで、作成したファイルをスワップ領域として初期化する
        * swaponコマンドで、作成したファイルをスワップ領域として有効化する
    * mkswap
        * スワップ領域を作成、初期化
    * dd
        * スワップ領域として利用するファイルを作成
    * swapon
        * スワップ領域を勇往
    * swapon -s, cat /proc/swaps
        * システムで有効になっているスワップ領域の利用状況を調べるコマンド
    * http://www.turbolinux.com/products/server/10s/manual/command_guide/command_guide/swap.html
    * https://www.atmarkit.co.jp/flinux/rensai/linuxtips/389swapfile.html
* SMART
    * ハードディスクの問題を発見するための自己診断機能。Self-Monitoring, Analysis and Reporting Technology System
    * smartdデーモンを実行、smartctlコマンドで監視内容を確認
    * http://e-words.jp/w/S.M.A.R.T..html
    * https://www.atmarkit.co.jp/flinux/rensai/linuxtips/521smartinfo.html
* blkid
    * デバイスのラベル、UUID、ファイルシステムの種類などを表示するコマンド
    * https://access.redhat.com/documentation/ja-jp/red_hat_enterprise_linux/6/html/deployment_guide/s2-sysinfo-filesystems-blkid
    
### ファイルシステムの作成とオプションの構成

* ファイルシステムを作成するコマンド
    * mke2fs
        * ext2/ext3/ext4を作成するコマンド
        * -j ext3として作成する
        * -b ブロックサイズをbyte単位で指定
    * mkfs.xfs
        * xfsを作成するコマンド
    * mkfs
        * いろいろなファイルシステムを作ることができる
    * https://www.atmarkit.co.jp/ait/articles/0408/24/news093_3.html
    * https://open-groove.net/linux/linux-mkfs/
* ファイルシステムをチェックするコマンド
    * e2fsck
        * ext2/ext3/ext4のチェック
    * xfs_check
        * xfsのチェック
    * fsck
        * いろいろなファイルシステムをチェック
        * https://tech.nikkeibp.co.jp/it/article/COLUMN/20060227/230781/
* 作成済みファイルシステムに対するコマンド
    * tune2fs
        * ext2/ext3/ext4の設定を行う
        * -j ext2をext3に変換
        * https://www.atmarkit.co.jp/flinux/rensai/linuxtips/760ext3chk.html
    * dumpe2fs
        * ext2/ext3/ext4の詳細を表示する
    * e2label
        * ext2/ext3/ext4のラベルを操作
        * https://www.atmarkit.co.jp/flinux/rensai/linuxtips/925vollabel.html
* ジャーナリング
    * 実データをディスクに書き込む前にメタデータを書き込んでおくことで、不正な終了時の不整合を防ぐ機能
    * https://www.atmarkit.co.jp/ait/articles/0309/17/news002.html
* オートマウント
    * 特定のデバイスを必要なときだけ自動でマウントし、利用できるようにする機能
    * メインの設定ファイル /etc/auto.master、編集したらデーモンの再起動が必要
    * http://www.maruko2.com/mw/automount_%E3%81%AE%E8%A8%AD%E5%AE%9A
* ファイルシステムの暗号化
    * 手順
        * ブロックデバイスへの暗号化マッピングを作成する
        * 暗号化マッピングにファイルシステムを作成する
        * 暗号化ファイルシステムをマウントする
    * dm-crypt
        * ブロックデバイスの暗号化方法のひとつ
        * cryptsetupで暗号化デバイスを作成、管理する
        * /dev/mapperディレクトリ配下にファイルシステムを作成
    * LUKS(Linux Unified Key Setup)
        * Linuxで標準的に使用される暗号化ファイルシステムの仕様
        * dm-cryptを用いて実装されている
        * 使用手順
            * デバイスをLUKSパーティションとして初期化
            * LUKSパーティションを開く
            * 開いたLUKSパーティションにファイルシステムを作成する
            * 暗号化ファイルシステムをマウントする
    * http://www.usupi.org/sysad/186.html
    * https://wiki.archlinux.jp/index.php/Dm-crypt/%E3%83%87%E3%83%90%E3%82%A4%E3%82%B9%E3%81%AE%E6%9A%97%E5%8F%B7%E5%8C%96
