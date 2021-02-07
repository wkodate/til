Glossary
===

### Worker

キューイングされたOperatorの処理を実行

### Executor

Taskが実行される。Sequential ExecutorならScheduler内のプロセスで実行される。CeleryExecutorを使用する場合、Operatorのキューイングを行い、各ワーカーにOperatorの処理を分散

SequentialExecutorはparallelism=1のLocalExecutorと同じ

### Celery

workerを分散するのに使われるミドルウェア。セロリと読む。

### Flower

Celeryのモニタリング

### XComs

cross-communication, タスク間でのメッセージのやりとりができる

### Macros

Jinjaテンプレートで使われる変数

