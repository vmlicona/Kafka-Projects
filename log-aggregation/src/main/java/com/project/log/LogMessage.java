package com.project.log_aggregation.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogMessage {
    public enum LogLevel {
        INFO, DEBUG, WARN, ERROR, FATAL
    }

    private String application;
    private LogLevel level;
    private String message;
    private String thread;
    private LocalDateTime timestamp;
    
    // Helper method to create log messages easily
    public static LogMessage of(String application, LogLevel level, String message) {
        return new LogMessage(application, level, message, Thread.currentThread().getName(), LocalDateTime.now());
    }
}