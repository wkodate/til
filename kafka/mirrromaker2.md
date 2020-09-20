MirrorMaker2
===

## Documentations

[KIP-382](https://cwiki.apache.org/confluence/display/KAFKA/KIP-382%3A+MirrorMaker+2.0)

[A look inside Kafka Mirrormaker 2 - Cloudera Blog](https://blog.cloudera.com/a-look-inside-kafka-mirrormaker-2/)


## Feature

* Change configuration dynamically
* Sync cluster properties across clusters
* Improve performance by reducing rebalances

## Architecture

MirrorMaker2 based on Kafka Connector framework (Kafka source and sink connector)

As with MirrorMaker1, remote-consume and local-produce is recommended.

For bidirectional active-active setup, Automatic naming of replicated topics by MirrorMaker2 between 2 datacenters