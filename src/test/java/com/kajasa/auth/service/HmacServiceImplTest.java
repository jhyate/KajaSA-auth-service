package com.kajasa.auth.service;

import com.kajasa.auth.security.impl.HmacServiceImpl;
import java.util.Arrays;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class HmacServiceImplTest {

    private SecretKeySpec secretKeySpec;

    private HmacServiceImpl hmacService;

    @BeforeEach
    void setUp() {
        byte[] keyBytes = new byte[32];
        Arrays.fill(keyBytes, (byte) 1);

        secretKeySpec = new SecretKeySpec(keyBytes, "HmacSHA256");

        hmacService = new HmacServiceImpl(secretKeySpec);
    }

    @Test
    void generateHmac_ok() {

        String code = "123456";
        String hmac = hmacService.generateHmac(code);

        assertNotNull(hmac, "El HMAC generado no debería ser nulo.");
        assertEquals(64, hmac.length(), "El HMAC generado debe contener exactamente 64 caracteres.");
        assertTrue(hmac.matches("^[0-9a-fA-F]{64}$"),
                "El HMAC generado debe contener únicamente caracteres hexadecimales.");
    }

    @Test
    void generateHmac_shouldGenerateDifferentHmacForDifferentCodes() {
        String codeOne = "123456";
        String codeTwo = "789107";

        String hmacOne = hmacService.generateHmac(codeOne);
        String hmacTwo = hmacService.generateHmac(codeTwo);

        assertNotEquals(hmacOne, hmacTwo, "Los HMAC generados para códigos diferentes no deberían coincidir.");
        assertNotNull(hmacOne, "El HMAC #1 generado no debería ser nulo.");
        assertNotNull(hmacTwo, "El HMAC #2 generado no debería ser nulo.");
    }

    @Test
    void verifyHmac_shouldReturnTrueWhenHmacIsValid() {
        String code = "123456";
        String hmac = hmacService.generateHmac(code);

        boolean verify = hmacService.verifyHmac(code, hmac);

        assertTrue(verify, "El codigo corresponde al HMAC.");
    }

    @Test
    void verifyHmac_shouldReturnFalseWhenHmacIsInvalid() {
        String codeOne = "123456";
        String hmac = hmacService.generateHmac(codeOne);
        String codeTwo = "789107";

        boolean verify = hmacService.verifyHmac(codeTwo, hmac);

        assertFalse(verify, "El codigo NO corresponde al HMAC.");
    }

    @Test
    void verifyHmac_shouldThrowExceptionWhenCodeIsNull() {
        String codeOne = "123456";
        String hmac = hmacService.generateHmac(codeOne);
        String codeTwo = null;

        assertThrows(
                IllegalArgumentException.class,
                () -> hmacService.verifyHmac(codeTwo, hmac),
                "El código es nulo."
        );
    }

    @Test
    void verifyHmac_shouldThrowExceptionWhenCodeIsBlank() {

        String codeOne = "123456";
        String hmac = hmacService.generateHmac(codeOne);
        String codeTwo = "";

        assertThrows(
                IllegalArgumentException.class,
                () -> hmacService.verifyHmac(codeTwo, hmac),
                "El código es vacio."
        );
    }

    @Test
    void verifyHmac_shouldThrowExceptionWhenHmacIsNull() {
        String code = "123456";
        String hmac = null;

        assertThrows(
                IllegalArgumentException.class,
                () -> hmacService.verifyHmac(code, hmac),
                "El HMAC es nulo."
        );
    }

    @Test
    void verifyHmac_shouldThrowExceptionWhenHmacIsBlank() {
        String code = "123456";
        String hmac = "";

        assertThrows(
                IllegalArgumentException.class,
                () -> hmacService.verifyHmac(code, hmac),
                "El HMAC es vacio."
        );
    }

    @Test
    void verifyHmac_shouldThrowExceptionWhenHmacHasInvalidLength() {
        String code = "123456";
        String hmac = "a".repeat(63);

        assertThrows(
                IllegalArgumentException.class,
                () -> hmacService.verifyHmac(code, hmac),
                "El HMAC no cumple con la cantidad de caracteres."
        );
    }

    @Test
    void verifyHmac_shouldThrowExceptionWhenHmacContainsInvalidCharacters() {
        String code = "123456";
        String hmac = "z".repeat(64);

        assertThrows(
                IllegalArgumentException.class,
                () -> hmacService.verifyHmac(code, hmac),
                "El HMAC no cumple caracteres invalidos."
        );
    }
}
