package ru.bank.cosmo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.bank.cosmo.dto.KafkaPostDto;

@Service
@RequiredArgsConstructor
public class PostKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "posts-created";

    @SneakyThrows
    public void sendPostCreatedEvent(KafkaPostDto postDto) {
        String json = objectMapper.writeValueAsString(postDto);
        kafkaTemplate.send(TOPIC, json);
    }
}
