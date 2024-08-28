package com.mydrive.backend.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Manejador global de excepciones para la aplicación.
 *
 * <p>Esta clase utiliza la anotación {@link ControllerAdvice} para manejar excepciones lanzadas en
 * los controladores de la aplicación. Proporciona métodos para gestionar diferentes tipos de excepciones
 * y devolver respuestas adecuadas al cliente con códigos de estado HTTP correspondientes.</p>
 *
 * <p>La clase está diseñada para capturar y registrar excepciones, y devolver mensajes de error con el
 * código de estado HTTP apropiado en función del tipo de excepción.</p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Logger para registrar mensajes de información y errores.
     */
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Maneja excepciones de tipo {@link IllegalArgumentException}.
     *
     * <p>Este método captura excepciones de tipo {@code IllegalArgumentException}, las registra utilizando
     * un logger, y devuelve una respuesta con un código de estado HTTP {@code 400 Bad Request}. El mensaje
     * de error de la excepción se incluye en la respuesta.</p>
     *
     * @param ex La excepción {@code IllegalArgumentException} que se está manejando.
     * @return Una respuesta {@code ResponseEntity} con el mensaje de error y el código de estado {@code 400}.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones de tipo {@link CloudLimitationException}.
     *
     * <p>Este método captura excepciones de tipo {@code CloudLimitationException}, las registra utilizando
     * un logger, y devuelve una respuesta con un código de estado HTTP {@code 409 Conflict}. El mensaje
     * de error de la excepción se incluye en la respuesta.</p>
     *
     * @param ex La excepción {@code CloudLimitationException} que se está manejando.
     * @return Una respuesta {@code ResponseEntity} con el mensaje de error y el código de estado {@code 409}.
     */
    @ExceptionHandler(CloudLimitationException.class)
    public ResponseEntity<String> handleCloudLimitationException(CloudLimitationException ex) {
        logger.error(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Maneja cualquier otra excepción no específica.
     *
     * <p>Este método captura excepciones genéricas de tipo {@code Exception}, las registra utilizando un
     * logger, y devuelve una respuesta con un código de estado HTTP {@code 500 Internal Server Error}. El
     * mensaje de error de la excepción se incluye en la respuesta.</p>
     *
     * @param ex La excepción {@code Exception} que se está manejando.
     * @return Una respuesta {@code ResponseEntity} con el mensaje de error y el código de estado {@code 500}.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        logger.error(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
