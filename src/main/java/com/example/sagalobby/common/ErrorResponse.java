package com.example.sagalobby.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@Builder
public class ErrorResponse {
    private String message;
    private int status;
    private Instant timestamp;
    private String path;
    private Map<String,String> validationErrors;
}
