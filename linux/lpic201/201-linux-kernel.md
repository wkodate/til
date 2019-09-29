Linuxカーネル
===

### カーネルの構成要素

* カーネルのバージョン
    * 2.6より前は、2.X.YのXが奇数だと開発版、偶数だと安定版
    * 2.6以降は-が付いていなければ安定版、付いていれば開発版やリリース候補版
* リリースされるLinuxカーネルのカテゴリ
    * prepatch
        * リリース候補のRC(Release Candidate)版
    * mainline
        * prepatchの後にリリースされる正式版
    * stable
        * mainlineの後にリリースされる正式版。mainlineで発見された不具合が修正された安定版
    * longterm
        * stableの中から選ばれ約2年の長い期間バグフィックスされる。LTS(Long Term Support)とも呼ばれる
* カーネルのバージョンを確認
    * uname -r (uname -a で全ての情報を表示)
    * cat /proc/version
    * cat /usr/src/linux/Makefile
* カーネルイメージ
    * イメージファイルとしてファイルシステム上に、vmlinux-カーネルバージョン、のファイル名で格納されている
    * bzImage
        * カーネルイメージの形式。gzip圧縮。ハイメモリ。今はこっちが主流
    * zImage
        * カーネルイメージの形式。512KB以下のイメージしか扱えないローメモリ。gzip圧縮
* Linuxカーネルはtar.gz形式、もしくはtar.xz形式で以下をアーカイブして配布される
    * カーネルソース
    * カーネルモジュールのソース
    * ドキュメント
    * Makefile

### Linuxカーネルのコンパイル

* カーネルのコンパイル(再構築)を検討する必要がある場合
    * 必要なデバイスドライバがカーネルに含まれていない
    * 使用しているハードウェアに最適化したカーネルを使用したい
    * カーネルのバージョンアップをしたい
* Linuxカーネルのビルドとインストールの流れ
    * 設定を初期化
    * 設定
    * ビルド
    * システムにインストール
* カーネルのソースは `/usr/src/linux` ディレクトリに展開するのが一般的。実際には/usr/src/linux-(VERSION)に対してシンボリックリンクが貼られる
    * .config
        * カーネルのビルド設定ファイル
    * Makefile
        * makeの設定やカーネルバージョンが書いてある
    * kernel/
        * カーネル本体のソースが入っている
    * Documentation/
        * ドキュメントが入っている
* カーネルの設定
    * `/usr/src/linux/.config`ファイルに記録される
    * make config
        * 項目毎に対話的に設定を行う
    * make oldconfig
        * 現在のカーネルの設定を引き継ぐ。バージョンアップでなければ実行する必要はない
    * make cloneconfig
        * make oldconfigと同じ
    * make manuconfig
        * ターミナル上のGUIで設定を行う
    * make xconfig
        * X上のGUIで設定を行う
    * make defconfig
        * デフォルト状態の設定ファイルを作る
* カーネル、カーネルモジュールのコンパイル、インストール
    * make
        * カーネルとモジュールをすべてビルド
        * make allと同じ挙動
    * make modules
        * カーネル本体は含めずカーネルモジュールをビルドする
    * make install
        * ビルドしたカーネルをインストール
        * やっていること
            * ビルドしたカーネルイメージファイルbzImageをvmlinuz-(VERSION)として/bootに置く
            * System.mapをSystem.map-(VERSION)として/bootに置く
            * /boot/vmlnuz, /boot/System.mapにシンボリックリンクを貼る
            * mkinitramfsまたはmkinitrdで初期RAMディスクを作成し、/boot/initrd.img-(VERSION)とする
            * ブートローダに新しいカーネル用の設定を追加する。GRUB Legacyなら/boot/grub/menu.lst、GRUB2なら/etc/grub.dディレクトリ配下のファイルを適切に編集する
    * make modules_install
        * カーネルモジュールをインストール。ディレクトリは/lib/modules/(VERSION)/
    * make rpm
        * ビルド成果物を使ってrpmパッケージを作る
    * make rpm-pkg
        * ソースコードを含むrpmパッケージを作る
    * make clean
        * 一時ファイル等を削除(設定ファイルは残す)
    * make mrproper
        * 一時ファイル等を削除(設定ファイルも削除)。cleanの設定ファイルも消す版
* ビルド・インストールしたカーネルは/bootに格納される
    * vmlinuz-(VERSION)
        * カーネルイメージ
    * System.map-(VERSION)
        * カーネルのアドレスマップ
        * アドレスマップは、メモリ上に展開される際のシンボルとアドレスのマッピングを記述したファイル
    * initrd.img-(VERSION)
        * 初期RAMディスク
    * grub/
        * GRUB2のファイル
    * config-<VER>
        * カーネルコンフィギュレーション
