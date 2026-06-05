package com.example.sagalobby.domain.postgres.playerprofile.repository;

import com.example.sagalobby.domain.postgres.playerprofile.PlayerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlayerProfileRepository extends JpaRepository<PlayerProfile, UUID> {
}
