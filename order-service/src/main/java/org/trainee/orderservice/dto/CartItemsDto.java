package org.trainee.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class CartItemsDto {

    private Long productId;
    private Integer quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItemsDto cartItems = (CartItemsDto) o;
        return Objects.equals(productId, cartItems.productId);
    }

    @Override
    public int hashCode() {
        return productId.hashCode();
    }
}