* 初期RAMディスク
    * 段階的にブートを実現する機能。システム起動時に仮の環境としてメモリ上にファイルシステムを展開し、そこでカーネルを動作させてから本来のファイルシステムをルートにマウントし直す
    * initrd
        * ファイルシステムイメージをgzip圧縮した初期RAMディスク
        * イメージ内のファイルを参照するには、gunzipで解凍した後、mountでファイルシステムとしてマウントする
        * mount -o loopでマウント
    * initramfs
        * cpioアーカイブをgzip圧縮した初期RAMディスク
        * 最近はこっちが使われることが多い
        * -o 出力先を指定
        * dracutコマンドでも作成できる
* DKMS(Dynamic Kernel Module Support)
    * カーネルとモジュールの依存関係を解消して、カーネルモジュールを自動的に生成

### カーネルの管理とトラブルシューティング

* カーネルモジュール
    * ファイルの拡張子は.ko
    * /lib/modules/カーネルバージョン ディレクトリにカーネルモジュールが配置される
    * lsmod
        * 現在ロードされれているモジュールを全て表示
        * `/proc/modules` からも同様の情報を取得可能
    * modinfo
        * モジュールを指定してその情報を表示
        * -n, --filename モジュールのファイル名を指定
    * insmod
        * モジュールを動的ロード
    * rmmod
        * 指定したロード済みモジュールをアンロード
    * modprobe
        * module probe
        * 依存関係を考慮してモジュールを追加削除できるコマンド
        * modules.depを利用して依存関係を解決している
            * modules.depファイルは、カーネルモジュールのそれぞれが、別のどのカーネルモジュールに依存しているかという依存関係情報が書かれているファイル
        * -f,—force 強制的に実行
        * -r, —remove アンロード
        * -lオプションはロードできるモジュールの一覧を表示
        * 設定ファイルは/etc/modprobe.conf、最近では/etc/modprobe.d/*.conf
    * depmod
        * カーネルモジューツの依存関係情報ファイルmodules.depを更新する
* カーネルパラメータ
    * カーネル動作の設定をする
    * 設定ファイルは/etc/sysctl.confまたは/etc/sysctl.dディレクトリ配下に配置する
    * OS起動時に実行されるスクリプト内でsysctlコマンドがこのファイルを読み込み、設定内容を反映させる
    * sysctlコマンドもしくは/proc/sys以下の仮想ファイルによってカーネルパラメータを操作する
    * sysctlコマンド
        * `sysctl カーネルパラメータ` そのカーネルパラメータを表示
        * `sysctl -w(--write) カーネルパラメータ=値` カーネルパラメータを変更
        * `sysctl -a(--all)` カーネルパラメータ一覧を表示
    * /proc/sys
        * `cat /proc/sys/kernel/modprobe` 表示
        * `echo "/sbin/modprobe" > /proc/sys/kernel/modprobe` 変更
* /proc
    * プロセス、システムリソースなどの情報を扱うための擬似的なファイルシステム
    * メモリ上に作成されるので、ハードディスク上には存在しない
    * PCに接続されたUSBデバイスの情報を確認する
        * lsusbコマンド、もしくは/proc/bus/usb/devicesでUSBデバイスの情報を確認できる
    * デバイスを表示するコマンド
        * lsusb
            * USBデバイスに関する情報を表示
            * /proc/bus/usb/devicesにも書かれている
        * lspci
            * PCIデバイスに関する情報を表示
        * lsdev
            * ハードウェアに関する情報を一覧表示
            * /prod/dma, /proc/interrupts, /proc/ioportsファイル
* udev
    * デバイスファイルを動的に管理するための仕組み
    * udevdはudevのデーモン
    * ユーザ空間で動作するため、カーネル空間の情報はsysfsから得ている
    * udevの動作
        * デバイスを接続
        * カーネルが仮想ファイルシステムsysfs(/sys)にデバイス情報を作成
        * カーネルがudevdにデバイス情報を通知(uevent)
        * udevdが/sysのデバイス情報を確認
        * udevdが/devにデバイスファイルを作成
    * 設定ファイル
        * `/etc/udev/udev.conf` メインの設定ファイル
        * 個別のルールファイルは  `/etc/udev/rules.d` ディレクトリ内に配置する
        * ルールファイルの名前は「12-hoge.rules」のように(2桁の番号)-(ルール名).rulesとする
    * コマンド
        * udevinfo
            * udevが認識しているデバイス情報を表示
        * udevadm info
            * udev関連のコマンドを統合したコマンド
        * udevmonitor
            * udevdの動作状況を監視、コンソールに出力
