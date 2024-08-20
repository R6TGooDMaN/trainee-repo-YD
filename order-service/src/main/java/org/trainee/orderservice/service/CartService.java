package org.trainee.orderservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.trainee.orderservice.dto.CartDto;
import org.trainee.orderservice.exception.NoCacheException;

@Service
public class CartService {
    private static final String CART_STRING_PREFIX = "cart:";
    private final CacheManager cacheManager;
    private final RestTemplate restTemplate;
    private final String NO_CACHE_MESSAGE = "There is no cache";
    private final String CACHE_NAME = "cart";
    private final Cache cache;

    @Autowired
    public CartService(CacheManager cacheManager, RestTemplate restTemplate) {
        this.cacheManager = cacheManager;
        this.restTemplate = restTemplate;
        this.cache = cacheManager.getCache(CACHE_NAME);
    }



    public void addToCart(Long userId, CartDto cart) {
        if (cache != null) {
            cache.put(CART_STRING_PREFIX + userId, cart);
        } else {
            throw new NoCacheException(NO_CACHE_MESSAGE);
        }
    }

    @Cacheable(value = "cart", key = "#userId")
    public CartDto getCart(Long userId) {
        if (cache != null) {
            return (CartDto) cache.get(CART_STRING_PREFIX + userId).get();
        }
        return null;
    }

    public void clearCart(Long userId) {
        if (cache != null) {
            cache.evict(CART_STRING_PREFIX + userId);
        }
    }

    public void removeFromCart(Long userId, Long productId) {
        CartDto cart = getCart(userId);
        if (cart != null) {
            cart.getCartItems().removeIf(item -> item.getProductId().equals(productId));
            addToCart(userId, cart);
        }
    }

}
