package com.online_shopping_rest_api.repositories;

import com.online_shopping_rest_api.models.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {
    void deleteByPaymentId(Integer paymentId);
    OrderDetails findByPaymentId(Integer paymentId);
}
