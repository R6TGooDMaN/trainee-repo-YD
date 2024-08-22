package org.trainee.orderservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class ProductOrderRequest {
    @NotNull
    private Long productId;
    @NotNull
    private Long orderId;
    @NotNull
    @Min(value = 0)
    private Integer quantity;
}
