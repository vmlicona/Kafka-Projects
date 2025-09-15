package com.notification.system.service;

import com.notification.system.dto.UserEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationConsumerService {

    // This method is a listener. It will be called for every message in the 'user-notifications' topic.
    @KafkaListener(
            topics = "user-notifications",
            groupId = "notification-group" // Consumer group ID
    )
    public void handleNotification(@Payload UserEvent event,
                                   @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        // This is where the "real work" of sending a notification would happen.
        // For now, we just log it to the console.

        String notificationMessage = String.format(
                "🔔 NOTIFICATION for %s: %s %s (Triggered by %s, Key: %s)",
                event.getTargetUser(),
                event.getActorUser(),
                event.getDetails(),
                event.getActorUser(),
                key
        );

        log.info(notificationMessage);
        
        // In a real application, you would now:
        // 1. Save the notification to a database for the user to see later.
        // 2. Send a real-time push notification via Firebase Cloud Messaging (FCM) / WebSockets.
        // 3. Send an email.
    }
}