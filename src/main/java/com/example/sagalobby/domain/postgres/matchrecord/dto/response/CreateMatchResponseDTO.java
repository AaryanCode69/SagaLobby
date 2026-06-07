package com.example.sagalobby.domain.postgres.matchrecord.dto.response;

import com.example.sagalobby.domain.postgres.matchrecord.Status;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateMatchResponseDTO(UUID id, Status status) {
}
