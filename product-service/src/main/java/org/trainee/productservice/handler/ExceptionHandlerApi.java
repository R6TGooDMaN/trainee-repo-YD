package org.trainee.productservice.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.trainee.productservice.exception.EntityNotFoundException;

@RestControllerAdvice
public class ExceptionHandlerApi {

    private static final String HANDLER_ERROR_MESSAGE = "An unexpected error occurred: ";
    private static final String ARGUMENT_VALIDATION_MESSAGE = "Method argument not valid: ";

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(HANDLER_ERROR_MESSAGE + ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ARGUMENT_VALIDATION_MESSAGE + ex.getMessage());
    }

}