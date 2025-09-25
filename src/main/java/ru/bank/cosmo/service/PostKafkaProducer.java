package ru.bank.cosmo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "posts-created";

    public void sendPostCreatedEvent(Long postId) {
        kafkaTemplate.send(TOPIC, postId.toString());
    }
}