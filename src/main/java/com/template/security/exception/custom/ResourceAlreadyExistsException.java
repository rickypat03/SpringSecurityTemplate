package com.template.security.exception.custom;


public class ResourceAlreadyExistsException extends RuntimeException {

    /**
     * Exception thrown when attempting to create a resource that already exists.
     * Mapped to HTTP 409 Conflict.
     * @param message the detail message.
     */
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
