package com.online_shopping_rest_api.repositories;

import com.online_shopping_rest_api.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
    void deleteBySessionId(Integer sessionId);
    CartItem findBySessionId(Integer sessionId);
}
