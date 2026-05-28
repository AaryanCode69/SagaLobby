package com.example.sagalobby.security.controller;

import com.example.sagalobby.security.service.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtService jwtService;

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken (@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Claims claims = jwtService.getValidatedClaims(token);

        String personId = claims.getSubject();
        String personName = jwtService.extractMetadataField(claims, "name");
        String personRole = jwtService.extractMetadataField(claims, "role");

        personName = personName!=null ?  personName : "Unknown";
        personRole = personRole!=null ? personRole : "USER";

        Map<String, Object> response = new HashMap<>();
        response.put("valid", true);
        response.put("role", personRole);
        response.put("personId", personId);
        response.put("personName", personName);

        return ResponseEntity.ok(response);

    }

}
