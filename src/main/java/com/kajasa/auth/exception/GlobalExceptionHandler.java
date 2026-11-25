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

}
