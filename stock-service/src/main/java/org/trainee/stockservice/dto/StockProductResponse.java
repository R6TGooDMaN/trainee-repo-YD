package org.trainee.stockservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockProductResponse {
    private Long productId;
    private Long stockId;
    private Integer quantity;
}
