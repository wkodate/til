package com.wkodate.kafka;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.Before;
import org.junit.Test;

public class SimpleKafkaConsumerTest {

    private Properties consumerProps;

    @Before
    public void setup() {
        consumerProps = new Properties();
        consumerProps.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProps.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test");
        consumerProps.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        consumerProps.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                                  "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                                  "org.apache.kafka.common.serialization.StringDeserializer");

        consumerProps.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        consumerProps.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

    @Test
    public void consumerTest() throws InterruptedException {
        String topic = "string-topic";
        SimpleKafkaConsumer consumer = new SimpleKafkaConsumer(consumerProps, topic);
        ConsumerRecords<String, String> records = consumer.consume();
        if (records.isEmpty()) {
            return;
        }
        for (ConsumerRecord<String, String> record : records) {
            System.out.println(record);
        }
    }

}
