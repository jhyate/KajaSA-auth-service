package com.kajasa.auth.exception;

/**
 * Excepción utilizada cuando no se encuentra un recurso solicitado.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
