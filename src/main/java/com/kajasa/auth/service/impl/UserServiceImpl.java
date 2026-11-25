package com.kajasa.auth.service.impl;

import com.kajasa.auth.dto.UserCreateRequestDto;
import com.kajasa.auth.dto.UserResponseDto;
import com.kajasa.auth.entity.UserEntity;
import com.kajasa.auth.enums.UserStatus;
import com.kajasa.auth.exception.ConflictException;
import com.kajasa.auth.mapper.UserMapper;
import com.kajasa.auth.repository.UserRepository;
import com.kajasa.auth.service.UserService;
import java.util.Locale;
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
}
