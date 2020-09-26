package com.wkodate.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.List;
import java.util.Properties;

public class SimpleKafkaProducer {

    protected final Producer<String, String> producer;

    protected SimpleKafkaProducer(Properties props) {
        producer = new KafkaProducer<>(props);
    }

    protected void send(String topic, List<String> messages) {
        for (int i = 0; i < messages.size(); i++) {
            producer.send(new ProducerRecord<>(topic, null, messages.get(i)));
        }
    }

    protected void close() {
        producer.close();
    }

}
