package org.trainee.orderservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.trainee.orderservice.key.ProductOrderKey;

@Entity
@Table(name = "product_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ProductOrderKey.class)
@Builder
public class ProductOrders {
    @Id
    private Long productId;
    @Id
    private Long orderId;
    private Integer quantity;
}
