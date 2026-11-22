package rabbitmq.producer.example.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rabbitmq.producer.example.messaging.Producer;

import java.util.UUID;

@Component
public class ScheduledProducer {
    private static final Logger log = LoggerFactory.getLogger(ScheduledProducer.class);
    @Autowired
    private Producer producer;

    @Value("${myexchange}")
    private String exchange;

    @Value("${routingkey}")
    private String routingKey;

    @Scheduled(fixedDelay = 5000)
    public void sendToMsgExchange(){
        log.info("Sending message!");
        String message = "Hello " + UUID.randomUUID().toString();
        producer.sendToExchange(exchange, routingKey, message);
    }
    @Scheduled(fixedDelay = 10000)
    public void sendToDefaultExchange(){
        log.info("Sending message to default!");
        String message = "Hello " + UUID.randomUUID().toString() + " to default";
        producer.sendToExchange("", "default-queue", message);
    }
}
