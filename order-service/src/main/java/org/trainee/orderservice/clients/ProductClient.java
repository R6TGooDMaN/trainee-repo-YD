package org.trainee.orderservice.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.trainee.orderservice.dto.ProductDto;
import org.trainee.orderservice.dto.UserDto;
import org.trainee.orderservice.exception.NoProductException;
import org.trainee.orderservice.exception.NoUserException;

import java.text.MessageFormat;

@Component
public class ProductClient {

    private final RestTemplate restTemplate;
    private final String serviceUrl;
    private final String productUrl = "/api/v1/products";
    private final String productTypeEntityUrl = "/etype_product";
    private final String orderTypeEntityUrl = "/etype_order";
    private final String userTypeEntityUrl = "/etype_user";
    private final String NO_PRODUCT_MESSAGE = "There is no product with id: {0}";

    public ProductClient(RestTemplate restTemplate, @Value("${product-service.url}") String serviceUrl) {
        this.restTemplate = restTemplate;
        this.serviceUrl = serviceUrl;
    }

    public ProductDto getProductById(Long id) {
        String productMessage = MessageFormat.format(NO_PRODUCT_MESSAGE, id);
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<ProductDto> response = restTemplate.exchange(serviceUrl + productUrl + id, HttpMethod.GET, entity,ProductDto.class);
            return response.getBody();
        } catch (HttpServerErrorException e) {
            throw new NoProductException(productMessage);
        }
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
