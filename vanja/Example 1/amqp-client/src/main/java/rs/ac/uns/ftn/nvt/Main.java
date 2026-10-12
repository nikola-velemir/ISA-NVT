package rs.ac.uns.ftn.nvt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final Random random = new Random();
    private static final List<String> measurements = Arrays.asList("Temperature", "Irradiance",
            "Humidity", "Precipitation");
    private static final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    public static void main(String[] args) throws Exception {
        if(args.length == 0) {
            throw new ExceptionInInitializerError("Topic is not defined");
        }
        String topic = args[0];
        if (topic.length() == 0) {
            throw new ExceptionInInitializerError("Invalid topic value");
        }
        Properties env = new Properties();
        env.load(Main.class.getClassLoader().getResourceAsStream("application.properties"));

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(env.getProperty("amqp.host"));
        factory.setPort(Integer.parseInt(env.getProperty("amqp.port")));
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
                channel.queueDeclare(topic, false, false, false, null);
                while(true) {
                    String message = generateRandomMessage();
                    LOGGER.info("Sending to topic \"{}\" message {}", topic, message);
                    channel.basicPublish("", topic, null, message.getBytes());
                    sleep();
                }
        }

    }

    /**
     * Pauses program for random number of seconds (1-10)
     */
    private static void sleep() throws InterruptedException {
        int sleepInterval = random.nextInt(10) + 1;
        LOGGER.info("Sleep for {} seconds", sleepInterval);
        Thread.sleep(sleepInterval * 1000L);
    }

    /**
     * Generates random message in JSON format that contains
     * random value and randomly picked measurement
     */
    private static String generateRandomMessage() throws JsonProcessingException {
        int randomIndex = random.nextInt(measurements.size());
        String randomElement = measurements.get(randomIndex);
        float randomValue = random.nextFloat() * (100);
        Measurement measurement = new Measurement(randomElement, randomValue);
        return objectWriter.writeValueAsString(measurement);
    }
}