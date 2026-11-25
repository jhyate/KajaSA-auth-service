package com.kajasa.auth.service.impl;

import com.kajasa.auth.dto.UserCreateRequestDto;
import com.kajasa.auth.dto.UserResponseDto;
import com.kajasa.auth.entity.UserEntity;
import com.kajasa.auth.enums.UserStatus;
import com.kajasa.auth.exception.ConflictException;
import com.kajasa.auth.exception.ResourceNotFoundException;
import com.kajasa.auth.mapper.UserMapper;
import com.kajasa.auth.repository.UserRepository;
import com.kajasa.auth.service.UserService;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio encargado de gestionar los usuarios
 * del sistema de autenticación de KajaSA.
 *
 * Contiene la lógica de negocio relacionada con el registro,
 * consulta y actualización del estado de los usuarios.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * Normaliza el correo electrónico, verifica que no esté registrado,
     * codifica la contraseña y crea el usuario con estado
     * PENDING_VERIFICATION.
     *
     * @param userCreateRequestDto DTO con los datos necesarios para registrar al usuario.
     * @return DTO con la información del usuario registrado.
     * @throws ConflictException si el correo electrónico ya está registrado.
     */
    @Transactional
    @Override
    public UserResponseDto registerUser(UserCreateRequestDto userCreateRequestDto) {

        log.info("Registering user with email: {}", userCreateRequestDto.getEmail());

        // normalizacion del correo electrónico
        String normalizedEmail = normalizeEmail(userCreateRequestDto.getEmail());

        log.debug("Normalized email: {}", normalizedEmail);

        if (userRepository.existsByEmail(normalizedEmail)) {
            log.warn("User with email {} already exists", normalizedEmail);
            throw new ConflictException(
                    "No fue posible completar el registro con los datos proporcionados."
            );
        }

        String hash = passwordEncoder
                .encode(userCreateRequestDto.getPassword());

        UserEntity user = userMapper.toEntity(userCreateRequestDto);
        user.setEmail(normalizedEmail);
        user.setPasswordHash(hash);
        user.setStatus(UserStatus.PENDING_VERIFICATION);

        UserEntity savedUser = userRepository.save(user);

        log.debug("User registered with ID: {}", savedUser.getId());

        UserResponseDto response = userMapper.toResponse(savedUser);

        log.info("User registration completed ID: {}", response.getId());

        return response;
    }

    /**
     * Obtiene la información de un usuario mediante su identificador.
     *
     * @param userId identificador único del usuario.
     * @return DTO con la información del usuario encontrado.
     * @throws ResourceNotFoundException si no existe un usuario con el identificador proporcionado.
     */
    @Override
    public UserResponseDto getUserById(UUID userId) {

        log.info("Fetching user by ID: {}", userId);

        UserEntity user = getUserEntityById(userId);

        log.debug("User found with ID: {}", userId);

        return userMapper.toResponse(user);
    }

    /**
     * Obtiene la información de un usuario mediante su correo electrónico.
     *
     * Normaliza el correo electrónico antes de realizar la consulta.
     *
     * @param email correo electrónico del usuario.
     * @return DTO con la información del usuario encontrado.
     * @throws ResourceNotFoundException si no existe un usuario con el correo proporcionado.
     */
    @Override
    public UserResponseDto getUserByEmail(String email) {

        log.info("Fetching user by email: {}", email);

        String normalizedEmail = normalizeEmail(email);

        UserEntity user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> {
                    log.warn("User not found by email: {}", normalizedEmail);
                    return new ResourceNotFoundException(
                            "No fue posible encontrar el registro solicitado."
                    );
                });

        log.debug("User found with email: {}", normalizedEmail);

        return userMapper.toResponse(user);
    }

    /**
     * Actualiza el estado de un usuario mediante su identificador.
     *
     * Valida que la transición entre el estado actual y el nuevo estado
     * esté permitida antes de realizar la actualización.
     *
     * La activación de usuarios pendientes de verificación debe realizarse
     * exclusivamente mediante el proceso de verificación del correo electrónico.
     *
     * @param userId identificador único del usuario.
     * @param status nuevo estado que se desea asignar al usuario.
     * @return DTO con la información actualizada del usuario.
     * @throws ResourceNotFoundException si no existe un usuario con el identificador proporcionado.
     * @throws ConflictException si la transición de estado no está permitida.
     */
    @Transactional
    @Override
    public UserResponseDto updateStatusByUserId(UUID userId, UserStatus status) {

        log.info("Updating status for user ID: {} to {}", userId, status);

        UserEntity user = getUserEntityById(userId);

        if (!isValidStatusTransition(user.getStatus(), status)) {
            log.warn("User status transition rejected");
            throw new ConflictException(
                    "No es posible realizar la transición al estado solicitado."
            );
        }

        user.setStatus(status);

        return userMapper.toResponse(user);
    }

    private boolean isValidStatusTransition(
            UserStatus currentStatus,
            UserStatus newStatus) {

        log.debug("Validating status transition from {} to {}",
                currentStatus, newStatus);

        switch (currentStatus) {
            case ACTIVE:
                return newStatus == UserStatus.BLOCKED
                        || newStatus == UserStatus.DISABLED;

            case BLOCKED:
                return newStatus == UserStatus.ACTIVE
                        || newStatus == UserStatus.DISABLED;

            case DISABLED:
            default:
                log.warn("Status transition is not allowed from {}", currentStatus);
                return false;
        }
    }

    private UserEntity getUserEntityById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No fue posible encontrar el registro solicitado."
                ));
    }

    private String normalizeEmail(String email) {
        return email
                .strip()
                .toLowerCase(Locale.ROOT);
    }
}
