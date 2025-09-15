package com.notification.system.controller;

import com.notification.system.dto.UserEvent;
import com.notification.system.service.EventProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class UserActionController {

    private final EventProducerService eventProducerService;

    @GetMapping("/simulate-action")
    public String simulateAction(
            @RequestParam String action,
            @RequestParam String fromUser,
            @RequestParam String toUser) {

        UserEvent.EventType type;
        String details = switch (action.toLowerCase()) {
            case "like" -> {
                type = UserEvent.EventType.LIKE;
                yield "liked your post";
            }
            case "comment" -> {
                type = UserEvent.EventType.COMMENT;
                yield "commented on your post";
            }
            case "friend" -> {
                type = UserEvent.EventType.FRIEND_REQUEST;
                yield "sent you a friend request";
            }
            default -> {
                type = UserEvent.EventType.LIKE;
                yield "performed an action";
            }
        };

        // Simple logic to map action string to EventType

        // Create the event
        UserEvent event = new UserEvent(type, fromUser, toUser, details, LocalDateTime.now());

        // Send it via Kafka
        eventProducerService.sendUserEvent(event);

        return "Event simulated and sent! Check the consumer logs.";
    }
}