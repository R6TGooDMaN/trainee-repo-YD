package org.trainee.orderservice.dto;

import lombok.Data;

@Data
public class CartItemsDto {
    private Long productId;
    private Integer quantity;
}
