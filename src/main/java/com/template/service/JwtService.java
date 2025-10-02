package com.template.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    /**
     * Generates a JWT token for the given user details.
     *
     * @param userId   the ID of the user
     * @param username the username of the user
     * @param role     the role of the user
     * @return a JWT token as a String
     */
    public String generateToken(Long userId,
                                String username,
                                String role) {

        String jwt = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role) // ROLE_USER, ROLE_MANAGER, ...
                .claim("username", username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();

        log.info("Generated JWT token: {}", jwt);
        return jwt;
    }

    /**
     * Validates the given JWT token and returns the claims if valid.
     *
     * @param token the JWT token to validate
     * @return the claims contained in the token
     * @throws JwtException if the token is invalid or expired
     */
    public Claims validateToken(String token) {

        try {

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            log.info("Token is valid. Claims: {}", claims);
            return claims;

        } catch (ExpiredJwtException e) {

            log.error("Token expired on: {}", e.getClaims().getExpiration());
            throw new JwtException("Token expired on" + e.getClaims().getExpiration() + ". Login again to get a new token");

        } catch (SignatureException e) {

            log.error("Token's signature is not valid");
            throw new JwtException("Token's signature is not valid. Login again to get a new token");

        } catch (Exception e) {

            log.error("Token is not valid");
            throw new JwtException("Token is not valid. Login again to get a new token");
        }
    }

}

