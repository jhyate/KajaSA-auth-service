package com.kajasa.auth.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Propiedades de configuración para las operaciones HMAC-SHA-256.
 * <p>
 * Proporciona acceso a la clave secreta utilizada para generar y
 * verificar códigos de autenticación mediante HMAC.
 */
@ConfigurationProperties(prefix = "kajasa.security.hmac")
@Validated
public record HmacProperties(

        @NotBlank
        @Pattern(
                regexp = "^[0-9a-fA-F]{64}$",
                message = "La clave HMAC debe contener exactamente 64 caracteres hexadecimales"
        )
        String secretKey
) {
}
