package org.trainee.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trainee.orderservice.dto.OrderResponse;
import org.trainee.orderservice.model.Order;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<OrderResponse> findByOrderDate(LocalDate orderDate);
}
