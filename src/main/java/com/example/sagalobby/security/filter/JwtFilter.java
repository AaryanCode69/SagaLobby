package com.example.sagalobby.security.filter;

import com.auth0.jwt.interfaces.Claim;
import com.example.sagalobby.security.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

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
                Map<String, Claim> claims = jwtService.verifySupabaseToken(token);
                Claim subClaim = claims.get("sub");
                String personId = subClaim.asString();
                Claim user_metadataClaim = claims.get("user_metadata");
                Map<String, Object> user_metadata = user_metadataClaim.asMap();
                String personName = (String) user_metadata.get("name");
                String personRole = (String ) user_metadata.get("role");

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
                chain.doFilter(request, response);
            }catch (Exception e){
                exceptionResolver.resolveException(request, response,null, e);
            }
        }


    }
}
