package ru.bank.cosmo.dto;

import java.time.LocalDateTime;

public record KafkaPostDto(Long postId, Long companyId, LocalDateTime postCreateAt) {
}
