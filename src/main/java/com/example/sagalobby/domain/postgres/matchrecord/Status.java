package com.example.sagalobby.domain.postgres.matchrecord;

public enum Status{
    PENDING_ALLOCATION,
    IN_PROGRESS,
    COMPLETED,
    FAILED_ROLLBACK
}
