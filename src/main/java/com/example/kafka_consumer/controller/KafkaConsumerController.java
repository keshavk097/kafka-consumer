package com.example.kafka_consumer.controller;

import com.example.kafka_consumer.model.MessageDetails;
import com.example.kafka_consumer.service.KafkaConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/consume")
@RequiredArgsConstructor
public class KafkaConsumerController {

    private final KafkaConsumerService kafkaConsumerService;

    public KafkaConsumerController(KafkaConsumerService kafkaConsumerService) {
        this.kafkaConsumerService = kafkaConsumerService;
    }

    @PostMapping("/init")
    public Mono<String> initializeConsumer() {
        return kafkaConsumerService.createConsumer()
                .then(kafkaConsumerService.subscribeConsumer())
                .thenReturn("Consumer initialized and subscribed!");
    }

    @GetMapping("/messages")
    public Flux<MessageDetails> getMessages() {
        return kafkaConsumerService.consumeMessages();
    }
}
