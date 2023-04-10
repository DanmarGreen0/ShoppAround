package com.online_shopping_rest_api.services;

import com.online_shopping_rest_api.models.CartItem;
import com.online_shopping_rest_api.exceptions.IllegalArgumentException;
import com.online_shopping_rest_api.repositories.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
/*

 */
@Service
public class CartItemServiceImpl implements CartItemService{

    @Autowired
    CartItemRepository cartItemRepository;

    @Override
    public Page getCartItems(Integer pageNo, Integer pageSize, String[] sortBy, String sortDirection) {

        final Pageable pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(getSortDirection(sortDirection),sortBy));

        return cartItemRepository.findAll(pageRequest);
    }

    @Override
    public <T> CartItem getCartItem(T value) {

        return cartItemRepository.findById((int)value).get();
    }

    @Override
    public CartItem addCartItem(CartItem cartItem) {
        return cartItemRepository.saveAndFlush(cartItem);
    }

    @Override
    public CartItem updateCartItem(CartItem cartItemChanges, int id) {
        final CartItem oldCartItem = cartItemRepository.findById(id).
                orElseThrow(()-> new IllegalArgumentException("Could not be found a product +"
                        + " associated with the id = " + id + "."));

        CartItem updatedCartItem = new CartItem(
                oldCartItem.getId(),
                cartItemChanges.getSessionId() == null ? oldCartItem.getSessionId() : cartItemChanges.getSessionId(),
                cartItemChanges.getQuantity() == null ? oldCartItem.getQuantity() : cartItemChanges.getQuantity(),
                cartItemChanges.getCreatedAt() == null ? oldCartItem.getCreatedAt() : cartItemChanges.getCreatedAt(),
                cartItemChanges.getModifiedAt() == null ? oldCartItem.getModifiedAt() : cartItemChanges.getModifiedAt()
        );

        return cartItemRepository.save(updatedCartItem);
    }

    @Override
    public String deleteCartItemById(Integer id) {
        try{

            cartItemRepository.deleteById(id);

        }catch (IllegalArgumentException ex){
            throw new IllegalArgumentException("Cart Item with id = " + id + " doesn't exists.");
        }

        return "Cart Item with id = " + id + " was successfully deleted.";
    }

    @Override
    public String deleteCartItemBySessionId(Integer sessionId) {
        try{

            cartItemRepository.deleteBySessionId(sessionId);

        }catch (IllegalArgumentException ex){
            throw new IllegalArgumentException("Cart Item with id = " + sessionId + " doesn't exists.");
        }

        return "Cart Item with id = " + sessionId + " was successfully deleted.";
    }

    private Sort.Direction getSortDirection(String direction){

        if(direction.equalsIgnoreCase("asc")){
            return Sort.Direction.ASC;
        }

        return Sort.Direction.DESC;
    }
}

