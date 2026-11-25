package com.kajasa.auth.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableConfigurationProperties(Argon2Properties.class)
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
