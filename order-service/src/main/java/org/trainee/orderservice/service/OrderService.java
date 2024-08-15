package org.trainee.orderservice.service;

import org.springframework.stereotype.Service;
import org.trainee.orderservice.dto.OrderRequest;
import org.trainee.orderservice.dto.OrderResponse;
import org.trainee.orderservice.enums.OrderStatuses;
import org.trainee.orderservice.mapper.OrderMapper;
import org.trainee.orderservice.model.Order;
import org.trainee.orderservice.repository.OrderRepository;

import java.util.List;

@Service
public class OrderService {
    public final OrderRepository orderRepository;
    public final String ORDER_NOT_FOUND_MESSAGE = "Entity with type: {0} with ID: {1} not found";

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderResponse createOrder(OrderRequest orderRequest) {
        Order order = OrderMapper.mapToOrder(orderRequest);
        order.setOrderStatus(OrderStatuses.OPENED);
        Order savedOrder = orderRepository.save(order);
        return OrderMapper.mapToOrderResponse(savedOrder);
    }

    public OrderResponse getOrder(Long id) {
        //String message = MessageFormat.format(ORDER_NOT_FOUND_MESSAGE, EntityType.ORDER.name(), id);
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("aboba"));
        return OrderMapper.mapToOrderResponse(order);
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderMapper::mapToOrderResponse)
                .toList();
    }

    public OrderResponse updateOrderStatus(Long id, OrderRequest orderRequest, OrderStatuses status) {
        //String message = MessageFormat.format(ORDER_NOT_FOUND_MESSAGE, EntityType.ORDER.name(), id);
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("aboba"));
        order.setOrderStatus(status);
        return OrderMapper.mapToOrderResponse(order);
    }

    public void deleteOrder(Long id) {
        //String message = MessageFormat.format(ORDER_NOT_FOUND_MESSAGE, EntityType.ORDER.name(), id);
        orderRepository.findById(id).orElseThrow(() -> new RuntimeException("aboba"));
        orderRepository.deleteById(id);
    }

    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("aboba"));
        order.setOrderStatus(OrderStatuses.CANCELED);
        orderRepository.save(order);
    }

    public void payOrder(Long id) {
        //String message = MessageFormat.format(ORDER_NOT_FOUND_MESSAGE, EntityType.ORDER.name(), id);
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("aboba"));
        order.setOrderStatus(OrderStatuses.CLOSED);
        orderRepository.save(order);
    }
}
