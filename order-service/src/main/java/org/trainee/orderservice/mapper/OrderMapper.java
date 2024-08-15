package org.trainee.orderservice.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;
import org.trainee.orderservice.dto.OrderRequest;
import org.trainee.orderservice.dto.OrderResponse;
import org.trainee.orderservice.model.Order;

@UtilityClass
public class OrderMapper {
    public OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .Id(order.getId())
                .userId(order.getUserId())
                .orderNumber(order.getOrderNumber())
                .build();
    }

    public Order mapToOrder(OrderRequest orderRequest) {
        return Order.builder()
                .userId(orderRequest.getUserId())
                .orderNumber(orderRequest.getOrderNumber())
                .build();
    }

    public Order updateOrderFromRequest(Order order, OrderRequest orderRequest) {
        order.setUserId(orderRequest.getUserId());
        order.setOrderNumber(orderRequest.getOrderNumber());
        order.setOrderStatus(order.getOrderStatus());
        return order;
    }
}
