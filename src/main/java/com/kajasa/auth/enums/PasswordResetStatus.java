package com.kajasa.auth.enums;

/**
 * Estados posibles de una solicitud de recuperación de contraseña.
 * <p>
 * Permite controlar el ciclo de vida del proceso de recuperación,
 * incluyendo su utilización, expiración e invalidación.
 */
public enum PasswordResetStatus {
    ACTIVE,
    USED,
    EXPIRED,
    INVALIDATED
}
