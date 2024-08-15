package org.trainee.orderservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.trainee.orderservice.dto.CartDto;

@Service
public class CartService {
    private static final String CART_STRING_PREFIX = "cart:";
    private CacheManager cacheManager;

    @Autowired
    public CartService( CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }


    //@CachePut(value = "cart", key = "#userId")
    public void addToCart(Long userId, CartDto cart) {
        Cache cache = cacheManager.getCache("cart");
        if (cache != null) {
            cache.put(CART_STRING_PREFIX + userId, cart);
        }
    }

    @Cacheable(value = "cart", key = "#userId")
    public CartDto getCart(Long userId) {
        Cache cache = cacheManager.getCache("cart");
        if (cache != null) {
        return (CartDto) cache.get(CART_STRING_PREFIX + userId).get();
        }
        return null;
    }

    //@CacheEvict(value = "cart", key = "#userId")
    public void clearCart(Long userId) {
        Cache cache = cacheManager.getCache("cart");
        if (cache != null) {
            cache.evict(CART_STRING_PREFIX + userId);
        }
    }

    //@CachePut(value = "cart", key = "#userId")
    public void removeFromCart(Long userId, Long productId) {
        CartDto cart = getCart(userId);
        if (cart != null) {
            cart.getCartItems().removeIf(item -> item.getProductId().equals(productId));
            addToCart(userId, cart);
        }
    }

}
