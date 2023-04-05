package com.online_shopping_rest_api.services;

import com.online_shopping_rest_api.models.PaymentDetails;
import com.online_shopping_rest_api.exceptions.IllegalArgumentException;
import com.online_shopping_rest_api.repositories.PaymentDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PaymentDetailsServiceImpl implements PaymentDetailsService{

    @Autowired
    PaymentDetailsRepository paymentDetailsRepository;

    @Override
    public Page getPaymentDetails(Integer pageNo, Integer pageSize, String[] sortBy, String sortDirection) {
        final Pageable pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(getSortDirection(sortDirection),sortBy));

        return paymentDetailsRepository.findAll(pageRequest);
    }

    @Override
    public <T> PaymentDetails getPaymentDetails(T value) {
        return paymentDetailsRepository.findById((int)value).get();
    }

    @Override
    public PaymentDetails addPaymentDetails(PaymentDetails paymentDetails) {
        return paymentDetailsRepository.saveAndFlush(paymentDetails);
    }

    @Override
    public PaymentDetails updatePaymentDetails(PaymentDetails paymentDetailsChanges, int id) {

        final PaymentDetails oldPaymentDetails = paymentDetailsRepository.findById(id).
                orElseThrow(()-> new IllegalArgumentException("Could not found order details +"
                        + " associated with the id = " + id + "."));

        PaymentDetails updatedOrderDetails = new PaymentDetails(
                oldPaymentDetails.getId(),
                paymentDetailsChanges.getOrderId() == 0.0 ? oldPaymentDetails.getOrderId() : paymentDetailsChanges.getOrderId(),
                paymentDetailsChanges.getAmount() == 0.0 ? oldPaymentDetails.getAmount() : paymentDetailsChanges.getAmount(),
                paymentDetailsChanges.getProvider() == null ? oldPaymentDetails.getProvider() : paymentDetailsChanges.getProvider(),
                paymentDetailsChanges.getStatus() == null ? oldPaymentDetails.getStatus() : paymentDetailsChanges.getStatus(),
                paymentDetailsChanges.getCreatedAt() == null ? oldPaymentDetails.getCreatedAt() : paymentDetailsChanges.getCreatedAt(),
                paymentDetailsChanges.getModifiedAt() == null ? oldPaymentDetails.getModifiedAt() : paymentDetailsChanges.getModifiedAt()
        );

        return paymentDetailsRepository.save(updatedOrderDetails);
    }

    @Override
    public String deletePaymentDetailsById(Integer id) {
        try{

            paymentDetailsRepository.deleteById(id);

        }catch (IllegalArgumentException ex){
            throw new IllegalArgumentException("Payment Details with id = " + id + " doesn't exists.");
        }

        return "Payment Details with id = " + id + " was successfully deleted.";
    }

    @Override
    public String deletePaymentDetailsByPaymentId(Integer orderId) {
        try{

            paymentDetailsRepository.deleteByOrderId(orderId);

        }catch (IllegalArgumentException ex){
            throw new IllegalArgumentException("Order Details with id = " + orderId + " doesn't exists.");
        }

        return "Order Details with id = " + orderId + " was successfully deleted.";
    }

    private Sort.Direction getSortDirection(String direction){

        if(direction.equalsIgnoreCase("asc")){
            return Sort.Direction.ASC;
        }

        return Sort.Direction.DESC;
    }
}
