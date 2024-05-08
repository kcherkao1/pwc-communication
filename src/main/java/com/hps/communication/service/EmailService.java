package com.hps.communication.service;

import com.hps.communication.model.User;
import com.hps.communication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class EmailService {

    public static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    // This method fetches all subscribed users for the event type and sends them an email individually
    public void sendEmailsToSubscribedUsers(String eventType, String subject, String messageText) {
        List<User> users = userRepository.findByEnabledTrueAndEventType(eventType);
        users.forEach(user -> sendEmail(user.getEmail(), subject, messageText));
    }

    // This method sends a single email to multiple users with each email as BCC
    public void sendBatchEmail(List<String> emails, String subject, String messageText) {
        if (emails.isEmpty()) {
            logger.info("No emails to send for subject: {}", subject);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setBcc(emails.toArray(new String[0]));
        message.setSubject(subject);
        message.setText(messageText);

        try {
            mailSender.send(message);
            logger.info("Batch email sent to {} users for subject: {}", emails.size(), subject);
        } catch (Exception e) {
            logger.error("Failed to send batch email for subject {}: {}", subject, e.getMessage());
        }
    }

    // Helper method to send an email to a single recipient
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            mailSender.send(message);
            logger.info("Email sent to: {}", to);
        } catch (Exception e) {
            logger.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
