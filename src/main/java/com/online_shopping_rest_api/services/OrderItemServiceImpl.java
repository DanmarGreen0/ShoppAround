package com.online_shopping_rest_api.services;

import com.online_shopping_rest_api.models.OrderItem;
import com.online_shopping_rest_api.exceptions.IllegalArgumentException;
import com.online_shopping_rest_api.repositories.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl implements OrderItemService{

    @Autowired
    OrderItemRepository orderItemRepository;

    @Override
    public Page getOrderItems(Integer pageNo, Integer pageSize, String[] sortBy, String sortDirection) {

        final Pageable pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(getSortDirection(sortDirection),sortBy));

        return orderItemRepository.findAll(pageRequest);
    }

    @Override
    public <T> OrderItem getOrderItem(T value) {

        return orderItemRepository.findById((int)value).get();
    }

    @Override
    public OrderItem addOrderItem(OrderItem orderitem) {
        return orderItemRepository.saveAndFlush(orderitem);
    }

    @Override
    public OrderItem updateOrderItem(OrderItem OrderItemChanges, int id) {
        final OrderItem oldOrderItem = orderItemRepository.findById(id).
                orElseThrow(()-> new IllegalArgumentException("Could not found order item +"
                        + " associated with the id = " + id + "."));

        OrderItem updatedOrderItem = new OrderItem(
                oldOrderItem.getId(),
                OrderItemChanges.getProductId() == null ? oldOrderItem.getProductId() : OrderItemChanges.getProductId(),
                OrderItemChanges.getCreatedAt() == null ? oldOrderItem.getCreatedAt() : OrderItemChanges.getCreatedAt(),
                OrderItemChanges.getModifiedAt() == null ? oldOrderItem.getModifiedAt() : OrderItemChanges.getModifiedAt()
        );

        return orderItemRepository.save(updatedOrderItem);
    }

    @Override
    public String deleteOrderItemById(Integer id) {
        try{

            orderItemRepository.deleteById(id);

        }catch (IllegalArgumentException ex){
            throw new IllegalArgumentException("Order Item with id = " + id + " doesn't exists.");
        }

        return "Order Item with id = " + id + " was successfully deleted.";
    }

    @Override
    public String deleteOrderItemByPaymentId(Integer productId) {
        try{

            orderItemRepository.deleteByProductId(productId);

        }catch (IllegalArgumentException ex){
            throw new IllegalArgumentException("Order Item with id = " + productId + " doesn't exists.");
        }

        return "Order Item with id = " + productId + " was successfully deleted.";
    }

    private Sort.Direction getSortDirection(String direction){

        if(direction.equalsIgnoreCase("asc")){
            return Sort.Direction.ASC;
        }

        return Sort.Direction.DESC;
    }
}
