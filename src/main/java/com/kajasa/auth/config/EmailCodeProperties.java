package com.kajasa.auth.config;

import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Propiedades de configuración para los códigos de verificación de correo.
 * <p>
 * Define parámetros como la longitud del código, su tiempo de expiración
 * y el número máximo de intentos permitidos.
 */
@ConfigurationProperties(prefix = "kajasa.security.email-code")
@Validated
public record EmailCodeProperties(
        @Positive
        int length,

        @Positive
        int expirationMinutes,

        @Positive
        int maxAttempts
) {
}
