package com.project.log_aggregation.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CriticalLogConsumer {

    @KafkaListener(
        topics = "critical-logs", 
        groupId = "critical-logs-consumer",
        containerFactory = "kafkaListenerContainerFactory" // Use String deserializer
    )
    public void consumeCriticalLog(String criticalLogMessage) {
        // In a real application, you would:
        // 1. Store in Elasticsearch for searching
        // 2. Send to Splunk/Datadog
        // 3. Trigger alerts (PagerDuty, email, Slack)
        // 4. Create metrics for monitoring
        
        log.warn("🚨 CRITICAL LOG RECEIVED: {}", criticalLogMessage);
        
        // Simulate sending to Elasticsearch
        log.info("📊 Writing to Elasticsearch: {}", criticalLogMessage);
    }
}