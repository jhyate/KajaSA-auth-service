package com.kajasa.auth.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO utilizado para representar las respuestas de error de la API.
 * Proporciona información estandarizada sobre el error ocurrido,
 * incluyendo la fecha, el código HTTP, el tipo de error,
 * el mensaje descriptivo y la ruta de la solicitud.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {

    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}
