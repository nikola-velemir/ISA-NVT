package rs.ac.uns.ftn.nvt.backendexample.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import rs.ac.uns.ftn.nvt.backendexample.model.Measurement;
import rs.ac.uns.ftn.nvt.backendexample.service.MeasurementService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Configuration
public class AmqpConfiguration implements DeliverCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(AmqpConfiguration.class);
    private final String host;
    private final Integer port;
    private final MeasurementService measurementService;

    public AmqpConfiguration(Environment env, MeasurementService measurementService) {
        this.host = env.getProperty("amqp.host");
        this.port = Integer.parseInt(Objects.requireNonNull(env.getProperty("amqp.port")));
        this.measurementService = measurementService;
    }

    @Bean
    public Channel amqpClient() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(this.host);
        factory.setPort(this.port);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        return channel;
    }

    @Override
    public void handle(String consumerTag, Delivery delivery) throws IOException {
        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        Measurement measurement = objectMapper.readValue(message, Measurement.class);
        measurement.setTopic(delivery.getEnvelope().getRoutingKey());
        this.measurementService.save(measurement);
        LOGGER.info(message);
    }
}
