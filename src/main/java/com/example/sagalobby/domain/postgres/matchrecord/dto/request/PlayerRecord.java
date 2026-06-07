package com.example.sagalobby.domain.postgres.matchrecord.dto.request;

import com.example.sagalobby.domain.postgres.playerprofile.Region;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record PlayerRecord(@NotNull UUID id,
                           @NotNull Region region,
                           @PositiveOrZero
                           @Max(5000)
                           @NotNull Integer mmrRating) {
}
