package com.kajasa.auth.exception;

/**
 * Excepción utilizada para representar conflictos
 * en las operaciones de negocio.
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
