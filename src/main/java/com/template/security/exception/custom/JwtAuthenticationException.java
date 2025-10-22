package com.template.security.exception.custom;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {

    /**
     * Exception thrown when there is an authentication error related to JWT.
     * Mapped to HTTP 401 Unauthorized.
     * @param message the detail message.
     */
    public JwtAuthenticationException(String message) {
        super(message);
    }
}
