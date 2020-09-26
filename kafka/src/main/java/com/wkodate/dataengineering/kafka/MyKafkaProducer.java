package com.wkodate.dataengineering.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class MyKafkaProducer {
    private Producer<String, String> producer;

    public MyKafkaProducer(Properties props) {
        producer = new KafkaProducer<>(props);
    }

    public void produce(String topic) {
        for (int i = 0; i < 100; i++)
            producer.send(new ProducerRecord<>(topic, Integer.toString(i), Integer.toString(i)));
    }

    public void close() {
        producer.close();
    }

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        MyKafkaProducer producer = new MyKafkaProducer(props);
        producer.produce("test");
        producer.close();
    }
}
