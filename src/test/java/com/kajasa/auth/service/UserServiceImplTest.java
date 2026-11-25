package com.kajasa.auth.service;

import com.kajasa.auth.dto.UserCreateRequestDto;
import com.kajasa.auth.dto.UserResponseDto;
import com.kajasa.auth.entity.UserEntity;
import com.kajasa.auth.enums.UserStatus;
import com.kajasa.auth.exception.ConflictException;
import com.kajasa.auth.exception.ResourceNotFoundException;
import com.kajasa.auth.mapper.UserMapper;
import com.kajasa.auth.repository.UserRepository;
import com.kajasa.auth.service.impl.UserServiceImpl;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        // Preparación inicial de las pruebas.
    }


    @Test
    void registerUser_shouldRegisterUserSuccessfully() {

        // Arrange: preparar los datos de entrada
        UserCreateRequestDto request = new UserCreateRequestDto();

        ReflectionTestUtils.setField(request, "email", " TEST@EMAIL.COM ");
        ReflectionTestUtils.setField(request, "password", "Password123");

        String normalizedEmail = "test@email.com";
        String encodedPassword = "encodedPassword";

        UserEntity userEntity = new UserEntity();

        UUID userId = UUID.randomUUID();

        UserEntity savedUser = new UserEntity();
        savedUser.setEmail(normalizedEmail);
        savedUser.setPasswordHash(encodedPassword);
        savedUser.setStatus(UserStatus.PENDING_VERIFICATION);

        UserResponseDto expectedResponse = new UserResponseDto();
        expectedResponse.setId(userId);
        expectedResponse.setEmail(normalizedEmail);
        expectedResponse.setStatus(UserStatus.PENDING_VERIFICATION);

        // Arrange: configurar el comportamiento de los mocks
        when(userRepository.existsByEmail(normalizedEmail))
                .thenReturn(false);

        when(passwordEncoder.encode("Password123"))
                .thenReturn(encodedPassword);

        when(userMapper.toEntity(request))
                .thenReturn(userEntity);

        when(userRepository.save(userEntity))
                .thenReturn(savedUser);

        when(userMapper.toResponse(savedUser))
                .thenReturn(expectedResponse);

        // Act: ejecutar el método que estamos probando
        UserResponseDto response = userService.registerUser(request);

        // Assert: validar el resultado
        assertNotNull(response);
        assertEquals(userId, response.getId());
        assertEquals(normalizedEmail, response.getEmail());
        assertEquals(UserStatus.PENDING_VERIFICATION, response.getStatus());

        // Verify: comprobar las interacciones
        verify(userRepository).existsByEmail(normalizedEmail);
        verify(passwordEncoder).encode("Password123");
        verify(userMapper).toEntity(request);
        verify(userRepository).save(userEntity);
        verify(userMapper).toResponse(savedUser);
    }

    @Test
    void registerUser_shouldThrowConflictExceptionWhenEmailAlreadyExists() {

        // Arrange
        UserCreateRequestDto request = new UserCreateRequestDto();

        ReflectionTestUtils.setField(request, "email", "test@email.com");
        ReflectionTestUtils.setField(request, "password", "Password123");

        when(userRepository.existsByEmail("test@email.com"))
                .thenReturn(true);

        // Act y Assert
        assertThrows(
                ConflictException.class,
                () -> userService.registerUser(request)
        );

        // Verify
        verify(userRepository).existsByEmail("test@email.com");

        verifyNoInteractions(passwordEncoder, userMapper);

        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void getUserById_shouldReturnUserWhenUserExists() {

        // Arrange: preparar los datos
        UUID userId = UUID.randomUUID();

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@email.com");
        userEntity.setStatus(UserStatus.ACTIVE);

        UserResponseDto expectedResponse = new UserResponseDto();
        expectedResponse.setId(userId);
        expectedResponse.setEmail("test@email.com");
        expectedResponse.setStatus(UserStatus.ACTIVE);

        // Configurar los mocks
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(userEntity));

        when(userMapper.toResponse(userEntity))
                .thenReturn(expectedResponse);

        // Act: ejecutar el método
        UserResponseDto response = userService.getUserById(userId);

        // Assert: comprobar el resultado
        assertNotNull(response);
        assertEquals(userId, response.getId());
        assertEquals("test@email.com", response.getEmail());
        assertEquals(UserStatus.ACTIVE, response.getStatus());

        // Verify: comprobar las llamadas
        verify(userRepository).findById(userId);
        verify(userMapper).toResponse(userEntity);
    }

    @Test
    void getUserById_shouldThrowResourceNotFoundExceptionWhenUserDoesNotExist() {

        // Arrange
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Act y Assert
        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserById(userId)
        );

        // Verify
        verify(userRepository).findById(userId);

        verifyNoInteractions(userMapper);
    }

    @Test
    void getUserByEmail_shouldReturnUserWhenUserExists() {

        // Arrange
        String email = " TEST@EMAIL.COM ";
        String normalizedEmail = "test@email.com";

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(normalizedEmail);
        userEntity.setStatus(UserStatus.ACTIVE);

        UserResponseDto expectedResponse = new UserResponseDto();
        expectedResponse.setEmail(normalizedEmail);
        expectedResponse.setStatus(UserStatus.ACTIVE);

        when(userRepository.findByEmail(normalizedEmail))
                .thenReturn(Optional.of(userEntity));

        when(userMapper.toResponse(userEntity))
                .thenReturn(expectedResponse);

        // Act
        UserResponseDto response = userService.getUserByEmail(email);

        // Assert
        assertNotNull(response);
        assertEquals(normalizedEmail, response.getEmail());
        assertEquals(UserStatus.ACTIVE, response.getStatus());

        // Verify
        verify(userRepository).findByEmail(normalizedEmail);
        verify(userMapper).toResponse(userEntity);
    }

    @Test
    void getUserByEmail_shouldThrowResourceNotFoundExceptionWhenUserDoesNotExist() {

        // Arrange
        String email = " TEST@EMAIL.COM ";
        String normalizedEmail = "test@email.com";

        when(userRepository.findByEmail(normalizedEmail))
                .thenReturn(Optional.empty());

        // Act y Assert
        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserByEmail(email)
        );

        // Verify
        verify(userRepository).findByEmail(normalizedEmail);

        verifyNoInteractions(userMapper);
    }

    @Test
    void updateStatusByUserId_shouldUpdateStatusSuccessfully() {

        // Arrange
        UUID userId = UUID.randomUUID();

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@email.com");
        userEntity.setStatus(UserStatus.ACTIVE);

        UserResponseDto expectedResponse = new UserResponseDto();
        expectedResponse.setId(userId);
        expectedResponse.setEmail("test@email.com");
        expectedResponse.setStatus(UserStatus.BLOCKED);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(userEntity));

        when(userMapper.toResponse(userEntity))
                .thenReturn(expectedResponse);

        // Act
        UserResponseDto response = userService.updateStatusByUserId(
                userId,
                UserStatus.BLOCKED
        );

        // Assert
        assertNotNull(response);
        assertEquals(UserStatus.BLOCKED, userEntity.getStatus());
        assertEquals(UserStatus.BLOCKED, response.getStatus());

        // Verify
        verify(userRepository).findById(userId);
        verify(userMapper).toResponse(userEntity);
    }

    @Test
    void updateStatusByUserId_shouldThrowConflictExceptionWhenTransitionIsInvalid() {

        // Arrange
        UUID userId = UUID.randomUUID();

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@email.com");
        userEntity.setStatus(UserStatus.DISABLED);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(userEntity));

        // Act y Assert
        assertThrows(
                ConflictException.class,
                () -> userService.updateStatusByUserId(
                        userId,
                        UserStatus.ACTIVE
                )
        );

        // Assert: el estado original no debe cambiar
        assertEquals(UserStatus.DISABLED, userEntity.getStatus());

        // Verify
        verify(userRepository).findById(userId);

        verifyNoInteractions(userMapper);
    }

    @Test
    void updateStatusByUserId_shouldThrowResourceNotFoundExceptionWhenUserDoesNotExist() {

        // Arrange
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Act y Assert
        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.updateStatusByUserId(
                        userId,
                        UserStatus.BLOCKED
                )
        );

        // Verify
        verify(userRepository).findById(userId);

        verifyNoInteractions(userMapper);
    }
}
