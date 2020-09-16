package com.wkodate.kafka;

import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class SimpleKafkaProducer {

    private final Producer<String, String> producer;

    private final String topic;

    public SimpleKafkaProducer(Properties props, String topic) {
        producer = new KafkaProducer<>(props);
        this.topic = topic;
    }

    public void send(List<String> messages) {
        for (int i = 0; i < messages.size(); i++) {
            producer.send(new ProducerRecord<>(topic, null, messages.get(i)));
        }
    }

    public void close() {
        producer.close();
    }

}
