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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;

    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    @Transactional
    @Override
    public UserResponseDto registerUser(UserCreateRequestDto userCreateRequestDto) {

        log.info("Registering user with email: {}", userCreateRequestDto.getEmail());

        // normalizacion del correo electrónico
        String normalizedEmail = userCreateRequestDto
                .getEmail()
                .strip()
                .toLowerCase(Locale.ROOT);

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

    @Override
    public UserResponseDto getUserById(UUID userId) {

        log.info("Fetching user by ID: {}", userId);

        UserEntity user = getUserEntityById(userId);

        log.debug("User found with ID: {}", userId);

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponseDto getUserByEmail(String email) {

        log.info("Fetching user by email: {}", email);

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found by email: {}", email);
                    return new ResourceNotFoundException(
                            "No fue posible encontrar el registro solicitado."
                    );
                });

        log.debug("User found with email: {}", email);

        return userMapper.toResponse(user);
    }

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
}
