package com.example.sagalobby.domain.postgres.matchrecord.service;
import com.example.sagalobby.domain.postgres.matchrecord.MatchRecord;
import com.example.sagalobby.domain.postgres.matchrecord.Status;
import com.example.sagalobby.domain.postgres.matchrecord.dto.request.CreateMatchDTO;
import com.example.sagalobby.domain.postgres.matchrecord.dto.request.PlayerRecord;
import com.example.sagalobby.domain.postgres.matchrecord.dto.response.CreateMatchResponseDTO;
import com.example.sagalobby.domain.postgres.matchrecord.repository.MatchRecordRepository;
import com.example.sagalobby.domain.postgres.playerprofile.PlayerProfile;
import com.example.sagalobby.domain.postgres.playerprofile.repository.PlayerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class MatchRecordService {

    private final MatchRecordRepository matchRecordRepository;

    private final PlayerProfileRepository playerProfileRepository;

    @Transactional
    public CreateMatchResponseDTO joinMatch(CreateMatchDTO matchRecord) {
        List<PlayerRecord> players = matchRecord.players();

        List<UUID> playerIds = players.stream()
                .map(PlayerRecord::id)
                .toList();
        List<PlayerProfile> playerProfiles = playerProfileRepository.findAllById(playerIds);

        if (playerProfiles.size() != playerIds.size()) {
            throw new IllegalArgumentException("Cannot create match: One or more player profiles could not be found.");
        }
        MatchRecord matchRecordEntity = new MatchRecord();
        for(PlayerProfile playerProfile : playerProfiles){
            matchRecordEntity.addPlayerProfile(playerProfile);
        }
        matchRecordEntity.setStatus(Status.IN_PROGRESS);

        matchRecordEntity =  matchRecordRepository.save(matchRecordEntity);

        return CreateMatchResponseDTO.builder()
                .id(matchRecordEntity.getId())
                .status(matchRecordEntity.getStatus())
                .build();
    }
}
