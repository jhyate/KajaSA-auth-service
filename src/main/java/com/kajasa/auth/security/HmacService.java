package com.kajasa.auth.security;

/**
 * Define las operaciones criptográficas para generar y verificar
 * códigos de autenticación basados en HMAC-SHA-256.
 * <p>
 * Esta interfaz permite desacoplar la lógica criptográfica de los
 * servicios que la utilizan, facilitando su mantenimiento y pruebas.
 */
public interface HmacService {

    /**
     * Genera la representación hexadecimal del HMAC de un código.
     *
     * @param code código que se desea proteger
     * @return HMAC-SHA-256 en formato hexadecimal
     */
    String generateHmac(String code);

    /**
     * Verifica si un código corresponde al HMAC proporcionado.
     *
     * @param code código que se desea verificar
     * @param hmac HMAC almacenado para realizar la comparación
     * @return true si coinciden, false en caso contrario
     */
    boolean verifyHmac(String code, String hmac);
}
