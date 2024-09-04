package org.trainee.stockservice.exception;

public class NoProductException extends RuntimeException {
    public NoProductException(String message) {
        super(message);
    }
}
