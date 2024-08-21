package org.trainee.orderservice.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class TokenNotValidException extends JsonProcessingException {
    public TokenNotValidException(String message) {
        super(message);
    }
}
