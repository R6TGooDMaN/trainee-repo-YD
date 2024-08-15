package org.trainee.orderservice.dto;

import lombok.Data;

@Data
public class ProductOrderRequest {
    private Long productId;
    private Long orderId;
    private Integer quantity;
}
