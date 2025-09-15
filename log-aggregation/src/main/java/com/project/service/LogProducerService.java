package com.project.log_aggregation.service;

import com.project.log_aggregation.log.LogMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogProducerService {

    private final KafkaTemplate<String, LogMessage> kafkaTemplate;
    private static final String RAW_LOGS_TOPIC = "raw-logs";
    
    private final Random random = new Random();
    private final List<String> applications = Arrays.asList("web-server-1", "web-server-2", "auth-service", "payment-service");
    private final List<LogMessage.LogLevel> levels = Arrays.asList(
        LogMessage.LogLevel.INFO, 
        LogMessage.LogLevel.DEBUG, 
        LogMessage.LogLevel.WARN, 
        LogMessage.LogLevel.ERROR
    );
    
    private final List<String> infoMessages = Arrays.asList(
        "User login successful", 
        "Request processed successfully", 
        "Cache hit", 
        "Database connection established"
    );
    
    private final List<String> errorMessages = Arrays.asList(
        "Database connection failed", 
        "Null pointer exception", 
        "Page not found", 
        "Authentication failed", 
        "Payment processing error"
    );

    // Generate a random log message every 2 seconds
    @Scheduled(fixedRate = 2000)
    public void generateRandomLog() {
        String application = applications.get(random.nextInt(applications.size()));
        LogMessage.LogLevel level = levels.get(random.nextInt(levels.size()));
        
        String messageContent;
        if (level == LogMessage.LogLevel.ERROR) {
            messageContent = errorMessages.get(random.nextInt(errorMessages.size()));
        } else {
            messageContent = infoMessages.get(random.nextInt(infoMessages.size()));
        }
        
        LogMessage logMessage = LogMessage.of(application, level, messageContent);
        
        kafkaTemplate.send(RAW_LOGS_TOPIC, application, logMessage);
        log.info("Produced log: {} - {}: {}", application, level, messageContent);
    }
}