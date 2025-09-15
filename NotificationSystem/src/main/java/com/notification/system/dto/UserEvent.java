package com.notification.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEvent {
    public enum EventType {
        LIKE,
        COMMENT,
        FRIEND_REQUEST,
        SHARE
    }

    private EventType type;
    private String actorUser; // Who performed the action
    private String targetUser; // Who receives the notification
    private String details; // e.g., "liked your post", "sent you a friend request"
    private LocalDateTime timestamp;
}