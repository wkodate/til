package com.wkodate.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class SimpleKafkaProducerTest {

    private static final String[] SAMPLE_MESSAGES = {
            "Hello.", "Thank you.", "Yes, I can.", "Good morning", "I see", "I'm grad to see you."
    };

    private static final Random RAND = new Random();

    private Properties producerProps;

    @Before
    public void setup() {
        producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        producerProps.put(ProducerConfig.ACKS_CONFIG, "all");
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");
    }

    @Test
    public void sendTest() {
        String topic = "string-topic-v2";
        SimpleKafkaProducer producer = new SimpleKafkaProducer(producerProps);
        List<String> messages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            messages.add(SAMPLE_MESSAGES[RAND.nextInt(SAMPLE_MESSAGES.length)]);
        }
        producer.send(topic, messages);
        producer.close();
    }

}
