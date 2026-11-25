package com.kajasa.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO utilizado para recibir la información necesaria
 * durante el registro de un nuevo usuario.
 * Contiene el correo electrónico y la contraseña
 * proporcionados por el usuario.
 */
@Getter
@NoArgsConstructor
public class UserCreateRequestDto {

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Size(max = 254, message = "El correo electrónico no puede exceder los 254 caracteres")
    @Email(message = "El correo electrónico debe tener un formato válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 128, message = "La contraseña debe tener entre 8 y 128 caracteres")
    private String password;
}
