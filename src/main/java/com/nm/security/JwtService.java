package com.nm.security;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    private javax.crypto.SecretKey key() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String email, Map<String, Object> claims) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(email)
                .claims(claims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expirationMs)))
                .signWith(key())
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            var payload = Jwts.parser().verifyWith(key()).build()
                    .parseSignedClaims(token)
                    .getPayload();
            return payload.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}

