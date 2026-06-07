package com.example.sagalobby.domain.postgres.matchrecord.repository;

import com.example.sagalobby.domain.postgres.matchrecord.MatchRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MatchRecordRepository extends JpaRepository<MatchRecord, UUID> {
}
