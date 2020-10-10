Setting quotas
==

Kafka cluster has the ability to enforce quotas by  user or client-id level.

Configuration of  quota for client-id=clientA.

```
$ ./bin/kafka-configs.sh  --bootstrap-server localhost:9092 --alter --add-config 'producer_byte_rate=1024,consumer_byte_rate=2048,request_percentage=200' --entity-type clients --entity-name clientA
Updated config for entity: client-id 'clientA'.
```

By default, the quota is unlimited.

Noted: `client-id` is different from `group-id`

`group.id`: https://kafka.apache.org/documentation/#group.id

`client.id`" https://kafka.apache.org/documentation/#client.id
