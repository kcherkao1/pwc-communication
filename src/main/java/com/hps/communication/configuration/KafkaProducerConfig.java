package com.hps.communication.configuration;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public KafkaProducer<String, String> kafkaProducer() {
        Properties props = new Properties();
        String bootstrapServers = "localhost:9092"; // Default value
        // It's better to externalize configuration like this
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");

        try {
            return new KafkaProducer<>(props);
        } catch (Exception e) {
            System.out.println("Failed to create Kafka Producer: " + e.getMessage());
            return null;
        }
    }
}
