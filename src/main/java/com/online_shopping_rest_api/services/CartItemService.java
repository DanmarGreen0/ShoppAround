package com.online_shopping_rest_api.services;

import com.online_shopping_rest_api.models.CartItem;
import org.springframework.data.domain.Page;

public interface CartItemService {
    Page getCartItems(Integer pageNo, Integer pageSize, String[] sortBy, String sortDirection);
    <T> CartItem getCartItem(T value);
    CartItem addCartItem(CartItem CartItem);
    CartItem updateCartItem(CartItem cartItemChanges, int id);
    String deleteCartItemById(Integer id);
    String deleteCartItemBySessionId(Integer sessionId);
}
