package com.online_shopping_rest_api.services;

import com.online_shopping_rest_api.models.OrderDetails;
import org.springframework.data.domain.Page;

public interface OrderDetailsService {
    Page getOrdersDetails(Integer pageNo, Integer pageSize, String[] sortBy, String sortDirection);
    <T> OrderDetails getOrderDetails(T value);
    OrderDetails addOrderDetails(OrderDetails orderDetails);
    OrderDetails updateOrderDetails(OrderDetails orderDetailsChanges, int id);
    String deleteOrderDetailsById(Integer id);
    String deleteOrderDetailsByPaymentId(Integer paymentId);
}
