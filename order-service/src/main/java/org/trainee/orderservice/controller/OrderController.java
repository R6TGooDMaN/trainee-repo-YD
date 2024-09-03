package org.trainee.orderservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trainee.orderservice.dto.OrderRequest;
import org.trainee.orderservice.dto.OrderResponse;
import org.trainee.orderservice.dto.ProductOrderResponse;
import org.trainee.orderservice.enums.OrderStatuses;
import org.trainee.orderservice.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @PostMapping("/open")
    public ResponseEntity<OrderResponse> openOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    @PutMapping("/{id}/{status}")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long id, @RequestBody OrderRequest orderRequest, @PathVariable OrderStatuses status) {
        OrderResponse orderResponse = orderService.updateOrderStatus(id, orderRequest, status);
        return ResponseEntity.ok(orderResponse);
    }

    @DeleteMapping
    public ResponseEntity<OrderResponse> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/pay")
    public ResponseEntity<OrderResponse> payOrder(@PathVariable Long id) {
        orderService.payOrder(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{productId}/products")
    public ResponseEntity<List<ProductOrderResponse>> getProductsInOrders(@PathVariable Long productId) {
        List<ProductOrderResponse> productOrderResponses = orderService.getProducts(productId);
        return ResponseEntity.ok(productOrderResponses);
    }
}
