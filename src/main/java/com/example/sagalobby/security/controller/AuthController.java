package com.example.sagalobby.security.controller;

import com.auth0.jwk.JwkException;
import com.auth0.jwt.interfaces.Claim;
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
    public ResponseEntity<?> validateToken (@RequestHeader("Authorization") String authHeader) throws JwkException {
        String token = authHeader.substring(7);
        Map<String, Claim> claims = jwtService.verifySupabaseToken(token);
        Claim subClaim = claims.get("sub");
        String personId = subClaim.asString();
        Claim user_metadataClaim = claims.get("user_metadata");
        Map<String, Object> user_metadata = user_metadataClaim.asMap();
        String personName = (String) user_metadata.get("name");
        String personRole = (String ) user_metadata.get("role");

        Map<String, Object> response = new HashMap<>();
        response.put("valid", true);
        response.put("role", personRole);
        response.put("personId", personId);
        response.put("personName", personName);

        return ResponseEntity.ok(response);

    }

}
