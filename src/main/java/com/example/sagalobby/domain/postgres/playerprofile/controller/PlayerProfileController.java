package com.example.sagalobby.domain.postgres.playerprofile.controller;

import com.example.sagalobby.domain.postgres.playerprofile.dto.request.ProfileRequestDTO;
import com.example.sagalobby.domain.postgres.playerprofile.dto.response.ProfileResponseDTO;
import com.example.sagalobby.domain.postgres.playerprofile.service.PlayerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/player-profile")
public class PlayerProfileController {

    private final PlayerProfileService playerProfileService;

    @PostMapping("/")
    public ResponseEntity<ProfileResponseDTO> validateToken (@RequestBody ProfileRequestDTO profileRequestDTO) {
        ProfileResponseDTO response = playerProfileService.registerUser(profileRequestDTO);
        return ResponseEntity.ok(response);

    }

}
