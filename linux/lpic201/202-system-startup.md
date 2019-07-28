### システム起動のカスタマイズ

* systemd
    * RHEL7では起動処理はsystemdが行われる。これまではinit/upstartだった
    * Linuxの起動処理
        * BIOS起動
        * ブートローダ呼び出し
        * Linuxカーネル、初期RAMディスク起動
        * Systemd起動。default.targetの命令に従う
    * systemdの機能
        * systemdジャーナルへのロギング
        * 依存関係
        * cgroup
        * サービスの有効化
        * サービス以外に機能
        * リソース管理
    * Unitと呼ばれる単位で管理して設定ファイルを持つ
        * これまではシェルスクリプトだったのに比べて柔軟に起動ができるようになった、起動時間が短くなった
        * 主要なUnit
            * service
                * 各種のサービスを起動停止する
            * mount
                * ファイルシステムをマウント
            * device
                * udevによって認識されたデバイス
            * target
                * ユニットをグループ化
    * サービスの管理はPIDではなくcgroupで管理する
    * systemdのディレクトリ
        * /usr/lib/systemd/system
            * 永続的なユニット・ターゲットの定義ファイルが置かれるディレクトリ
        * /etc/systemd/system
            * カスタム用のディレクトリ
        * /run/systemd/system
            * 再起動すると削除されるディレクトリ
    * systemd-deltaは、ファイルが上書きされたか変更されたかを確認できる
    * systemctl
        * サービス管理コマンド。serviceやchkconfigの互換は含まれているが利用は推奨されていない
        * サブコマンド
            * list-units
                * ユニットの一覧を表示する
            * isolate
                * 他のユニットを停止して対象のユニットを起動
        * https://access.redhat.com/documentation/ja-jp/red_hat_enterprise_linux/7/html/system_administrators_guide/sect-managing_services_with_systemd-services
        * https://dev.classmethod.jp/cloud/aws/service-control-use-systemd/
    * https://access.redhat.com/ja/articles/1379593
    * http://enakai00.hatenablog.com/entry/20130914/1379146157
    * https://access.redhat.com/documentation/ja-jp/red_hat_enterprise_linux/7/html/system_administrators_guide/sect-managing_services_with_systemd-targets
* init
    * すべてのプロセスの親
    * /etc/inittab に処理の定義が書いてある
        * デフォルトランレベル、ランレベルに応じた動作が設定できる
        * 形式は、ID:RunLevel:Act:Cmd
        * Act
            * initdefault
                * デフォルトランレベルを指定
            * sysinit
                * システムのブート時に優先して実行される設定を行うための指定
            * bootwait
                * ブート時に実行され、プロセスの終了を待つ設定を行う
            * respawn
                * プロセスが終了しても自動的に再起動
    * ランレベル
        * ランレベルとターゲット
            * 0: poweroff.target
                * システム終了
            * 1,s,single: rescue.target
                * シングルユーザモード
            * 2-4: multi-user.target
                * マルチユーザモード
            * 5: graphical.target
                * マルチユーザモード(GUI)
            * 6: reboot.target
                * システム再起動
        * Sが起動、Kは終了。K->Sで実施される
        * https://ja.wikipedia.org/wiki/%E3%83%A9%E3%83%B3%E3%83%AC%E3%83%99%E3%83%AB#Linux
        * http://linuxjm.osdn.jp/html/SysVinit/man8/init.8.html
    * telinitは、initプロセスと同様にランレベルを変更することができるコマンド
    * https://www.atmarkit.co.jp/ait/articles/0204/02/news002.html
    * https://kazmax.zpp.jp/cmd/i/inittab.5.html
    * http://linuxjm.osdn.jp/html/SysVinit/man5/inittab.5.html
* chkconfig
    * RedHat系のコマンド
    * 起動スクリプトを管理するコマンド
    * https://tech.nikkeibp.co.jp/it/article/COLUMN/20070805/279115/
* update-rc.d
    * 6.0より前のDebian系のコマンド
    * 自動起動設定を追加・削除することができる
    * 優先順位を指定することができる
    * https://www.fulldigit.co.jp/command/s2_update-rc-d.html
    * https://www.server-world.info/command/html/update-rc.d.html
    * http://www.linuxcertif.com/man/8/update-rc.d/ja/
