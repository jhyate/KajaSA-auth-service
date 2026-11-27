package com.kajasa.auth.config;

import java.util.HexFormat;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración criptográfica de HMAC-SHA-256.
 * Convierte la clave secreta hexadecimal en una clave criptográfica
 * que podrá ser utilizada por los componentes de la aplicación.
 */
@Configuration
@EnableConfigurationProperties({
        Argon2Properties.class,
        EmailCodeProperties.class,
        HmacProperties.class
})
public class HmacConfig {

    @Bean
    public SecretKeySpec configureHmac(HmacProperties hmacProperties) {
        String secretKey = hmacProperties.secretKey();
        // Decodifica la representación hexadecimal de la clave secreta,
        // convirtiendo cada par de caracteres hexadecimales en un byte.
        // De esta manera recuperamos los 32 bytes originales (256 bits).
        byte[] keyBytes = HexFormat.of().parseHex(secretKey);
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

}
