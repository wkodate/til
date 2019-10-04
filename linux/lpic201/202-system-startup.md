システムの起動
===

### システム起動のカスタマイズ

* Linuxの起動処理
    * BIOS起動
    * ブートローダ呼び出し
    * Linuxカーネル、初期RAMディスク起動
        * initプログラムの起動
        * dmesgコマンド おｈもしくは/var/log/dmesg でログを確認
    * Systemd起動。default.targetの命令に従う
* SysVinit
    * System Five Init
    * initプロセスが/etc/inittabファイルの設定に従い、システムに必要なサービスを順次起動する
    * デフォルトランレベルの指定は、/etc/inittabに書くか、GRUBの設定ファイルのkernel行に引数で渡す
    * /etc/inittab
        * initがシステムに必要なサービスを起動する順番を記述する
        * デフォルトランレベル、ランレベルに応じた動作が設定できる
        * 形式は、ID:RunLevel:Act:Cmd
        * Act(アクション指示子)
            * initdefault
                * デフォルトランレベルを指定
            * once
                * 指定ランレベルになった際に一度だけ実行
            * wait
                * 指定ランレベルになった際に一度だけ実行、プロセスの終了を待つ
            * boot
                * システムのブート時に実行される
            * bootwait
                * ブート時に実行され、プロセスの終了を待つ設定を行う
            * sysinit
                * ブート時に最優先で実行される
            * respawn
                * プロセスが終了しても自動的に再起動
* ランレベルとターゲット
    * SysVinitではランレベル、systemdではターゲットがランレベルの概念になる
    * ランレベルとターゲット
        * 0 poweroff.target
            * システム終了
        * 1,s,single rescue.target
            * シングルユーザモード
        * 2-4 multi-user.target
            * マルチユーザモード
        * 5 graphical.target
            * マルチユーザモード(GUI)
        * 6 reboot.target
            * システム再起動
    * Qのランレベルでは、/etc/inittabを再読込してinitを再実行する
    * /etc/rc[0-6].dディレクトリ以下にランレベル毎のサービスのシンボリックリンクが貼られる
    * サービスのうち、Sで始まるサービスが起動、Kで始まるサービスは終了。K->Sで実施される
    * telinit
        * initプロセスに指示を出し、ランレベルを変更できるコマンド
* serviceコマンド
    * Linuxデーモン(サービス)の起動や停止、ステータスの確認を実行する
    * rcスクリプトの実体は/etc/init.dに収められているシェルスクリプト
* サービスの自動起動
    * 手動でリンクを作成
        * /etc/rc[0-6].dディレクトリ以下の、ファイル名がSで始まるファイルがシステム起動時に自動起動する
    * chkconfig
        * RedHat系のコマンド
        * 起動スクリプトを管理するコマンド
        * chkconfig [--level ランレベル] サービス (on|off|reset)
            * --level 特定のランレベルだけを指定。複数のランレベルを指定する場合はスペースなどで区切らずに数字を並べる。省略すると2,3,4,5が対象
        * chkconfig --list [サービス]
        * --list サービスを指定して設定を表示
    * update-rc.d
        * 6.0より前のDebian系のコマンド
        * 自動起動設定を追加・削除し、優先順位を指定することができる
        * update-rc.d [オプション] サービス 操作 [優先度] [ランレベル]
            * 操作
                * defaultsでランレベルに応じてSかKで始まるリンクを作る
                * start,stopで起動停止用スクリプトを作成
                * removeでシンボリックリンクを削除
            * ランレベル指定の最後にドットをつけて終了を伝える
    * insserv
        * SUSE系、6.0以降のDebian系の自動起動設定
        * insserv [オプション] サービス
        * -r 自動起動しない設定
* systemd
    * RHEL7では起動処理はsystemdが行われる。これまではinit/upstartだった
    * systemdの機能
        * systemdジャーナルへのロギング
        * 依存関係
        * cgroup
        * サービスの有効化
        * サービス以外の機能
        * リソース管理
    * サービスの管理はPIDではなくcgroupで管理する
    * systemdのディレクトリ
        * /usr/lib/systemd/system
            * 永続的なユニット・ターゲットの定義ファイルが置かれるディレクトリ
        * /etc/systemd/system
            * カスタム用のディレクトリ
            * 優先度が高く反映される
            * default.targetが置かれる
        * /run/systemd/system
            * 再起動すると削除されるディレクトリ
    * systemctl
        * サービス管理コマンド。serviceやchkconfigの互換は含まれているが利用は推奨されていない
        * systemctl サブコマンド [ユニット名|定義ファイル]
        * ユニットに関するサブコマンド
            * start,stop
                * サービスを開始、停止
            * enable,disable
                * 自動起動を設定、解除
            * list-units
                * ユニットの一覧を表示する
            * isolate
                * 他のユニットを停止して対象のユニットを起動
            * set-default
                * デフォルトターゲットを設定
        * システム全体の起動停止に関わるコマンド
            * poweroff
                * 電源オフ
            * emergency
                * 緊急モード
            * rescue
                * レスキューモード
            * default
                * 標準のモード
            * reboot
                * 再起動
    * Unit(ユニット)
        * systemdの起動処理はUnitと呼ばれる単位に分かれている
        * これまではシェルスクリプトだったのに比べて柔軟に起動ができるようになった、起動時間が短くなった
        * 主要なUnit。拡張子になる
            * service
                * 各種のサービスを起動停止する
            * mount
                * ファイルシステムをマウント
            * device
                * udevによって認識されたデバイス
            * target
                * ユニットをグループ化
    * systemd-delta
        * ファイルが上書きされたか変更されたかを確認できる

