package com.example.sagalobby.domain.postgres.matchrecord.controller;

import com.example.sagalobby.domain.postgres.matchrecord.dto.request.CreateMatchDTO;
import com.example.sagalobby.domain.postgres.matchrecord.dto.response.CreateMatchResponseDTO;
import com.example.sagalobby.domain.postgres.matchrecord.service.MatchRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/match")
@RequiredArgsConstructor
public class MatchRecordController {

    private final MatchRecordService matchRecordService;

    @PostMapping("/join")
    public ResponseEntity<CreateMatchResponseDTO> createMatch(@RequestBody()CreateMatchDTO request){
        CreateMatchResponseDTO response = matchRecordService.joinMatch(request);
        return ResponseEntity.ok(response);
    }
}
