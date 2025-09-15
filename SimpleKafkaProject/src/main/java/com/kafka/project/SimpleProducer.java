package com.kafka.project;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class SimpleProducer {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 1. Set Producer Properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // 2. Create the Producer
        // The Producer takes a key of type String and a value of type String
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // Create a topic name. (Make sure this topic exists on your Kafka server!
        // You can create it using the CLI: `kafka-topics --create --topic first-topic --bootstrap-server localhost:9092`)
        String topicName = "first-topic";

        // 3. Send messages in a loop
        for (int i = 1; i <= 10; i++) {
            String value = "System Kafka #" + i;
            String key = "id_" + i;

            // Create a producer record. This represents the message.
            ProducerRecord<String, String> record = new ProducerRecord<>(topicName, key, value);

            System.out.println("Sending message: " + value);

            // Send data - asynchronous operation
            // The .get() call makes it synchronous (wait for the result). Good for learning, bad for production.
            RecordMetadata metadata = producer.send(record).get();

            // Print metadata about where the message was sent (partition, offset)
            System.out.println("Message sent successfully to topic " + metadata.topic()
                    + ", partition " + metadata.partition()
                    + ", offset " + metadata.offset());
        }

        // 4. Flush and close the producer
        producer.flush(); // Wait for any remaining messages to be sent
        producer.close(); // Close the connection
    }
}