package org.trainee.orderservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.trainee.orderservice.key.ProductOrderKey;

@Entity
@Table(name = "product_order")
@Getter
@Setter
@IdClass(ProductOrderKey.class)
public class ProductOrders {

    @Id
    private Long productId;
    @Id
    private Long order_Id;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

}
