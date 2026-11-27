package com.kajasa.auth.enums;

/**
 * Estados posibles de un refresh token.
 * <p>
 * Permite controlar el ciclo de vida de los tokens de renovación,
 * incluyendo su utilización, revocación y expiración.
 */
public enum RefreshTokenStatus {
    ACTIVE,
    USED,
    REVOKED,
    EXPIRED
}
