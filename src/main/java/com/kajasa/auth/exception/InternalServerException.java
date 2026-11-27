package com.kajasa.auth.exception;

/**
 * Excepción personalizada para representar errores internos
 * durante la ejecución de la aplicación.
 * <p>
 * Permite encapsular errores técnicos y conservar la excepción
 * original como causa cuando sea necesario.
 */
public class InternalServerException extends RuntimeException {
    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
