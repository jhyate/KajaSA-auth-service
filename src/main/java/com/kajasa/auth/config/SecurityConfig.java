package com.kajasa.auth.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuración de los componentes de seguridad de la aplicación.
 * <p>
 * Define los beans y componentes necesarios para implementar
 * los mecanismos de seguridad utilizados por KajaSA Auth Service.
 */
@Configuration
@EnableConfigurationProperties({
        Argon2Properties.class,
        EmailCodeProperties.class,
        HmacProperties.class
})
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(Argon2Properties properties) {
        return new Argon2PasswordEncoder(
                properties.saltLength(),
                properties.hashLength(),
                properties.parallelism(),
                properties.memory(),
                properties.iterations()
        );
    }
}
