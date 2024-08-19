package org.trainee.orderservice.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.trainee.orderservice.dto.ProductDto;

@Component
public class ProductClient {

    private final RestTemplate restTemplate;
    private final String serviceUrl;
    private final String productUrl = "/api/v1/products";
    private final String productTypeEntityUrl = "/etype_product";
    private final String orderTypeEntityUrl = "/etype_order";
    private final String userTypeEntityUrl = "/etype_user";

    public ProductClient(RestTemplate restTemplate, @Value("${product-service.url}") String serviceUrl) {
        this.restTemplate = restTemplate;
        this.serviceUrl = serviceUrl;
    }

    public ProductDto getProduct(Long id) {
        return restTemplate.getForObject(serviceUrl + productUrl + id, ProductDto.class);
    }

    public String getProductType(){
        return restTemplate.getForObject(serviceUrl + productUrl + productTypeEntityUrl, String.class);
    }
    public String getOrderType(){
        return restTemplate.getForObject(serviceUrl + productUrl + orderTypeEntityUrl, String.class);
    }
    public String getUserType(){
        return restTemplate.getForObject(serviceUrl + productUrl + userTypeEntityUrl, String.class);
    }

}
