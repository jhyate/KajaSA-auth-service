package com.kajasa.auth.dto;

import com.kajasa.auth.enums.UserStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO utilizado para devolver la información pública de un usuario.
 * Contiene su identificador, correo electrónico, estado de la cuenta
 * y fechas de creación y verificación del correo electrónico.
 * Excluye información sensible como contraseñas, hashes y tokens.
 */
@Data
@ToString
@NoArgsConstructor
public class UserResponseDto {

    private UUID id;
    private String email;
    private UserStatus status;
    private Instant emailVerifiedAt;
    private Instant createdAt;
}
