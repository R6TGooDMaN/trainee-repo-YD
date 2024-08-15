package org.trainee.orderservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.trainee.orderservice.dto.CartDto;

@Service
public class CartService {
    private RedisTemplate<String, Object> redisTemplate;
    private static final String CART_STRING_PREFIX = "cart:";
    private static final String CART_GET_PREFIX = "cart:get:";

    @Autowired
    public CartService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @CachePut(value = "cart", key = "#userId")
    public void addToCart(Long userId, CartDto cart) {
        redisTemplate.opsForValue().set(CART_STRING_PREFIX + userId, cart);
    }

    @Cacheable(value = "cart", key = "#userId")
    public CartDto getCart(Long userId) {
        return (CartDto) redisTemplate.opsForValue().get(CART_GET_PREFIX + userId);
    }

    @CacheEvict(value = "cart", key = "#userId")
    public void clearCart(Long userId) {
        redisTemplate.delete(CART_STRING_PREFIX + userId);
    }

    @CachePut(value = "cart", key = "#userId")
    public void removeFromCart(Long userId, Long productId) {
        CartDto cart = getCart(userId);
        if (cart != null) {
            cart.getCartItems().removeIf(item -> item.getProductId().equals(productId));
            addToCart(userId, cart);
        }
    }

}
