package com.template.security.exception.custom;

/**
 * Status: 409 Conflict
 */
public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
