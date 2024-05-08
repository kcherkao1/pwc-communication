package com.hps.communication.scheduler;

import com.hps.communication.service.KafkaProducerService;
import com.hps.communication.repository.EventRepository;
import com.hps.communication.model.Event;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.hps.communication.service.EmailService.logger;

@Component
public class MessageScheduler {
    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private EventRepository eventRepository;

    @Scheduled(fixedRate = 3600000)
    public void sendMessages() {
        List<Event> events = eventRepository.findAll();
        for (Event event : events) {
            kafkaProducerService.sendMessage("notificationTopic", UUID.randomUUID().toString(), event.getEventType());
            logger.info("Triggering message for event type: {}", event.getEventType());
        }
    }
}
