package com.hps.communication.repository;

import com.hps.communication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    // Find all users who are enabled and subscribed to a specific event type
    List<User> findByEnabledTrueAndEventType(String eventType);


}
