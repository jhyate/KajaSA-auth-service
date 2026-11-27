package com.kajasa.auth.security.impl;

import com.kajasa.auth.exception.InternalServerException;
import com.kajasa.auth.security.HmacService;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio criptográfico HMAC-SHA-256.
 * <p>
 * Proporciona las operaciones necesarias para generar y verificar
 * códigos de autenticación mediante una clave secreta.
 */
@Service
@AllArgsConstructor
@Slf4j
public class HmacServiceImpl implements HmacService {

    private final SecretKeySpec secretKeySpec;

    /**
     * Genera un código de autenticación HMAC-SHA-256 a partir del contenido recibido.
     * <p>
     * Utiliza una clave secreta para producir una representación hexadecimal
     * del HMAC, permitiendo verificar posteriormente la integridad y autenticidad
     * del contenido.
     *
     * @param code contenido original sobre el que se calculará el HMAC.
     * @return HMAC-SHA-256 generado en formato hexadecimal.
     */
    @Override
    public String generateHmac(String code) {
        try {
            Mac instance = Mac.getInstance("HmacSHA256");
            instance.init(secretKeySpec);
            byte[] codeBytes = code.getBytes(StandardCharsets.UTF_8);
            return HexFormat.of().formatHex(instance.doFinal(codeBytes));
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            log.error("Error al obtener el algoritmo HmacSHA256 durante la generación del HMAC.");
            throw new InternalServerException("No fue posible obtener el algoritmo HMAC-SHA-256.", noSuchAlgorithmException);
        } catch (InvalidKeyException invalidKeyException) {
            log.error("Error al inicializar HmacSHA256 con la clave configurada durante la generación del HMAC.");
            throw new InternalServerException("No fue posible inicializar HMAC con la clave configurada.", invalidKeyException);
        }
    }

    /**
     * Verifica la autenticidad de un código comparando su HMAC calculado
     * con el HMAC proporcionado.
     * <p>
     * La comparación se realiza de forma segura para evitar filtraciones
     * de información relacionadas con el tiempo de ejecución.
     *
     * @param code código original que se desea verificar.
     * @param hmac HMAC esperado con el que se realizará la comparación.
     * @return true si los HMAC coinciden; false en caso contrario.
     */
    @Override
    public boolean verifyHmac(String code, String hmac) {

        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("El código no puede ser nulo ni estar vacío.");
        }

        if (hmac == null || hmac.isBlank()) {
            throw new IllegalArgumentException("El HMAC no puede ser nulo ni estar vacío.");
        } else if (hmac.length() != 64) {
            throw new IllegalArgumentException("El HMAC debe contener exactamente 64 caracteres hexadecimales.");
        } else if (!hmac.matches("^[0-9a-fA-F]{64}$")) {
            throw new IllegalArgumentException("El HMAC contiene caracteres que no pertenecen al formato hexadecimal.");
        }

        byte[] generatedHmac = HexFormat.of().parseHex(generateHmac(code));
        byte[] expectedHmac = HexFormat.of().parseHex(hmac);
        return MessageDigest.isEqual(generatedHmac, expectedHmac);
    }
}
