package com.example.sagalobby.domain.postgres.matchrecord.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateMatchDTO(@NotEmpty @Size(min = 2,max = 10) @Valid List<PlayerRecord> players) {
}
