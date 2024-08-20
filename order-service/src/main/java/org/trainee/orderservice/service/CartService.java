package org.trainee.orderservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.trainee.orderservice.clients.UserClient;
import org.trainee.orderservice.dto.CartDto;
import org.trainee.orderservice.exception.NoCacheException;
import org.trainee.orderservice.exception.NoUserException;

import java.text.MessageFormat;

@Service
public class CartService {
    private final UserClient userClient;
    private static final String CART_STRING_PREFIX = "cart:";
    private final CacheManager cacheManager;
    private final RestTemplate restTemplate;
    private final String NO_CACHE_MESSAGE = "No cart exists of user with id: {0}";
    private final String NO_USER_MESSAGE = "There is no user with id: {0}";
    private final String CACHE_NAME = "cart";
    private final Cache cache;

    @Autowired
    public CartService(UserClient userClient, CacheManager cacheManager, RestTemplate restTemplate) {
        this.userClient = userClient;
        this.cacheManager = cacheManager;
        this.restTemplate = restTemplate;
        this.cache = cacheManager.getCache(CACHE_NAME);
    }



    public void addToCart(Long userId, CartDto cart) {
        String userMessage = MessageFormat.format(NO_USER_MESSAGE, userId);
        String cacheMessage = MessageFormat.format(NO_CACHE_MESSAGE, userId);
        if (userClient.getUser(userId) == null) {
           throw new NoUserException(userMessage);
        } else
        if (cache != null) {
            cache.put(CART_STRING_PREFIX + userId, cart);
        } else {
            throw new NoCacheException(cacheMessage);
        }
    }

    @Cacheable(value = "cart", key = "#userId")
    public CartDto getCart(Long userId) {
        String userMessage = MessageFormat.format(NO_USER_MESSAGE, userId);
        String cacheMessage = MessageFormat.format(NO_CACHE_MESSAGE, userId);
        if (userClient.getUser(userId) == null) {
            throw new NoUserException(userMessage);
        }
        if (cache != null) {
            return (CartDto) cache.get(CART_STRING_PREFIX + userId).get();
        } else {
            throw new NoCacheException(cacheMessage);
        }
    }

    public void clearCart(Long userId) {
        String userMessage = MessageFormat.format(NO_USER_MESSAGE, userId);
        String cacheMessage = MessageFormat.format(NO_CACHE_MESSAGE, userId);
        CartDto cart = getCart(userId);
        if (userClient.getUser(userId) == null) {
            throw new NoUserException(userMessage);
        }
        if (cache != null && cart != null) {
            cache.evict(CART_STRING_PREFIX + userId);
        } else {
            throw new NoCacheException(cacheMessage);
        }
    }

    public void removeFromCart(Long userId, Long productId) {
        String userMessage = MessageFormat.format(NO_USER_MESSAGE, userId);
        String cacheMessage = MessageFormat.format(NO_CACHE_MESSAGE, userId);
        CartDto cart = getCart(userId);
        if (userClient.getUser(userId) == null) {
            throw new NoUserException(userMessage);
        }
        if (cart != null) {
            cart.getCartItems().removeIf(item -> item.getProductId().equals(productId));
            addToCart(userId, cart);
        } else {
            throw new NoCacheException(cacheMessage);
        }
    }

}
