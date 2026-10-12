package rs.ac.uns.ftn.nvt;

import org.eclipse.paho.mqttv5.client.*;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Properties;
import java.util.UUID;

public class MqttConfiguration {
    private final Properties env;
    private final String broker;
    private final String uniqueClientIdentifier;
    private final MqttClient client;

    public MqttConfiguration() throws Exception {
        this.env = new Properties();
        env.load(MqttConfiguration.class.getClassLoader().getResourceAsStream("application.properties"));
        this.broker = String.format("tcp://%s:%s", env.getProperty("mqtt.host"), env.getProperty("mqtt.port"));
        this.uniqueClientIdentifier = UUID.randomUUID().toString();
        this.client = this.mqttClient();
    }

    public MqttClient getClient() {
        return client;
    }

    private MqttClient mqttClient() throws MqttException {
        MqttClient client = new MqttClient(this.broker, this.uniqueClientIdentifier, new MemoryPersistence());
        client.setCallback(new MessageCallback());
        MqttConnectionOptions options = new MqttConnectionOptions();
        options.setCleanStart(false);
        options.setAutomaticReconnect(true);
        options.setUserName(this.env.getProperty("mqtt.username"));
        options.setPassword(Objects.requireNonNull(this.env.getProperty("mqtt.password")).getBytes());
        client.connect(options);
        return client;
    }

    public static class MessageCallback implements MqttCallback {
        private static final Logger LOGGER = LoggerFactory.getLogger(MessageCallback.class);

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
