package com.hps.communication.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hps.communication.model.EventMessage;
import com.hps.communication.repository.EventMessageRepository;
import com.hps.communication.service.EmailService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class KafkaMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageListener.class);
    private final Map<String, Long> messageTimestamps = new ConcurrentHashMap<>();
    private static final long DUPLICATE_TIME_WINDOW_MS = 5000; // 5 seconds window

    @Autowired
    private EmailService emailService;

    @Autowired
    private EventMessageRepository eventMessageRepository;

    @KafkaListener(topics = "notificationTopic", groupId = "myGroup")
    public void listenForNotifications(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        String eventType = record.value();
        logger.debug("Received Kafka message for event type: {}", eventType);

        long now = System.currentTimeMillis();
        Long lastSeen = messageTimestamps.getOrDefault(eventType, 0L);
        if (now - lastSeen < DUPLICATE_TIME_WINDOW_MS) {
            logger.info("Duplicate message detected, skipping processing for: {}", eventType);
            acknowledgment.acknowledge();
            return;
        }

        messageTimestamps.put(eventType, now); // Update last seen time

        EventMessage eventMessage = eventMessageRepository.findByEventType(eventType).orElse(null);
        if (eventMessage != null) {
            logger.info("Found EventMessage for eventType: {}. Processing...", eventType);
            emailService.sendEmailsToSubscribedUsers(eventType, eventMessage.getSubject()+" (event_type:"+eventType+")", eventMessage.getMessage()+"\n Mssg_Type:"+eventType.toUpperCase());
        } else {
            logger.warn("No EventMessage found for eventType: {}", eventType);
        }
        acknowledgment.acknowledge();
    }
}
