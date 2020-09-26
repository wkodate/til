package com.wkodate.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class HeaderKafkaProducer extends SimpleKafkaProducer {

    List<Header> headers;

    public HeaderKafkaProducer(Properties props) {
        super(props);
        headers = new ArrayList<>();
    }

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

    public void send(String topic, List<String> messages) {
        for (int i = 0; i < messages.size(); i++) {
            producer.send(new ProducerRecord<String, String>(topic, null, null, messages.get(i), headers));
        }
    }

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        HeaderKafkaProducer producer = new HeaderKafkaProducer(props);

        String topic = "header-test";
        List<String> messages = new ArrayList<>();
        messages.add("This is a message1.");
        messages.add("This is a message2.");
        producer.setHeader("header-key1", "header-value1");
        producer.setHeader("header-key2", "header-value2");
        producer.send(topic, messages);
        producer.close();

    }
}
