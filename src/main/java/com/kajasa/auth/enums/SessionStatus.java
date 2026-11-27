package com.kajasa.auth.enums;

/**
 * Estados posibles de una sesión de usuario.
 * <p>
 * Permite identificar si una sesión se encuentra activa, expirada
 * o revocada.
 */
public enum SessionStatus {
    ACTIVE,
    EXPIRED,
    REVOKED
}
