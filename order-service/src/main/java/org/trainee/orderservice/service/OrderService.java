package org.trainee.orderservice.service;

import org.springframework.stereotype.Service;
import org.trainee.orderservice.clients.ProductClient;
import org.trainee.orderservice.dto.OrderRequest;
import org.trainee.orderservice.dto.OrderResponse;
import org.trainee.orderservice.enums.OrderStatuses;
import org.trainee.orderservice.exception.OrderNotFoundMessage;
import org.trainee.orderservice.mapper.CartMapper;
import org.trainee.orderservice.mapper.OrderMapper;
import org.trainee.orderservice.model.Cart;
import org.trainee.orderservice.model.CartItems;
import org.trainee.orderservice.model.Order;
import org.trainee.orderservice.model.ProductOrders;
import org.trainee.orderservice.repository.OrderRepository;
import org.trainee.orderservice.repository.ProductOrderRepository;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {
    public final OrderRepository orderRepository;
    public final String ORDER_NOT_FOUND_MESSAGE = "Entity with type: {0} with ID: {1} not found";
    public final CartService cartService;
    public final ProductClient productClient;
    public final ProductOrderRepository productOrderRepository;

    public OrderService(OrderRepository orderRepository, CartService cartService, ProductClient productClient, ProductOrderRepository productOrderRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.productClient = productClient;
        this.productOrderRepository = productOrderRepository;
    }

    public OrderResponse createOrder(OrderRequest orderRequest) {
        Cart cart = CartMapper.mapToCart(cartService.getCart(orderRequest.getUserId()));
        Set<CartItems> itemsList = cart.getItems();
        Order order = OrderMapper.mapToOrder(orderRequest);
        for (CartItems cartItems : itemsList) {
            ProductOrders productOrders = new ProductOrders();
            productOrders.setOrderId(order.getId());
            productOrders.setProductId(cartItems.getProductId());
            productOrders.setQuantity(cartItems.getQuantity());
            productOrderRepository.save(productOrders);
        }
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
