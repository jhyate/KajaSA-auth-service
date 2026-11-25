package com.kajasa.auth.service;

import com.kajasa.auth.dto.UserCreateRequestDto;
import com.kajasa.auth.dto.UserResponseDto;
import com.kajasa.auth.enums.UserStatus;
import java.util.UUID;

/**
 * Servicio encargado de gestionar los usuarios de KajaSA.
 * Administra el registro, consulta, actualización de información,
 * estado de las cuentas y gestión de credenciales de los usuarios.
 */
public interface UserService {

    UserResponseDto registerUser(UserCreateRequestDto userCreateRequestDto);

    UserResponseDto getUserById(UUID userId);

    UserResponseDto getUserByEmail(String email);

    UserResponseDto updateStatusByUserId(UUID userId, UserStatus status);

}
