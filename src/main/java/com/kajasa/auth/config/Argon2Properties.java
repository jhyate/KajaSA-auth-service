package com.kajasa.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propiedades de configuración del algoritmo Argon2id.
 * <p>
 * Permite definir los parámetros criptográficos utilizados para
 * generar hashes seguros de las contraseñas de los usuarios.
 */
@ConfigurationProperties(prefix = "kajasa.security.password.argon2")
public record Argon2Properties(
        String algorithm,
        int memory,
        int iterations,
        int parallelism,
        int saltLength,
        int hashLength
) {
}
