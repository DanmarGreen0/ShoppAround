package com.online_shopping_rest_api.services;


import com.online_shopping_rest_api.models.OrderItem;
import org.springframework.data.domain.Page;

public interface OrderItemService {
    Page getOrderItems(Integer pageNo, Integer pageSize, String[] sortBy, String sortDirection);
    <T> OrderItem getOrderItem(T value);
    OrderItem addOrderItem(OrderItem OrderItem);
    OrderItem updateOrderItem(OrderItem orderItemChanges, int id);
    String deleteOrderItemById(Integer id);
    String deleteOrderItemByPaymentId(Integer productId);
}
