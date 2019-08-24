Linuxカーネル
===

### カーネルの構成要素

* Linuxの安定版
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
    * 参考
        * https://eetimes.jp/ee/articles/0809/01/news127.html
        * https://www.itmedia.co.jp/enterprise/articles/0703/19/news016.html
* カーネルのバージョンを確認
    * uname -r (uname -a で全ての情報を表示)
    * cat /proc/version
    * cat /usr/src/linux/Makefile
    * 参考
        * https://www.atmarkit.co.jp/flinux/rensai/linuxtips/068infokernelver.html
* vmlinux
    * bzImage
        * カーネルイメージの形式。gzip圧縮。ハイメモリ。こっちが主流
    * zImage
        * カーネルイメージの形式。512KB以下のイメージしか扱えないローメモリ。gzip圧縮
    * 参考
        * https://ja.wikipedia.org/wiki/Vmlinux
* Linuxカーネルはtar.gz形式、もしくはtar.xz形式で以下をアーカイブして配布される
    * カーネルソース
    * カーネルモジュールのソース
    * ドキュメント
    * Makefile
    * 参考
        * https://kakurasan.hatenadiary.jp/entry/20070724/p1
        * https://www.express.nec.co.jp/linux/distributions/knowledge/system/kernel.html
        * https://www.atmarkit.co.jp/ait/articles/0808/28/news129.html
        
        
### Linuxカーネルのコンパイル

* 初期RAMディスク
    * http://kwkw.wiki.fc2.com/wiki/Linux%E3%81%AE%E5%88%9D%E6%9C%9FRAM%E3%83%87%E3%82%A3%E3%82%B9%E3%82%AF
    * 段階的にブートを実現する機能。システム起動時に仮の環境としてメモリ上にファイルシステムを展開し、そこでカーネルを動作させてから本来のファイルシステムをルートにマウントし直す
    * initrd形式のイメージは、ファイルシステムイメージをgzip圧縮したもの
    * initramfs形式のイメージは、cpioアーカイブをgzip圧縮したもの
* Linuxカーネルのコンフィギュレーション
    * https://www.atmarkit.co.jp/ait/articles/0808/28/news129.html
    * カーネルコンフィギュレーションは /boot/config-(VERSION) 
* Linuxカーネル・コンパイル入門
    * https://tech.nikkeibp.co.jp/it/article/COLUMN/20071016/284752/
    * カーネルのソースは/usr/src/linuxに展開される。実際には/usr/src/linux-(VERSION)に対してシンボリックリンクが貼られる
* Linux Kernel Restructuring
    * https://www.anarg.jp/personal/t-tugawa/note/linux/kernel_restruct.html
* makeコマンド
    * makeコマンドは、ソースをコンパイルして実行ファイルを作る(ビルドする)
    * make mrproper
        * ビルドする際にソースディレクトリ内を初期化する
    * make clean
        * ビルドする際にソースディレクトリ内を初期化する(設定ファイルは残す)
    * make oldconfig
        * 現在のカーネルの設定を引き継ぐ
    * make config
        * 項目毎に対話的に設定を行う
* /boot
    * System.map-(VERSION) はカーネルがメモリ上に展開される際の、シンボルとアドレスのマッピングを記述したファイル(アドレスマップ)
* カーネルモジュールの依存関係
    * modules.depファイルは、カーネルモジュールのそれぞれが、別のどのカーネルモジュールに依存しているかという依存関係情報が書かれているファイル
    * modprobeコマンドはmodules.depを利用して依存関係を解決している

### カーネルの管理とトラブルシューティング

* udev
    * udevと呼ばれるデバイスファイルを動的に管理する仕組みのデーモン
    * udevの動作
        * デバイスを接続
        * カーネルが仮想ファイルシステム/sysにデバイス情報を作成
        * カーネルがudevdにデバイス情報を通知(uevent)
        * udevdが/sysのデバイス情報を確認
        * udevdが/devにデバイスファイルを作成
        * メイン設定ファイルは/etc/udev/udev.conf
    * 個別のルールファイル
        * /etc/udev/rules.dディレクトリ内に配置する
        * ルールファイルの名前は「12-hoge.rules」のように(2桁の番号)-(ルール名).rulesとする
    * コマンド
        * udevinfo
            * udevが認識しているデバイス情報を表示
        * udevadm info
            * udev関連のコマンドを統合したコマンド
        * udevmonitor
            * udevdの動作状況を監視、コンソールに出力
    * http://www.usupi.org/sysad/114.html
    * http://www.usupi.org/sysad/115.html
* カーネルモジュール
    * ファイルの拡張子は.ko
    * コマンド
        * lsmod
            * 現在有効にされれているモジュールを全て表示
        * modinfo
            * モジュールを指定してその情報を表示
            * -nオプションでモジュールのファイル名
        * depmod
            * カーネルモジューツの依存関係情報ファイルmodules.depを更新する
        * modprobe
            * 依存関係を考慮してロード、アンロードが行えるコマンド
            * -fオプションで強制的に実行
            * -rオプションでロードではなくアンロードする
            * -lオプションはロードできるモジュールの一覧を表示
            * 設定ファイルは/etc/modprobe.conf、最近では/etc/modprobe.d/*.conf
        * rmmod
            * 指定したロード済みモジュールをアンロード
        * insmod
            * モジュールを動的ロード
* カーネルパラメータ
    * カーネル動作の設定
    * カーネルパラメータを設定するファイルは/etc/sysctl.confまたは/etc/sysctl.dディレクトリ配下のファイル
    * OS起動時に実行されるスクリプト内でsysctlコマンドがこのファイルを読み込み、設定内容を反映させる
    * カーネルパラメータを変更する方法は、sysctlコマンドを使う方法と、/proc/sys以下の仮想ファイルに書き込みを行う方法がある
    * sysctlコマンド
        * sysctl カーネルパラメータ でそのカーネルパラメータを表示
        * sysctl -w カーネルパラメータ=値 でカーネルパラメータを変更
        * sysctl -a カーネルパラメータ一覧を表示
    * /proc/sys以下にカーネルパラメータを操作するための仮想ファイルが存在する
    * チューニング https://www.atmarkit.co.jp/flinux/special/proctune/proctune02a.html
* /proc
    * プロセス、システムリソースなどの情報を扱うための擬似的なファイルシステム。メモリ上に作成される
    * /procとは
        * https://tech.nikkeibp.co.jp/it/article/Keyword/20071214/289515/
    * PCに接続されたUSBデバイスの情報を確認する
        * https://www.atmarkit.co.jp/flinux/rensai/linuxtips/510showusbdev.html
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
