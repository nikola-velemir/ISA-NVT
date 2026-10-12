package rs.ac.uns.ftn.nvt.backendexample.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.mqttv5.client.*;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import rs.ac.uns.ftn.nvt.backendexample.model.Measurement;
import rs.ac.uns.ftn.nvt.backendexample.service.MeasurementService;

import java.util.Objects;
import java.util.UUID;

@Configuration
public class MqttConfiguration {
    private final String broker;
    private final String username;
    private final String password;
    private final String uniqueClientIdentifier;
    private final MeasurementService measurementService;

    public MqttConfiguration(Environment env, MeasurementService measurementService) {
        this.broker = String.format("tcp://%s:%s", env.getProperty("mqtt.host"),
                env.getProperty("mqtt.port"));
        this.username = env.getProperty("mqtt.username");
        this.password = env.getProperty("mqtt.password");
        this.uniqueClientIdentifier = UUID.randomUUID().toString();
        this.measurementService = measurementService;
    }

    @Bean
    public IMqttClient mqttClient() throws Exception {
        MqttClient client = new MqttClient(this.broker, this.uniqueClientIdentifier, new MemoryPersistence());
        client.setCallback(new MessageCallback(this.measurementService));
        MqttConnectionOptions options = new MqttConnectionOptions();
        options.setCleanStart(false);
        options.setAutomaticReconnect(true);
        options.setUserName(this.username);
        options.setPassword(Objects.requireNonNull(this.password).getBytes());
        client.connect(options);
        return client;
    }

    public static class MessageCallback implements MqttCallback {
        private static final Logger LOGGER = LoggerFactory.getLogger(MessageCallback.class);
        private final MeasurementService measurementService;
        public MessageCallback(MeasurementService measurementService) {
            super();
            this.measurementService = measurementService;
        }

        @Override
        public void disconnected(MqttDisconnectResponse mqttDisconnectResponse) {
            LOGGER.warn("disconnected: {}", mqttDisconnectResponse.getReasonString());
        }

        @Override
        public void mqttErrorOccurred(MqttException e) {
            LOGGER.error("error: {}", e.getMessage());
        }

        @Override public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
            LOGGER.debug("topic: {}, message: {}", topic, mqttMessage);
            String message = new String(mqttMessage.getPayload());
            ObjectMapper objectMapper = new ObjectMapper();
            Measurement measurement = objectMapper.readValue(message, Measurement.class);
            measurement.setTopic(topic);
            this.measurementService.save(measurement);
            LOGGER.info(message);
        }

        @Override
        public void deliveryComplete(IMqttToken iMqttToken) {
            LOGGER.debug("delivery complete, message id: {}", iMqttToken.getMessageId());
        }

        @Override
        public void connectComplete(boolean b, String s) {
            LOGGER.debug("connect complete, status: {} {}", b, s);
        }

        @Override
        public void authPacketArrived(int i, MqttProperties mqttProperties) {
            LOGGER.debug("auth packet arrived , status: {} {}", i, mqttProperties.getAuthenticationMethod());
        }
    }
}
