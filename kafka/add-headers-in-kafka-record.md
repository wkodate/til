Add Headers in Kafka Record
==

## Overview

I tried to add headers in kafka record.

This is an operation on MacOS.

#### Data Flow

`Kafka Producer(Java) -> Kafka(http://localhost:9200) -> Kafka Consumer(kafkacat)`

## Message protocol

```
Message =>
        Length => varint
        Attributes => int8
        TimestampDelta => varlong
        OffsetDelta => varint
        KeyLen => varint
        Key => data
        ValueLen => varint
        Value => data
        Headers => [Header] <------------ NEW Added Array of headers
         
Header =>
        Key => string (utf8) <------------------------------- NEW UTF8 encoded string (uses varint length)
        Value => bytes  <------------------------------------ NEW header value as data (uses varint length)
```

Ref. [KIP-82#Wire protocol change - add array of headers to end of the message format](https://cwiki.apache.org/confluence/display/KAFKA/KIP-82+-+Add+Record+Headers#KIP82AddRecordHeaders-Wireprotocolchange-addarrayofheaderstoendofthemessageformat)

## Run Kafka cluster

Run Kafka and ZooKeeper.

```
$ zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties & kafka-server-start /usr/local/etc/kafka/server.properties

[2020-09-26 20:38:54,947] INFO [KafkaServer id=0] started (kafka.server.KafkaServer)

$ jps
95697 Kafka
96359 Jps
78348 QuorumPeerMain
```

Kafka version is 2.4.1
```
$ /usr/local/Cellar/kafka/2.4.1/bin/kafka-topics --version
2.4.1 (Commit:c57222ae8cd7866b)
```

## Kafka Producer

This is a Producer code. 

[HeaderKafkaProducer.java](https://github.com/wkodate/til/blob/master/kafka/src/main/java/com/wkodate/kafka/HeaderKafkaProducer.java)
```
private List<Header> headers;

public void setHeader(String key, String value) {
    Header header = new Header() {
        @Override
        public String key() {
            return key;
        }

        @Override
        public byte[] value() {
            return value.getBytes();
        }
    };
    headers.add(header);
}

producer.setHeader("header-key1", "header-value1");
producer.setHeader("header-key2", "header-value2");

producer.send(new ProducerRecord<String, String>(topic, null, null, message, headers));
```

We can append headers by [ProducerRecord](https://downloads.apache.org/kafka/2.4.1/javadoc/org/apache/kafka/clients/producer/ProducerRecord.html) class.

2 header of `header-key1`:`header-value1` and `header-key2`:`header-value2` are added to the records.

Also, [Headers](https://downloads.apache.org/kafka/2.4.1/javadoc/org/apache/kafka/common/header/Headers.html) class has `add(String key, byte[] value)` method that can add header's key-value pair after creating `ProducerRecord` instance.

Example
```
ProducerRecord<String, String> records = new ProducerRecord<>(topic, null, null, message);
records.headers().add("header-key1", "header-value1".getBytes());
records.headers().add("header-key2", "header-value2".getBytes());
```

## Kafka Consumer

Use [kafkacat](https://github.com/edenhill/kafkacat) as Kafka consumer. It is easy to setup Kafka Consumer by a command line only.

Command
```
$ kafkacat -b localhost:9092 -C -t header-test -o earliest -f 'Headers: %h: Message value: %s\n'
```


## Run Producer and confirm data by kafkacat

Consumed data after running the producer.

kafkacat output

```
$ kafkacat -b localhost:9092 -C -t header-test -o earliest -f 'Headers: %h: Message value: %s\n'
Headers: header-key2=header-value1,header-key2=header-value2: Message value: This is a message1.
Headers: header-key1=header-value1,header-key2=header-value2: Message value: This is a message2.
% Reached end of topic header-test [0] at offset 2
```

I confirmed that records contain message and headers as expected.

## Reference

[KIP-82 - Add Record Headers](https://cwiki.apache.org/confluence/display/KAFKA/KIP-82+-+Add+Record+Headers)