package com.kajasa.auth.enums;

/**
 * Acciones de autenticación sujetas a restricciones de frecuencia.
 * <p>
 * Identifica las operaciones para las cuales se controlan los límites
 * de solicitudes realizadas por cada usuario.
 */
public enum RateLimitCounterAction {
    EMAIL_CODE_REQUEST,
    PASSWORD_RESET_REQUEST
}
