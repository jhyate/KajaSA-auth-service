package com.kajasa.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

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
