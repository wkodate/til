Structured Logging: The Best Friend You’ll Want When Things Go Wrong
==

https://engineering.grab.com/structured-logging

## Introduction

* 複数のサービスを持つバックエンドシステムを維持するためにはObservabilityが重要
* 良いログが状況の理解、メンテナンス、デバッグをより簡単にする
* Structured loggingと呼ばれるロギングの形式を説明

## What are Logs?

* ログの特徴
  * Log Format
    * syslogのようなシンプルなkey-valueや構造化されたJSONのようなフォーマットがある
    * 構造化されたログはサイズが大きくなるが、検索しやすくリッチな情報が含まれる
  * Levelled logging(Log Level)
    * 重要度に合わせたロギング
  * Log Aggregation Backend
    * Kibana, Splunkなど別のバックエンドシステムで集約
  * Casual Ordering
    * 正確なタイムスタンプで保存されイベントの順番を決定
  * Log Correlation
    * 特徴のあるリクエストやイベントをログと付き合わせ

## The State of Logging at Grab

## Why Change?

## Structured Logging

## But Why Structured Logging?

