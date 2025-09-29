package com.worksta.backend.session;

import com.worksta.backend.secrets.SecretService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private static final long TTL_SECONDS = 86_400; // 24 h

    private final Key key = Keys.hmacShaKeyFor(SecretService.JWT_KEY_SECRET);

    public String generateToken(String username, String role) {
        Instant now = Instant.now();
        return Jwts.builder().subject(username)
                .claims(Map.of("role", role))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(TTL_SECONDS)))
                .signWith(key)
                .compact();
    }

    /**
     * Allow Spring-Security to decode / validate the HS-256 tokens we issue.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder
                .withSecretKey(Keys.hmacShaKeyFor(SecretService.JWT_KEY_SECRET))
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

}
