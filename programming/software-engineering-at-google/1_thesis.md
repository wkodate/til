I. Thesis
===

# 1. ソフトウェアエンジニアリングとは何か

「ソフトウェアエンジニアリングとは時間で積分したプログラミングである」

プログラミングのタスクは開発、ソフトウェアエンジニアリングのタスクは開発に加えて修正、保守。

プログラミングとソフトウェアエンジニアリングの違いは、時間、スケール、トレードオフ。

## 1.1　時間と変化

コードの想定保守時間はどのくらいか。この長さによってコードをどう保守すればいいかが異なってくる。時間が長いほど変化がより重要。

Hyrum(ハイラム)の法則: ユーザに提供する仕様に対して、開発者が意図しない使い方をしそれを仕様としてみなすユーザが出てくる。時間の経過によってそういった依存が増えてくるので、変更が困難になる

リファクタリングやライブラリのアップグレードなど、必ずしも変更する必要はないが、変更可能な状態にはしておく。

## 1.2 スケールと効率

「変更すべき据えてのものを安全に変更可能で、かつコードベースの存続期間を通してそれが可能であるとき、組織のコードベースは持続可能である」

組織が繰り返し実行しなければならないのであれば、人的リソースの点ですべてスケーラブルであるべき

Beyonce(ビヨンセ)ルール: インフラの変更で問題が起きるのが嫌ならCIでテストをしよう。逆にCIのテストで問題がなかったのだったならそれはしょうがない

## 1.3　トレードオフとコスト

十分な情報に基づいてトレードオフに関する決定する。誰かがそう言ったから、の理由はダメ。

時間の経過に応じて過去の意思決定を再考する必要が出てくる。間違いを認め、計画を変更しなければならない

## 1.4　ソフトウェアエンジニアリング対プログラミング

ソフトウェアエンジニアリングが上位にあるということではなく、それらは2つの異なる問題領域を表象している。