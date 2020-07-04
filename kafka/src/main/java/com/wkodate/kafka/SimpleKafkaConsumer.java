package com.wkodate.kafka;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class SimpleKafkaConsumer {

    private final KafkaConsumer<String, String> consumer;

    private ConsumerRecords<String, String> consumerRecords;

    private final AtomicBoolean closed = new AtomicBoolean(false);

    public SimpleKafkaConsumer(Properties props, String topic) {
        this(props, Collections.singletonList(topic));
    }

    public SimpleKafkaConsumer(Properties props, List<String> topics) {
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(topics);
    }

    public ConsumerRecords<String, String> consume() {
        long start = System.currentTimeMillis();
        ConsumerRecords<String, String> records;
        while (System.currentTimeMillis() - start < 10000) {
            records = consumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                consumerRecords = records;
                break;
            }
        }
        return consumerRecords;
    }

    public void shutdown() {
        closed.set(true);
        consumer.wakeup();
    }
}
