# Social Activity Notifications with Spring Boot and Kafka

This project demonstrates a real-time notification system using Spring Boot and Apache Kafka. It simulates social media actions (likes, comments, friend requests) and generates appropriate notifications.

## Prerequisites

- Java 17 or higher
- Apache Kafka (with Zookeeper)
- Maven

## Setup and Running

1. **Start Kafka Services**
   ```bash
   # Start Zookeeper (in one terminal)
   zookeeper-server-start.sh config/zookeeper.properties

   # Start Kafka Broker (in another terminal)
   kafka-server-start.sh config/server.properties
   ```

2. **Build and Run the Application**
   ```bash
   # Build the project
   mvn clean package
   
   # Run the Spring Boot application
   java -jar target/your-application-name.jar
   ```
   Or run directly from your IDE if preferred.

## Testing the Application

### Using the REST Controller

Once the application is running, you can test the producer by visiting these URLs in your browser:

- **Like Action**:
  `http://localhost:8080/simulate-action?action=like&fromUser=Alice&toUser=Bob`

- **Friend Action**:
  `http://localhost:8080/simulate-action?action=friend&fromUser=Charlie&toUser=Alice`

- **Comment Action**:
  `http://localhost:8080/simulate-action?action=comment&fromUser=Dave&toUser=Bob`

### Monitoring Output

Watch your application's console logs for:
- Producer logs: `Producing event ->`
- Consumer notifications: `🔔 NOTIFICATION for Bob: Alice liked your post...`

### Alternative Testing Method

You can also create a test component that automatically sends sample events on application startup:

```java
@Component
public class TestDataRunner implements CommandLineRunner {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Override
    public void run(String... args) throws Exception {
        // Send test events on startup
        kafkaTemplate.send("social-activity", "like:Alice:Bob");
        kafkaTemplate.send("social-activity", "comment:Charlie:Alice");
        kafkaTemplate.send("social-activity", "friend:Dave:Bob");
    }
}
```

## Expected Behavior

1. The producer sends social activity events to the Kafka topic
2. The consumer listens to the topic, processes events, and generates formatted notifications
3. Notifications appear in the application logs with the bell icon (🔔) prefix

## Troubleshooting

- Ensure Kafka and Zookeeper are running before starting the application
- Check that the Kafka bootstrap servers configuration matches your Kafka instance
- Verify the topic name matches between producer and consumer