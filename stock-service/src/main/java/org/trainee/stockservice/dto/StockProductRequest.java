package org.trainee.stockservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StockProductRequest {

    @NotNull(message = "ProductId cannot be null")
    private Long productId;

    @NotNull(message = "StockId cannot be null")
    private Long stockId;

    @Min(value = 1, message = "Quantity must be greater than zero")
    private Integer quantity;

}
