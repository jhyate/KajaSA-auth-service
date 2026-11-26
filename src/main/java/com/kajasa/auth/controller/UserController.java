package com.kajasa.auth.controller;

import com.kajasa.auth.dto.UserCreateRequestDto;
import com.kajasa.auth.dto.UserResponseDto;
import com.kajasa.auth.enums.UserStatus;
import com.kajasa.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controlador REST encargado de gestionar las operaciones
 * relacionadas con los usuarios del sistema de autenticación
 * de KajaSA.
 * <p>
 * Expone los endpoints para el registro y la gestión de
 * información de los usuarios, delegando las operaciones
 * de negocio al servicio correspondiente.
 * <p>
 * Proporciona documentación de los endpoints mediante
 * anotaciones OpenAPI.
 */
@RestController
@RequestMapping("${application.api.auth.main-url}/users")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "User API", description = "API para la gestión de usuarios")
public class UserController {

    private final UserService userService;

    /**
     * Registra un nuevo usuario en KajaSA.
     * <p>
     * Recibe el correo electrónico y la contraseña del usuario,
     * valida los datos proporcionados y delega el proceso de
     * registro al servicio de usuarios.
     *
     * @param userCreateRequestDto DTO con los datos necesarios
     *                             para registrar al usuario.
     * @return ResponseEntity con los datos públicos del usuario
     * registrado y el código HTTP 201 CREATED.
     */
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Registrar usuario",
            description = "Registra un nuevo usuario en KajaSA mediante su correo electrónico y contraseña. La cuenta queda pendiente de verificación del correo electrónico."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario registrado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Los datos enviados no cumplen las validaciones requeridas"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "No fue posible completar el registro con los datos proporcionados"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<UserResponseDto> registerUser(
            @Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {

        log.info("Registering new user");

        UserResponseDto response = userService.registerUser(userCreateRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtiene la información pública de un usuario mediante su identificador.
     *
     * @param id identificador único del usuario.
     * @return ResponseEntity con la información del usuario encontrado
     * y el código HTTP 200 OK.
     */
    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Obtener usuario por ID",
            description = "Retorna la información pública de un usuario mediante su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No fue posible encontrar el registro solicitado"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<UserResponseDto> getUserById(
            @Parameter(
                    name = "id",
                    description = "Identificador único UUID del usuario",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable(name = "id") UUID id) {

        log.info("Fetching user by ID");

        UserResponseDto response = userService.getUserById(id);

        return ResponseEntity.ok(response);
    }


    @GetMapping(
            value = "/email",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Obtener usuario por correo electrónico",
            description = "Retorna la información pública de un usuario mediante su dirección de correo electrónico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<UserResponseDto> getUserByEmail(
            @Parameter(
                    name = "email",
                    description = "Dirección de correo electrónico del usuario",
                    required = true,
                    example = "usuario@ejemplo.com"
            )
            @RequestParam(name = "email") String email) {

        log.info("Fetching user by email");

        UserResponseDto response = userService.getUserByEmail(email);

        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza el estado de un usuario mediante su identificador.
     * <p>
     * Valida que la transición entre el estado actual y el nuevo estado
     * esté permitida antes de realizar la actualización.
     *
     * @param id     identificador único del usuario.
     * @param status nuevo estado que se desea asignar al usuario.
     * @return ResponseEntity con la información actualizada del usuario
     * y el código HTTP 200 OK.
     */
    @PatchMapping(
            value = "/{id}/status",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Actualizar estado del usuario",
            description = "Actualiza el estado de un usuario existente, validando que la transición solicitada esté permitida."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estado del usuario actualizado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "El estado proporcionado no es válido"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No fue posible encontrar el usuario solicitado"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "La transición de estado solicitada no está permitida"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<UserResponseDto> updateStatusByUserId(
            @Parameter(
                    name = "id",
                    description = "Identificador único UUID del usuario",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable(name = "id") UUID id,

            @Parameter(
                    name = "status",
                    description = "Nuevo estado que se desea asignar al usuario",
                    required = true,
                    example = "BLOCKED"
            )
            @RequestParam(name = "status") UserStatus status) {

        log.info("Updating user status");

        UserResponseDto response = userService.updateStatusByUserId(id, status);

        return ResponseEntity.ok(response);
    }


}
