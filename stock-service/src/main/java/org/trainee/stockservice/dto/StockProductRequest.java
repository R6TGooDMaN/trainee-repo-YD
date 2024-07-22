package org.trainee.stockservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StockProductRequest {
    private Long productId;
    private Long stockId;
    private Integer quantity;

}
