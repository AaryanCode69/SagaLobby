package com.example.sagalobby.domain.postgres.matchrecord.service;
import com.example.sagalobby.domain.postgres.matchrecord.MatchRecord;
import com.example.sagalobby.domain.postgres.matchrecord.Status;
import com.example.sagalobby.domain.postgres.matchrecord.dto.request.CreateMatchDTO;
import com.example.sagalobby.domain.postgres.matchrecord.dto.request.PlayerRecord;
import com.example.sagalobby.domain.postgres.matchrecord.dto.response.CreateMatchResponseDTO;
import com.example.sagalobby.domain.postgres.matchrecord.repository.MatchRecordRepository;
import com.example.sagalobby.domain.postgres.playerprofile.PlayerProfile;
import com.example.sagalobby.domain.postgres.playerprofile.repository.PlayerProfileRepository;
import com.example.sagalobby.mapper.PlayerProfileMapper;
import com.example.sagalobby.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchRecordService {

    private final MatchRecordRepository matchRecordRepository;

    private final PlayerProfileRepository playerProfileRepository;

    private final PlayerProfileMapper playerProfileMapper;

    @Transactional
    public CreateMatchResponseDTO joinMatch(CreateMatchDTO matchRecord) {
        List<PlayerRecord> players = matchRecord.players();

        Set<PlayerProfile> playerProfiles = players.stream()
                .map(playerProfileMapper::mapPlayerRecordToPlayerProfile)
                .collect(Collectors.toSet());

        MatchRecord matchRecordEntity = new MatchRecord();
        matchRecordEntity.setPlayerProfiles(playerProfiles);
        matchRecordEntity.setStatus(Status.IN_PROGRESS);

        matchRecordEntity =  matchRecordRepository.save(matchRecordEntity);

        for (PlayerProfile playerProfile : playerProfiles) {
            playerProfile.addMatchRecord(matchRecordEntity);
            playerProfileRepository.save(playerProfile);
        }

        return CreateMatchResponseDTO.builder()
                .id(matchRecordEntity.getId())
                .status(matchRecordEntity.getStatus())
                .build();
    }
}
