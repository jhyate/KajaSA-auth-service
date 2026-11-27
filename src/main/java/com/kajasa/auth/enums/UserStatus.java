package com.kajasa.auth.enums;

/**
 * Estados posibles de una cuenta de usuario.
 * <p>
 * Permite controlar la disponibilidad de la cuenta durante su ciclo
 * de vida, desde el registro hasta su activación, bloqueo o deshabilitación.
 */
public enum UserStatus {
    PENDING_VERIFICATION,
    ACTIVE,
    BLOCKED,
    DISABLED
}
