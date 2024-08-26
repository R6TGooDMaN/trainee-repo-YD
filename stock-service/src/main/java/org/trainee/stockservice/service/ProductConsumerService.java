package org.trainee.stockservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.trainee.stockservice.dto.ProductDto;

import java.util.concurrent.CompletableFuture;

@Service
public class ProductConsumerService {

    private final CompletableFuture<ProductDto> productFuture = new CompletableFuture<>();

    @KafkaListener(topics = "product-response-topic", groupId = "stock-service-group")
    public void consume(ProductDto productDto) {
        productFuture.complete(productDto);
    }

    public ProductDto getProductResponse(Long productId) {
        try {
            return productFuture.get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get response", e);
        }
    }

}
