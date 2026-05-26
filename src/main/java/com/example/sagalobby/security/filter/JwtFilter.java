package com.example.sagalobby.security.filter;

import com.example.sagalobby.security.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        final String authHeader = request.getHeader("Authorization");

        if(!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")){
            chain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);

        if(SecurityContextHolder.getContext().getAuthentication() == null){
            try {
                Claims claims = jwtService.getValidatedClaims(token);

                String personId = claims.getSubject();
                String personName = jwtService.extractMetadataField(claims, "name");
                String personRole = jwtService.extractMetadataField(claims, "role");

                personName = personName!=null ?  personName : "Unknown";
                personRole = personRole!=null ? personRole : "USER";

                Map<String, String> principal = Map.of(
                        "personId", personId,
                        "personName", personName
                );

                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + personRole.toUpperCase());
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        Collections.singletonList(authority)
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }catch (Exception e){
                log.warn("JWT Authentication failed: {}",e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }
}
