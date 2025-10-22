package com.template.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    //TODO: When you'll call this function remember to create a Map<String, Object> variable where you'll insert your claims
    //      ex: Map<String, Object> claims = new HashMap();
    //          claims.put("username", "bobby");
    /**
     * Generates a JWT token for the given user details.
     *
     * @param userId   the ID of the user
     * @param claims   the claims of the token
     * @return a JWT token as a String
     */
    public String generateToken(Long userId,
                                Map<String, Object> claims) {

        String jwt = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .addClaims(claims)
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

