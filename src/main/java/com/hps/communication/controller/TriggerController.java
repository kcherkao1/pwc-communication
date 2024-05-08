package com.hps.communication.controller;

import com.hps.communication.scheduler.MessageScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trigger")
public class TriggerController {

    @Autowired
    private MessageScheduler messageScheduler;

    @PostMapping("/sendMessages")
    public String triggerSendingMessages() {
        messageScheduler.sendMessages();
        return "Triggered sending messages.";
    }
}
