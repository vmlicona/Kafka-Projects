package com.kafka.project;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class SimpleConsumer {

    public static void main(String[] args) {
        // 1. Set Consumer Properties
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        
        // Required: Define which consumer group this consumer is part of.
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "my-first-application");
        
        // Optional: Start reading from the beginning of the topic if no offset is found.
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // 2. Create the Consumer
        // The Consumer takes a key of type String and a value of type String
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // 3. Subscribe the consumer to our topic(s)
        String topicName = "first-topic";
        consumer.subscribe(Collections.singletonList(topicName));
        
        System.out.println("Waiting for messages in topic: " + topicName);

        // 4. Poll for new data in an infinite loop
        try {
            while (true) {
                // The consumer polls the broker for new messages every 100ms
                // `records` is a collection of all messages received in this poll
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                // Iterate through all received records
                for (ConsumerRecord<String, String> record : records) {
                    // Print the message we received
                    System.out.println("\n--- New Message ---");
                    System.out.println("Key: " + record.key());
                    System.out.println("Value: " + record.value());
                    System.out.println("Partition: " + record.partition());
                    System.out.println("Offset: " + record.offset());
                }
            }
        } finally {
            // 5. Close the consumer gracefully when the program is interrupted (Ctrl+C)
            consumer.close();
        }
    }
}