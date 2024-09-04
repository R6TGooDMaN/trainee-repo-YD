package org.trainee.orderservice.mapper;

import lombok.experimental.UtilityClass;
import org.trainee.orderservice.dto.CartDto;
import org.trainee.orderservice.dto.CartItemsDto;
import org.trainee.orderservice.model.Cart;
import org.trainee.orderservice.model.CartItems;

import java.util.Set;

@UtilityClass
public class CartMapper {
    private Set<CartItems> items;
    private Set<CartItemsDto> itemsDto;
    public Cart mapToCart(CartDto cart) {
        for (CartItemsDto itemsDto : cart.getCartItems()) {
            items.add(mapToCartItems(itemsDto));
        }
        return Cart.builder()
                .userId(cart.getUserId())
                .items(items)
                .build();
    }
    public CartDto mapToCartDto(Cart cart) {
        for (CartItems items : cart.getItems()) {
            itemsDto.add(mapToCartItemsDto(items));
        }
        return CartDto.builder()
                .userId(cart.getUserId())
                .cartItems(itemsDto)
                .build();
    }
    public CartItems mapToCartItems(CartItemsDto cartItems) {
        return CartItems.builder()
                .productId(cartItems.getProductId())
                .quantity(cartItems.getQuantity())
                .build();
    }
    public CartItemsDto mapToCartItemsDto(CartItems cartItems) {
        return CartItemsDto.builder()
                .productId(cartItems.getProductId())
                .quantity(cartItems.getQuantity())
                .build();

    }

}
