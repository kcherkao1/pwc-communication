package com.hps.communication;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class EventProducer {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        String topic = "notificationTopic";
        String eventType = "emailAlert";
        String message = "This is a message for email alerts";

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, eventType, message);

        try {
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    System.err.println("Sending failed: " + exception.getMessage());
                } else {
                    System.out.println("Message sent to partition " + metadata.partition() + " with offset " + metadata.offset());
                }
            });
        } finally {
            producer.close();
        }
    }
}
