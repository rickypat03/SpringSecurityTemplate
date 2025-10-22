package com.template.security.exception.custom;

public class ResourceNotFoundException extends RuntimeException {

    /**
     * Exception thrown when a requested resource is not found.
     * Mapped to HTTP 404 Not Found.
     * @param message the detail message.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
