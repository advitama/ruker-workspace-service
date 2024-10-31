package com.ruker.workspace.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    // Constants for environment variable keys
    private static final String JWT_SECRET_KEY = "JWT_SECRET";
    private static final String JWT_ISSUER_KEY = "JWT_ISSUER";

    private final Algorithm algorithm = createAlgorithm();
    private final JWTVerifier verifier = createVerifier();

    /**
     * Decodes and verifies a JWT token.
     * 
     * @param token the JWT token to decode
     * @return DecodedJWT object if the token is valid
     * @throws IllegalArgumentException if the token is invalid
     */
    public DecodedJWT decodeToken(String token) {
        try {
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    /**
     * Validates if the JWT token is not expired and correctly signed.
     * 
     * @param token the JWT token to validate
     * @return true if the token is valid and not expired, false otherwise
     */
    public boolean isTokenValid(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        return decodedJWT.getExpiresAt().after(new Date());
    }

    /**
     * Retrieves the subject (user identifier) from the JWT token.
     * 
     * @param token the JWT token from which to retrieve the subject
     * @return subject contained in the token
     */
    public String getSubjectFromToken(String token) {
        return decodeToken(token).getSubject();
    }

    /**
     * Generates a JWT token with the given subject and expiration time.
     * 
     * @param subject    the subject (user identifier) to include in the token
     * @param expiration the expiration date for the token
     * @return the generated JWT token
     */
    public String generateToken(String subject, Date expiration) {
        return JWT.create()
                .withIssuer(System.getenv(JWT_ISSUER_KEY))
                .withSubject(subject)
                .withExpiresAt(expiration)
                .sign(algorithm);
    }

    /**
     * Creates and configures the JWT Algorithm instance using environment
     * variables.
     * 
     * @return Algorithm instance for HMAC256 signing
     */
    private Algorithm createAlgorithm() {
        String secret = System.getenv(JWT_SECRET_KEY);
        if (secret == null) {
            throw new IllegalStateException("JWT_SECRET environment variable is not set");
        }
        return Algorithm.HMAC256(secret);
    }

    /**
     * Creates a JWTVerifier instance for verifying tokens.
     * 
     * @return a configured JWTVerifier instance
     */
    private JWTVerifier createVerifier() {
        String issuer = System.getenv(JWT_ISSUER_KEY);
        if (issuer == null) {
            throw new IllegalStateException("JWT_ISSUER environment variable is not set");
        }
        return JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
    }
}