package org.trainee.orderservice.dto;

import lombok.Data;

@Data
public class StockProductDto {
    private Long productId;
    private Long stockId;
    private Integer quantity;
}
