package com.notification.system.service;

import com.notification.system.dto.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventProducerService {

    // The KafkaTemplate is automatically configured by Spring Boot
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    // Define the topic name
    private static final String TOPIC = "user-notifications";

    public void sendUserEvent(UserEvent userEvent) {
        log.info("Producing event -> {}", userEvent);

        // Create a Spring Message object. The payload is our UserEvent.
        // We can also add headers, like a key for partitioning.
        Message<UserEvent> message = MessageBuilder
                .withPayload(userEvent)
                .setHeader(KafkaHeaders.TOPIC, TOPIC)
                .setHeader(KafkaHeaders.KEY, userEvent.getActorUser()) // Use actor's name as key
                .build();

        // Send the message to the topic
        kafkaTemplate.send(message);
    }
}