package com.example.sagalobby.common;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> ExceptionHandler(JwtException exception, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value())
                .body(ErrorResponse.builder()
                        .timestamp(Instant.now())
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .message(exception.getMessage())
                        .path(request.getRequestURI())
                        .build());

    }
}
