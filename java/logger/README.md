Log
===

# Logback

* 設定ドキュメント
    * http://logback.qos.ch/manual/configuration_ja.html
* slf4j+logbackを使ってログを出力する流れ
    * 設定ファイル(logback.xml)の作成
    * ログ出力コードの記述
* クラスパス状にlogback.xmlを用意する。なかったらBasicConfigurationを使う
* 設定ファイルが変更されたら自動で読み込む設定もできる
* 内部実装
    * LoggingEventオブジェクトの作成
        * ロギングリクエストに含まれる必要な情報をすべて格納したLoggingEventオブジェクトを作成
    * Appenderの起動
        * doAppend()メソッドを呼び出す
    * メッセージの書式化
        * Layoutで書式化。LoggingEventのインスタンスを文字列として返す
    * LoggingEventの送信
        * ロギングされたイベントをAppenderの宛先に送信

## 構成要素

* Appender
    * どこにどんなレイアウトで出力するかの出力先
    * コンソールやファイル、DBなど
    * name属性とclass属性は必須
    * layout属性
        * 受け取った変換イベントを文字列に変換
* Logger
    * パッケージやクラスに対してどのログレベルで出力するか
    * name属性とlevel属性は必須
* Root
    * Loggerで指定されていないものについて、どのログレベル以上でどのAppenderに出力するか

## 設定ファイルの構文

* configuration要素
    * appender要素
        * アペンダーの設定
        * name属性とclass属性が必須
    * logger要素
        * ロガーを指定
        * name属性とlevel属性が必要
        * appender-ref要素で参照しているアペンダーを割り当てることができる
    * root要素
        * ルートロガーを指定
        * level属性を指定できる
        * 複数のappender-refを含めることができる
