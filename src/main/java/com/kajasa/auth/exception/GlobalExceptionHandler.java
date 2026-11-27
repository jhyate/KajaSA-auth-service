package com.kajasa.auth.exception;

import com.kajasa.auth.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones de Auth Service.
 * Captura las excepciones de la aplicación y genera
 * respuestas HTTP estandarizadas para los clientes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja los conflictos generados por las operaciones
     * de negocio y devuelve una respuesta HTTP 409.
     *
     * @param exception excepción de conflicto
     * @param request solicitud HTTP que originó el error
     * @return respuesta estandarizada de conflicto
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDto> handleConflict(
            ConflictException exception,
            HttpServletRequest request) {

        HttpStatus status = HttpStatus.CONFLICT;

        ErrorResponseDto response = new ErrorResponseDto(
                Instant.now(),
                status.value(),
                status.name(),
                exception.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(response);
    }

    /**
     * Maneja las excepciones de recursos no encontrados.
     * <p>
     * Construye una respuesta HTTP 404 con información sobre el error
     * y la ruta en la que se produjo.
     *
     * @param exception excepción que representa el recurso no encontrado.
     * @param request   solicitud HTTP que originó la excepción.
     * @return respuesta HTTP 404 con los detalles del error.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFound(
            ResourceNotFoundException exception,
            HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        ErrorResponseDto response = new ErrorResponseDto(
                Instant.now(),
                status.value(),
                status.name(),
                exception.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(response);
    }

    /**
     * Maneja las excepciones relacionadas con errores internos del servidor.
     * <p>
     * Construye una respuesta HTTP 500 utilizando un mensaje genérico
     * para evitar exponer detalles técnicos de la aplicación.
     *
     * @param exception excepción que representa el error interno.
     * @param request   solicitud HTTP que originó la excepción.
     * @return respuesta HTTP 500 con la información general del error.
     */
    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorResponseDto> handleInternalServer(
            InternalServerException exception,
            HttpServletRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorResponseDto response = new ErrorResponseDto(
                Instant.now(),
                status.value(),
                status.name(),
                exception.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(response);
    }

}
