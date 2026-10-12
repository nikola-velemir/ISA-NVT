package rs.ac.uns.ftn.nvt.backendexample.controller;

import com.rabbitmq.client.Channel;
import jakarta.validation.Valid;
import org.eclipse.paho.mqttv5.client.IMqttClient;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.nvt.backendexample.configuration.AmqpConfiguration;
import rs.ac.uns.ftn.nvt.backendexample.dto.TopicSubscription;
import rs.ac.uns.ftn.nvt.backendexample.model.Measurement;
import rs.ac.uns.ftn.nvt.backendexample.service.MeasurementService;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping(value = "topics")
public class TopicController {

    private final IMqttClient mqttClient;
    private final Channel amqpClient;
    private final AmqpConfiguration amqpConfiguration;
    private final MeasurementService measurementService;

    public TopicController(IMqttClient mqttClient, Channel amqpClient, AmqpConfiguration amqpConfiguration,
                           MeasurementService measurementService) {
        this.mqttClient = mqttClient;
        this.amqpClient = amqpClient;
        this.amqpConfiguration = amqpConfiguration;
        this.measurementService = measurementService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Measurement>> getAll(String topic) {
        return new ResponseEntity<>(this.measurementService.findByTopic(topic), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "mqtt")
    public ResponseEntity<Object> subscribeMqtt(@Valid @RequestBody TopicSubscription topic) throws MqttException {
        this.mqttClient.subscribe(topic.getTopic(), 2);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "amqp")
    public ResponseEntity<Object> subscribeAmqp(@Valid @RequestBody TopicSubscription topic) throws IOException {
        this.amqpClient.basicConsume(topic.getTopic(), true, this.amqpConfiguration, consumerTag -> { });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
