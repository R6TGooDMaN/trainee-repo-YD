package org.trainee.orderservice.dto;

import lombok.Data;

@Data
public class ProductOrderResponse {
    private Long productId;
    private Long orderId;
    private Integer quantity;
}
