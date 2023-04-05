package com.online_shopping_rest_api.repositories;

import com.online_shopping_rest_api.models.OrderDetails;
import com.online_shopping_rest_api.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {
    void deleteByProductId(Integer productId);
    OrderDetails findByProductId(Integer productId);
}
