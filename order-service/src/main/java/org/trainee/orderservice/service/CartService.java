package org.trainee.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.trainee.orderservice.clients.ProductClient;
import org.trainee.orderservice.clients.UserClient;
import org.trainee.orderservice.dto.CartDto;
import org.trainee.orderservice.dto.CartItemsDto;
import org.trainee.orderservice.exception.NoCacheException;

import java.text.MessageFormat;
import java.util.Objects;

@Service
public class CartService {
    private final UserClient userClient;
    private static final String CART_STRING_PREFIX = "cart:";
    private final CacheManager cacheManager;
    private final RestTemplate restTemplate;
    private final String NO_CACHE_MESSAGE = "No cart exists of user with id: {0}";
    private final String CACHE_NAME = "cart";
    private final ProductClient productClient;

    @Value("${keycloak.port}")
    private String KEYCLOAK_PORT;

    @Value("${keycloak.endpoint}")
    private String KEYClOAK_TOKEN_ENDPOINT;

    @Value("${keycloak.client.secret}")
    private String clientSecret;
    private final Cache cache;

    @Autowired
    public CartService(UserClient userClient, CacheManager cacheManager, RestTemplate restTemplate, ProductClient productClient) {
        this.userClient = userClient;
        this.cacheManager = cacheManager;
        this.restTemplate = restTemplate;
        this.cache = cacheManager.getCache(CACHE_NAME);
        this.productClient = productClient;
    }

    private String getCurrentUserToken() {
        String url = KEYCLOAK_PORT + KEYClOAK_TOKEN_ENDPOINT;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", "user-service");
        body.add("client_secret", clientSecret);
        body.add("grant_type", "client_credentials");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            if (responseBody != null) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    JsonNode jsonNode = mapper.readTree(responseBody);
                    return jsonNode.get("access_token").asText();
                } catch (JsonProcessingException e) {
                    System.out.println("Bad token");
                    ;
                }
            }
        }
        return "Token is empty!";
    }

    public void addToCart(Long userId, CartDto cart) {
        for (CartItemsDto cartItem : cart.getCartItems()) {
            productClient.getProductById(cartItem.getProductId());
        }
        userClient.getUser(userId, getCurrentUserToken());
        String cacheMessage = MessageFormat.format(NO_CACHE_MESSAGE, cart.getUserId());
        if (cache != null && Objects.equals(cart.getUserId(), userId)) {
            cache.put(CART_STRING_PREFIX + userId, cart);
        } else {
            throw new NoCacheException(cacheMessage);
        }
    }

    public CartDto getCart(Long userId) {
        userClient.getUser(userId, getCurrentUserToken());
        String cacheMessage = MessageFormat.format(NO_CACHE_MESSAGE, userId);
        if (cache != null) {
            return (CartDto) cache.get(CART_STRING_PREFIX + userId).get();
        } else {
            throw new NoCacheException(cacheMessage);
        }
    }

    public void clearCart(Long userId) {
        userClient.getUser(userId, getCurrentUserToken());
        String cacheMessage = MessageFormat.format(NO_CACHE_MESSAGE, userId);
        CartDto cart = getCart(userId);
        if (cache != null && cart != null) {
            cache.evict(CART_STRING_PREFIX + userId);
        } else {
            throw new NoCacheException(cacheMessage);
        }
    }

    public void removeFromCart(Long userId, Long productId) {
        userClient.getUser(userId, getCurrentUserToken());
        String cacheMessage = MessageFormat.format(NO_CACHE_MESSAGE, userId);
        CartDto cart = getCart(userId);
        if (cart != null) {
            cart.getCartItems().removeIf(item -> item.getProductId().equals(productId));
            addToCart(userId, cart);
        } else {
            throw new NoCacheException(cacheMessage);
        }
    }
}
