package org.trainee.orderservice.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.trainee.orderservice.dto.ProductDto;

@Component
public class ProductClient {

    private final RestTemplate restTemplate;
    private final String serviceUrl;

    public ProductClient(RestTemplate restTemplate, @Value("${product-service.url}") String serviceUrl) {
        this.restTemplate = restTemplate;
        this.serviceUrl = serviceUrl;
    }

    public ProductDto getProduct(Long id) {
        return restTemplate.getForObject(serviceUrl + "/api/v1/products/" + id, ProductDto.class);
    }
}
