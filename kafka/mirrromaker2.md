MirrorMaker2
===

## Documentations

[KIP-382](https://cwiki.apache.org/confluence/display/KAFKA/KIP-382%3A+MirrorMaker+2.0)

[A look inside Kafka Mirrormaker 2 - Cloudera Blog](https://blog.cloudera.com/a-look-inside-kafka-mirrormaker-2/)

## Limitation of MM1

MM1 was insufficient for some use cases.

Because...

* Topic that are created will need to be repartitioned manually.
* ACL and configuration changes are not synced accross clusters.
* Rebalancing causes latency spikes.
* There are no support for migration of producer or consumer between mirrored clusters.
* There are no support for exactly-once delivery.
* No support active-active pairs.

## MM2 design

* Kafka Connect framework. (including both source and sink connector)
* Detect new topics and partitions.
* Change configuration dynamically.
* Sync cluster properties across clusters
* Manages downstream topic ACL
* Support active/active cluster
* Improve performance by reducing rebalances


