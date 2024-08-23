package org.trainee.orderservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.trainee.orderservice.key.ProductOrderKey;

@Entity
@Table(name = "product_orders")
@Getter
@Setter
@IdClass(ProductOrderKey.class)
public class ProductOrders {
    @Id
    private Long productId;
    @Id
    private Long orderId;
    private Integer quantity;
}
