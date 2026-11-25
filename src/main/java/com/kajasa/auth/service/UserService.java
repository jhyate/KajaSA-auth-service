package com.kajasa.auth.service;

import com.kajasa.auth.dto.UserCreateRequestDto;
import com.kajasa.auth.dto.UserResponseDto;

/**
 * Servicio encargado de gestionar los usuarios de KajaSA.
 * Administra el registro, consulta, actualización de información,
 * estado de las cuentas y gestión de credenciales de los usuarios.
 */
public interface UserService {

    UserResponseDto registerUser(UserCreateRequestDto userCreateRequestDto);
}
