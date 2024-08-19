package org.trainee.orderservice.exception;

public class OrderNotFoundMessage extends RuntimeException {
    public OrderNotFoundMessage(String message) {
        super(message);
    }

}
