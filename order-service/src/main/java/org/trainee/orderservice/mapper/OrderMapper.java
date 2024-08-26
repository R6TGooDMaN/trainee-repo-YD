package org.trainee.orderservice.mapper;

import lombok.experimental.UtilityClass;
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
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .build();
    }

    public Order mapToOrder(OrderRequest orderRequest) {
        return Order.builder()
                .userId(orderRequest.getUserId())
                .orderNumber(orderRequest.getOrderNumber())
                .orderDate(orderRequest.getOrderDate())
                .orderStatus(orderRequest.getOrderStatus())
                .build();
    }

    public Order updateOrderFromRequest(Order order, OrderRequest orderRequest) {
        order.setUserId(orderRequest.getUserId());
        order.setOrderNumber(orderRequest.getOrderNumber());
        order.setOrderDate(orderRequest.getOrderDate());
        order.setOrderStatus(order.getOrderStatus());
        return order;
    }
}
