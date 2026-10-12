package rs.ac.uns.ftn.nvt.backendexample.dto;

import jakarta.validation.constraints.NotBlank;

public class TopicSubscription {
    @NotBlank(message = "Topic is mandatory")
    private String topic;

    public TopicSubscription() {
    }

    public TopicSubscription(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "TopicSubscription{" +
                "topic='" + topic + '\'' +
                '}';
    }
}
