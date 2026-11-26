package com.kajasa.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad utilizada exclusivamente
 * durante el desarrollo local de KajaSA Auth Service.
 * <p>
 * Permite acceder a los endpoints sin autenticación
 * para facilitar las pruebas y el desarrollo.
 * <p>
 * Esta configuración no debe utilizarse en producción.
 */
@Configuration
@Profile("local")
public class LocalSecurityConfig {

    @Bean
    public SecurityFilterChain localSecurityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
