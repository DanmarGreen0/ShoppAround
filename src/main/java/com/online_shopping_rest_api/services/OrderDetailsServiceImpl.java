package com.online_shopping_rest_api.services;

import com.online_shopping_rest_api.models.OrderDetails;
import com.online_shopping_rest_api.exceptions.IllegalArgumentException;
import com.online_shopping_rest_api.repositories.OrderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService{
    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    @Override
    public Page getOrdersDetails(Integer pageNo, Integer pageSize, String[] sortBy, String sortDirection) {

        final Pageable pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(getSortDirection(sortDirection),sortBy));

        return orderDetailsRepository.findAll(pageRequest);
    }

    @Override
    public <T> OrderDetails getOrderDetails(T value) {

        return orderDetailsRepository.findById((int)value).get();
    }

    @Override
    public OrderDetails addOrderDetails(OrderDetails orderDetails) {
        return orderDetailsRepository.saveAndFlush(orderDetails);
    }

    @Override
    public OrderDetails updateOrderDetails(OrderDetails OrderDetailsChanges, int id) {
        final OrderDetails oldOrderDetails = orderDetailsRepository.findById(id).
                orElseThrow(()-> new IllegalArgumentException("Could not found order details +"
                        + " associated with the id = " + id + "."));

        OrderDetails updatedOrderDetails = new OrderDetails(
                oldOrderDetails.getId(),
                OrderDetailsChanges.getTotal() == 0.0 ? oldOrderDetails.getTotal() : OrderDetailsChanges.getTotal(),
                OrderDetailsChanges.getPaymentId() == null ? oldOrderDetails.getPaymentId() : OrderDetailsChanges.getPaymentId(),
                OrderDetailsChanges.getCreatedAt() == null ? oldOrderDetails.getCreatedAt() : OrderDetailsChanges.getCreatedAt(),
                OrderDetailsChanges.getModifiedAt() == null ? oldOrderDetails.getModifiedAt() : OrderDetailsChanges.getModifiedAt()
        );

        return orderDetailsRepository.save(updatedOrderDetails);
    }

    @Override
    public String deleteOrderDetailsById(Integer id) {
        try{

            orderDetailsRepository.deleteById(id);

        }catch (IllegalArgumentException ex){
            throw new IllegalArgumentException("Order Details with id = " + id + " doesn't exists.");
        }

        return "Order Details with id = " + id + " was successfully deleted.";
    }

    @Override
    public String deleteOrderDetailsByPaymentId(Integer paymentId) {
        try{

            orderDetailsRepository.deleteByPaymentId(paymentId);

        }catch (IllegalArgumentException ex){
            throw new IllegalArgumentException("Order Details with id = " + paymentId + " doesn't exists.");
        }

        return "Order Details with id = " + paymentId + " was successfully deleted.";
    }

    private Sort.Direction getSortDirection(String direction){

        if(direction.equalsIgnoreCase("asc")){
            return Sort.Direction.ASC;
        }

        return Sort.Direction.DESC;
    }
}
