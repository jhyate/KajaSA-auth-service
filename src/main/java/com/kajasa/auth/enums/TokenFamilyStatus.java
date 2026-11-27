package com.kajasa.auth.enums;

/**
 * Estados posibles de una familia de refresh tokens.
 * <p>
 * Permite controlar la validez de una familia de tokens asociados
 * a una misma sesión y gestionar situaciones de revocación o compromiso.
 */
public enum TokenFamilyStatus {
    ACTIVE,
    REVOKED,
    COMPROMISED,
    EXPIRED
}
