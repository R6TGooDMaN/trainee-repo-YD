package org.trainee.orderservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.trainee.orderservice.clients.ProductClient;
import org.trainee.orderservice.dto.OrderRequest;
import org.trainee.orderservice.dto.OrderResponse;
import org.trainee.orderservice.enums.OrderStatuses;
import org.trainee.orderservice.exception.OrderNotFoundMessage;
import org.trainee.orderservice.mapper.OrderMapper;
import org.trainee.orderservice.model.Order;
import org.trainee.orderservice.repository.OrderRepository;

import java.text.MessageFormat;
import java.util.List;

@Service
public class OrderService {
    public final OrderRepository orderRepository;
    public final String ORDER_NOT_FOUND_MESSAGE = "Entity with type: {0} with ID: {1} not found";
    public final ProductClient productClient;

    public OrderService(OrderRepository orderRepository, RestTemplate restTemplate, ProductClient productClient) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
    }

    public OrderResponse createOrder(OrderRequest orderRequest) {
        Order order = OrderMapper.mapToOrder(orderRequest);
        order.setOrderStatus(OrderStatuses.OPENED);
        Order savedOrder = orderRepository.save(order);
        return OrderMapper.mapToOrderResponse(savedOrder);
    }

    public OrderResponse getOrder(Long id) {
        String message = MessageFormat.format(ORDER_NOT_FOUND_MESSAGE, productClient.getOrderType(), id);
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundMessage(message));
        return OrderMapper.mapToOrderResponse(order);
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderMapper::mapToOrderResponse)
                .toList();
    }

    public OrderResponse updateOrderStatus(Long id, OrderRequest orderRequest, OrderStatuses status) {
        String message = MessageFormat.format(ORDER_NOT_FOUND_MESSAGE, productClient.getOrderType(), id);
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundMessage(message));
        order.setOrderStatus(status);
        return OrderMapper.mapToOrderResponse(order);
    }

    public void deleteOrder(Long id) {
        String message = MessageFormat.format(ORDER_NOT_FOUND_MESSAGE, productClient.getOrderType(), id);
        orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundMessage(message));
        orderRepository.deleteById(id);
    }

    public void cancelOrder(Long id) {
        String message = MessageFormat.format(ORDER_NOT_FOUND_MESSAGE, productClient.getOrderType(), id);
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundMessage(message));
        order.setOrderStatus(OrderStatuses.CANCELED);
        orderRepository.save(order);
    }

    public void payOrder(Long id) {
        String message = MessageFormat.format(ORDER_NOT_FOUND_MESSAGE, productClient.getOrderType(), id);
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundMessage(message));
        order.setOrderStatus(OrderStatuses.CLOSED);
        orderRepository.save(order);
    }
}
