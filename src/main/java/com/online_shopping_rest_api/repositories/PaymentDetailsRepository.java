package com.online_shopping_rest_api.repositories;

import com.online_shopping_rest_api.models.OrderDetails;
import com.online_shopping_rest_api.models.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails,Integer> {
    void deleteByOrderId(Integer paymentId);
    OrderDetails findByOrderId(Integer paymentId);
}
