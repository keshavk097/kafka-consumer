package com.example.kafka_consumer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageDetails {

    private String topic;
    private Integer partition;
    private Long offset;
    private Long timestamp;
    private String key;
    private String value;

    @JsonProperty("topic")
    public String getTopic() {
        return topic;
    }

    @JsonProperty("partition")
    public Integer getPartition() {
        return partition;
    }

    @JsonProperty("offset")
    public Long getOffset() {
        return offset;
    }

    @JsonProperty("timestamp")
    public Long getTimestamp() {
        return timestamp;
    }

    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "MessageDetails{" +
                "topic='" + topic + '\'' +
                ", partition=" + partition +
                ", offset=" + offset +
                ", timestamp=" + timestamp +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