* insserv
    * SUSE系、6.0以降のDebian系
    * -rオプションで自動起動しない設定
* service
    * Linuxデーモン(サービス)の起動や停止、ステータスの確認を実行する
    * rcスクリプトの実体は/etc/init.dに収められているシェルスクリプト
    * https://tech.nikkeibp.co.jp/it/article/COLUMN/20070605/273739/
    * https://www.atmarkit.co.jp/ait/articles/0206/04/news001.html
    
### システムのリカバリ

* GRUB Legacy, GRUB2
    * Grand Unified Bootloader。Linuxのブートローダ。一般にGRUB2が使われている
    * GRUBのバージョンが0,9x系のものをGRUB Legacy 1.9以降のものをGRUB2と呼ぶ
    * BIOS -> MBRのブートローダ -> カーネル -> init の順にブートプロセスが進行
    * BIOSがMBR(Master Boot Record)を読み込み、ブートローダに制御を移す
    * grub-install, grub2-installでインストール
        * https://gentoo.reichsarchiv.jp/item/13
    * GRUBシェルはgrubコマンドで対話的に操作できる
    * GRUB2への移行
        * https://www.ibm.com/developerworks/jp/linux/library/l-grub2/
    * ブートローダ
        * https://lpi.or.jp/lpic_all/linux/intro/intro05.shtml
        * http://www.obenri.com/_install_cent6/loader_cent6.html
        * http://qref.sourceforge.net/quick/ch-tips.ja.html
* GRUB Legacy
    * 設定ファイルは /boot/grub/menu.lst, ディストリビューションによっては/boot/grub/grub.conf, UEFIを使ったシステムは /boot/efi/EFI/***/grub.cfg
    * title は各設定の名前を指定する項目
    * GRUBを設定する
        * http://www.usupi.org/sysad/201.html
* GRUB2
    * 設定ファイルは /boot/grub/grub.cfg, CentOS7では/boot/grub2/grub.cfg
    * menuentryは各設定の名前を指定する項目(GRUBのtitleに相当)
* UEFI
    * Unified Extensible Firmware Interface
    * BIOSに変わる新しいファームウェア
    * 完全置き換えでなくBIOSと共存するような使用も多くある
    * UEFI -> ESP(のブートローダ) -> カーネル -> init の順にブートプロセスが進行
    * UEFI shellは、OSを起動せずにハードウェア周りの設定をシェルで行う
    * eifbootmgr
        * UEFIブートマネージャの起動エントリをOS上から操作するコマンド
    * systemd-boot
        * systemd独自のUEFIブートマネージャ
    * U-boot
        * オープンソースの組恋用のブートローダ
    * efiboot.img
        * UEFIでIOSLINUXを使ってブートする際に使われるイメージ
    * /EFI/BOOT/bootx64.efi
        * UEFIのブートマネージャ
    * Secure Boot
        * システム起動にあたって署名の確認を行い、起動を制限する機能
    * shim.efi
        * セキュアブート時に最初に読み込まれるUEFIアプリケーション
    * grub64.efi
        * grubを起動するUEFIアプリケーション
    * http://www.dosv.jp/feature/1103/17.htm

### その他のブートローダ

* SYSLINUX
    * https://ja.wikipedia.org/wiki/SYSLINUX
    * FATファイルシステムからLinuxを起動する軽量のブートローダ
    * FATファイルシステムはMS-DOSやWindowsなどのOSで使用されるファイルシステム
    * デバイスにインストールするコマンド
        * syslinux -i|--install デバイス名
* ISOLINUX
    * CD-ROMで使用されるISO9660ファイルシステムからLinuxを移動するブートローダ
* PXELINUX
    * PXE(Preboot Execution Environment)を使用してネットワークサーバからLinuxを起動するブートローダ
    * 起動順
        * DHCPサーバからTFTPサーバのIPアドレスとブートローダ本体のpxelinux.0のパスを取得する
        * TFTPサーバからpxelinux.0ファイルと設定ファイルを取得
        * 設定ファイルに従って起動
    * https://tech.nikkeibp.co.jp/it/article/Keyword/20080530/305494/
* EXTLINUX
    * ファイルシステムext2/ext3/ext4/btrfsからLinuxを起動するブートローダ
    * インストールコマンド
        * extlinux --install /boot
