package com.hps.communication.repository;

import com.hps.communication.model.EventMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventMessageRepository extends JpaRepository<EventMessage, String> {
    Optional<EventMessage> findByEventType(String eventType);
}
