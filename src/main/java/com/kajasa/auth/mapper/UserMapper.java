package com.kajasa.auth.mapper;

import com.kajasa.auth.dto.UserCreateRequestDto;
import com.kajasa.auth.dto.UserResponseDto;
import com.kajasa.auth.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Convierte una solicitud de creación de usuario
     * en una entidad de usuario.
     *
     * La contraseña no se mapea porque debe convertirse
     * previamente mediante PasswordEncoder.
     *
     * @param request datos recibidos para crear el usuario
     * @return entidad de usuario
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "emailVerifiedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserEntity toEntity(UserCreateRequestDto request);

    /**
     * Convierte una entidad de usuario en su DTO de respuesta.
     *
     * @param user entidad de usuario
     * @return información pública del usuario
     */
    UserResponseDto toResponse(UserEntity user);
}
