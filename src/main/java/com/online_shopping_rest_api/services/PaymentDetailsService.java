package com.online_shopping_rest_api.services;

import com.online_shopping_rest_api.models.PaymentDetails;
import org.springframework.data.domain.Page;

public interface PaymentDetailsService {
    Page getPaymentDetails(Integer pageNo, Integer pageSize, String[] sortBy, String sortDirection);

    <T> PaymentDetails getPaymentDetails(T value);

    PaymentDetails addPaymentDetails(PaymentDetails paymentDetails);

    PaymentDetails updatePaymentDetails(PaymentDetails paymentDetailsChanges, int id);

    String deletePaymentDetailsById(Integer id);

    String deletePaymentDetailsByPaymentId(Integer orderId);
}
