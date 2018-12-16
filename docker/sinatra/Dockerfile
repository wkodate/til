# 基となるイメージを指定
FROM ubuntu:14.04
# 誰がこの新しいイメージを管理するか
MAINTAINER Kate Smith <ksmith@example.com>
# イメージの中で実行するコマンドを実行
RUN apt-get update && apt-get install -y ruby ruby-dev
RUN gem install sinatra
