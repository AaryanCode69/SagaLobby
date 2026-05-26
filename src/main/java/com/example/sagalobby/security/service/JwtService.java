package com.example.sagalobby.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {

    private static final String CLAIM_USER_METADATA = "user_metadata";
    private static final String META_NAME = "name";
    private static final String META_ROLE = "role";
    private static final String SUPABASE_AUDIENCE = "authenticated";

    @Value("${supabase.jwt.secret}")
    private String secret;

    @Value("${supabase.url}")
    private String supabaseUrl;

    private Key signingKey;

    @PostConstruct
    public void init() {
        // Cache the signing key exactly once at startup to save CPU cycles
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    public Claims getValidatedClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.signingKey)
                .requireAudience("authenticated")
                .requireIssuer(supabaseUrl)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.signingKey)
                .requireAudience(SUPABASE_AUDIENCE)
                .requireIssuer(supabaseUrl)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractMetadataField(Claims claims, String fieldName) {
        return Optional.ofNullable(claims.get(CLAIM_USER_METADATA, Map.class))
                .map(meta -> meta.get(fieldName))
                .map(Object::toString)
                .orElse(null);
    }
}