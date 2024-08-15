package org.trainee.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trainee.orderservice.key.ProductOrderKey;
import org.trainee.orderservice.model.ProductOrders;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrders, ProductOrderKey> {
}
