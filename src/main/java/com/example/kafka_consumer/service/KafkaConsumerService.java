package com.example.kafka_consumer.service;


import com.example.kafka_consumer.model.MessageDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final WebClient.Builder webClientBuilder;

    @Value("${kafka.bridge.url}")
    private String kafkaBridgeUrl;

    @Value("${kafka.consumer.group}")
    private String consumerGroup;

    @Value("${kafka.consumer.instance}")
    private String consumerInstance;

    @Value("${kafka.consumer.topic}")
    private String topic;

    public KafkaConsumerService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }


    private WebClient getWebClient() {
        return webClientBuilder.baseUrl(kafkaBridgeUrl).build();
    }

    // Step 1: Create Consumer Instance
    public Mono<Void> createConsumer() {
        Map<String, Object> consumerConfig = Map.of(
                "name", consumerInstance,
                "format", "json",
                "auto.offset.reset", "earliest"
        );

        return getWebClient().post()
                .uri("/consumers/{consumerGroup}", consumerGroup)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(consumerConfig)
                .retrieve()
                .bodyToMono(Void.class);
    }

    // Step 2: Subscribe Consumer to a Topic
    public Mono<Void> subscribeConsumer() {
        Map<String, Object> subscriptionConfig = Map.of("topics", new String[]{topic});

        return getWebClient().post()
                .uri("/consumers/{consumerGroup}/instances/{consumerInstance}/subscription", consumerGroup, consumerInstance)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(subscriptionConfig)
                .retrieve()
                .bodyToMono(Void.class);
    }

    // Step 3: Poll Messages from Kafka Topic
    public Flux<MessageDetails> consumeMessages() {
        return getWebClient().get()
                .uri("/consumers/{consumerGroup}/instances/{consumerInstance}/records", consumerGroup, consumerInstance)
                .header(HttpHeaders.ACCEPT, "application/vnd.kafka.json.v2+json")
                .retrieve()
                .bodyToFlux(MessageDetails.class); // Extract only the message value
    }
}