### システムのリカバリ

* BIOS
    * 電源を入れると起動する基本的な制御プログラム
    * BIOSがMBR(Master Boot Record)を読み込み、ブートローダに制御を移す
    * 起動順序
        * BIOSを使ったシステムの場合、BIOS -> MBRのブートローダ -> カーネル -> init
    * 起動ディスクの上限が2TBまで
* UEFI
    * Unified Extensible Firmware Interface
    * BIOSに変わる新しいファームウェア。完全置き換えでなくBIOSと共存するような使用も多くある
        * GUIベースのセットアップ画面が利用できる
        * メモリ量上限が緩和
    * ESP(EFI System Partition)は/boot/efiにマウントされる
        * FAT16またはFAT32でフォーマットされている必要がある
    * GPTでパーティションを管理。GPTはハードディスク上のパーティションテーブルに関する規格。ESPはGPTの一部
    * eifbootmgrコマンドでEFIの起動エントリを表示、設定
    * 起動手順は、UEFI -> ESP(のブートローダ) -> カーネル -> init
    * systemd-boot
        * systemd独自のUEFIブートマネージャ
    * UEFI shell
        * OSを起動せずにハードウェア周りの設定をシェルで行うことができるシェル環境
    * U-boot
        * Universal Boot Loader
        * オープンソースの組み込み用のブートローダ
    * efiboot.img
        * UEFIでIOSLINUXを使ってブートする際に使われるイメージ
        * FAT16で作成されている
    * /EFI/BOOT/bootx64.efi
        * UEFIのブートマネージャ
    * Secure Boot
        * システム起動にあたって署名の確認を行い、起動を制限する機能
        * shim.efiは、セキュアブート時に最初に読み込まれるUEFIアプリケーション
    * grub64.efi
        * grubを起動するUEFIアプリケーション
* ブートローダ
    * GRUBはGrand Unified Bootloader。Linuxのブートローダ。一般にGRUB2が使われている
    * GRUBのバージョンが0,9x系のものはGRUB Legacy
    * GRUBのバージョンが1.9以降のものをGRUB2と呼ぶ
    * BIOS -> MBRのブートローダ -> カーネル -> init の順にブートプロセスが進行
    * GRUBシェルはgrubコマンドで対話的に操作できる
    * カーネルをロードする際の起動オプション
        * 数字 指定したランレベルで起動
        * init=<PATH> initの代わりに指定したコマンドを実行。 `init=/bin/bash` でbashシェルを立ち上げることによってメンテナンス用にシステムを起動できる
        * root=<DEVICE> ルートファイルシステムを設定
        * nosmp, maxcpus=0 シングルプロセッサマシンとして動作させる
            * smpはsymmetric multiple processorで、マルチプロセッサの手法のこと
* GRUB Legacy
    * 設定ファイル
        * /boot/grub/menu.lst
        * ディストリビューションによっては、/boot/grub/grub.conf
        * UEFIを使ったシステムでは、/boot/efi/EFI/***/grub.cfg
        * 全体設定のあと、各エントリが記載される
            * title
                * メニューに表示されるエントリ名
            * root
                * パーティションを指定
            * kernel
                * カーネルイメージを指定
            * initrd
                * 初期RAMディスクファイルの指定
    * パーティションとデバイス名との対応は/boot/grub/device.mapに書かれている
* GRUB2
    * 設定ファイル
        * /boot/grub/grub.cfg
        * CentOS7では、/boot/grub2/grub.cfg
        * UEFIを使ったシステムでは、/boot/efi/EFI/redhat/grub.cfg
    * 項目
        * menuentry
            * 各設定の名前を指定する項目
            * GRUB Legacyのtitleに相当
        * set root='(hdディスク番号, パーティション番号)'
            * パーティションを指定
            * GRUB Legacyのrootに相当
        * linux
            * カーネルイメージを指定
            * GRUB Legacyのkernelに相当
        * initrd
            * 初期RAMディスクイメージファイル
    * カーネルイメージや初期RAMディスクイメージの含まれているパーティションを指定する項目は`set root’(hd0,3)’`のように指定
* rdev
    * ルートデバイス、RAMディスクサイズ、ビデオモードの問い合わせと設定
    * 引数なしで起動すると/etc/mtabを表示
    * rdev カーネルイメージ ルートデバイス

### その他のブートローダ

* SYSLINUX
    * FATファイルシステムからLinuxを起動する軽量のブートローダ
    * FAT(File Allocation Table)ファイルシステムは、MS-DOSやWindowsなどのOSで使用されるファイルシステム
    * デバイスにインストールするコマンド
        * syslinux -i|--install デバイス名
* ISOLINUX
    * CD-ROMからカーネルを起動するブートローダ
    * 本体ファイルはisolinux.bin
    * 設定ファイルはisolinux.cfg
* PXELINUX
    * ネットワーク経由でカーネルを起動するブートローダ
    * PXE(Preboot eXecution Environment)は、ネットワーク経由でOSを起動するための規格
    * DHCPやTFTPを使用する
    * 本体ファイルはpxelinux.0
    * 設定ファイルはpxelinux.cfg/ディレクトリに配置
    * 起動順
        * DHCPサーバからTFTPサーバのIPアドレスとブートローダ本体のpxelinux.0のパスを取得する
        * TFTPサーバからpxelinux.0ファイルと設定ファイルを取得
        * 設定ファイルに従って起動
* EXTLINUX
    * ファイルシステムext2/ext3/ext4/btrfsからカーネルを起動するブートローダ
    * インストールコマンド
        * extlinux --install /boot
