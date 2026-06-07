package com.example.sagalobby.domain.postgres.playerprofile.service;

import com.example.sagalobby.domain.postgres.playerprofile.PlayerProfile;
import com.example.sagalobby.domain.postgres.playerprofile.dto.request.ProfileRequestDTO;
import com.example.sagalobby.domain.postgres.playerprofile.dto.response.ProfileResponseDTO;
import com.example.sagalobby.domain.postgres.playerprofile.repository.PlayerProfileRepository;
import com.example.sagalobby.mapper.PlayerProfileMapper;
import com.example.sagalobby.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerProfileService {

    private final SecurityUtils securityUtils;

    private final PlayerProfileRepository playerProfileRepository;

    private final PlayerProfileMapper playerProfileMapper;

    @Transactional
    public ProfileResponseDTO registerUser(ProfileRequestDTO profileRequestDTO) {
        UUID id = securityUtils.getCurrentUser();

        PlayerProfile playerProfile = playerProfileRepository.findById(id).orElse(null);

        if(playerProfile != null){
            throw new IllegalStateException("Player Profile already exists");
        }

        playerProfile = PlayerProfile.builder()
                .id(id)
                .username(profileRequestDTO.username())
                .region(profileRequestDTO.region())
                .build();

        playerProfile =  playerProfileRepository.save(playerProfile);

        return playerProfileMapper.mapPlayerProfileToProfileResponse(playerProfile);
    }
}
