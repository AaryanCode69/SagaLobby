package com.example.sagalobby.domain.postgres.playerprofile.dto.request;

import com.example.sagalobby.domain.postgres.playerprofile.Region;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ProfileRequestDTO(@NotNull String username, @NotNull Region region) {
}
