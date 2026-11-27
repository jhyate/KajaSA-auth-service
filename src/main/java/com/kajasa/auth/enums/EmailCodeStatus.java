package com.kajasa.auth.enums;

/**
 * Estados posibles de un código de verificación de correo electrónico.
 * <p>
 * Permite controlar el ciclo de vida de los códigos desde su generación
 * hasta su consumo, expiración o invalidación.
 */
public enum EmailCodeStatus {
    ACTIVE,
    CONSUMED,
    EXPIRED,
    INVALIDATED
}
