package com.online_shopping_rest_api.dtos;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

/**
 * Transfers payment data between the controller and service layer of the application.
 */
@Getter
public class PaymentDetailsDTO extends RepresentationModel<PaymentDetailsDTO> {

    private final Integer id;
    private final Integer orderId;
    private final Double amount;
    private final String provider;
    private final String status;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public PaymentDetailsDTO(Integer id, Integer orderId, Double amount, String provider,
                             String status, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.provider = provider;
        this.status = status;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
