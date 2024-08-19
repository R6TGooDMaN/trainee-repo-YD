package org.trainee.orderservice.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.trainee.orderservice.dto.StockProductDto;

@Component
public class UserClient {
    private final RestTemplate restTemplate;
    private final String userUrl = "api/v1/user/";
    private final String serviceUrl;

    public UserClient(RestTemplate restTemplate, @Value("${user-service.url}") String serviceUrl) {
        this.restTemplate = restTemplate;
        this.serviceUrl = serviceUrl;
    }

    public StockProductDto getUser(Long id) {
        return restTemplate.getForObject(serviceUrl + userUrl + id, StockProductDto.class);
    }

    public void UpdateProductQuantity(Integer productId, Integer quantity) {
        restTemplate.put(serviceUrl + userUrl + productId + "/" + quantity, quantity);
    }

}
