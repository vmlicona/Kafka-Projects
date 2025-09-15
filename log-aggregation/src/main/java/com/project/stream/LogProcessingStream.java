package com.project.log_aggregation.stream;


import com.project.log_aggregation.log.LogMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class LogProcessingStream {

    private static final String RAW_LOGS_TOPIC = "raw-logs";
    private static final String CRITICAL_LOGS_TOPIC = "critical-logs";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Bean
    public KStream<String, LogMessage> processLogs(StreamsBuilder streamsBuilder) {
        // Create JsonSerde for LogMessage
        JsonSerde<LogMessage> logMessageSerde = new JsonSerde<>(LogMessage.class);
        
        // Build the stream from the raw-logs topic
        KStream<String, LogMessage> stream = streamsBuilder.stream(
            RAW_LOGS_TOPIC, 
            Consumed.with(Serdes.String(), logMessageSerde)
        );

        // Process the stream: filter, transform, and branch
        stream
            // 1. Filter: Only keep ERROR logs
            .filter((key, logMessage) -> {
                boolean isError = logMessage.getLevel() == LogMessage.LogLevel.ERROR;
                if (isError) {
                    log.info("Filtered ERROR log from {}: {}", logMessage.getApplication(), logMessage.getMessage());
                }
                return isError;
            })
            // 2. Transform: Enrich and format the log message
            .mapValues((logMessage) -> {
                // Create an enriched, human-readable message
                return String.format("[CRITICAL] %s - %s - %s: %s (Thread: %s)",
                    logMessage.getTimestamp().format(formatter),
                    logMessage.getApplication(),
                    logMessage.getLevel(),
                    logMessage.getMessage(),
                    logMessage.getThread()
                );
            })
            // 3. Send to the critical-logs topic
            .to(CRITICAL_LOGS_TOPIC, Produced.with(Serdes.String(), Serdes.String()));

        return stream; // Return the stream (Spring will manage it)
    }
}