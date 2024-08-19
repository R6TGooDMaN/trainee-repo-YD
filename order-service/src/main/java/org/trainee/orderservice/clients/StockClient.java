package org.trainee.orderservice.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.trainee.orderservice.dto.StockProductDto;

@Component
public class StockClient {
    private final RestTemplate restTemplate;
    private final String stockUrl = "api/v1/stock/";
    private final String serviceUrl;

    public StockClient(RestTemplate restTemplate, @Value("${stock-service.url}") String serviceUrl) {
        this.restTemplate = restTemplate;
        this.serviceUrl = serviceUrl;
    }

    public StockProductDto getProduct(Long id) {
        return restTemplate.getForObject(serviceUrl + stockUrl + id, StockProductDto.class);
    }

    public void UpdateProductQuantity(Integer productId, Integer quantity) {
        restTemplate.put(serviceUrl + stockUrl + productId + "/" + quantity, quantity);
    }

}
