package ru.bank.cosmo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.bank.cosmo.dto.KafkaPostDto;

@Service
@RequiredArgsConstructor
public class PostKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "posts-created";

    public void sendPostCreatedEvent(KafkaPostDto postDto) {
        kafkaTemplate.send(TOPIC, postDto.toString());
    }
}