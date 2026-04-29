package com.chatop.back.auth.service;

import com.chatop.back.user.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtTokenService {

    private static final JwsHeader HS256_HEADER = JwsHeader.with(MacAlgorithm.HS256).build();

    private final JwtEncoder jwtEncoder;
    private final long expirationMinutes;

    public JwtTokenService(JwtEncoder jwtEncoder,
                           @Value("${jwt.expiration-minutes}") long expirationMinutes) {
        this.jwtEncoder = jwtEncoder;
        this.expirationMinutes = expirationMinutes;
    }

    public String issue(User user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getEmail())
                .claim("user_id", user.getId())
                .issuedAt(now)
                .expiresAt(now.plus(expirationMinutes, ChronoUnit.MINUTES))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(HS256_HEADER, claims)).getTokenValue();
    }
}
