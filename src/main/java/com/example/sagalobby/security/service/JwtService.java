package com.example.sagalobby.security.service;

import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.ECPublicKey;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class JwtService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    JwkProvider provider;

    public JwtService(@Value("${supabase.jwt.url}") String jwksProviderUrl) throws MalformedURLException {
        // I highly recommend using the builder here to enable caching!
        this.provider = new JwkProviderBuilder(new URL(jwksProviderUrl))
                .cached(10, 24, TimeUnit.HOURS)
                .rateLimited(10, 1, TimeUnit.MINUTES)
                .build();
    }


    public Map<String, Claim> verifySupabaseToken(String token) throws JwkException {
        DecodedJWT unverifedjwt = JWT.decode(token);
        String kid = unverifedjwt.getKeyId();

        ECPublicKey publicKey = (ECPublicKey) provider.get(kid).getPublicKey();

        Algorithm algorithm = Algorithm.ECDSA256(publicKey,null);

        DecodedJWT verifiedjwt = JWT.require(algorithm)
                .withIssuer(supabaseUrl + "/auth/v1")
                .build()
                .verify(unverifedjwt);

        return verifiedjwt.getClaims();
    }
}