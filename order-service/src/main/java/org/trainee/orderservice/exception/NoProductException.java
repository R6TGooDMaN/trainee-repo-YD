package org.trainee.orderservice.exception;

public class NoProductException extends RuntimeException {
    public NoProductException(String message) {
        super(message);
    }
}
