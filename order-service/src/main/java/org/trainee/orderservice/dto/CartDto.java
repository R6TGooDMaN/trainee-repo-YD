package org.trainee.orderservice.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class CartDto {
    private Long userId;
    private Set<CartItemsDto> cartItems;
}
