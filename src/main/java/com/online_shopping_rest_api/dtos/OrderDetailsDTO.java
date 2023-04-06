package com.online_shopping_rest_api.dtos;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

/**
 * Transfers order details data between the controller and service layer of the application.
 */
@Getter
public class OrderDetailsDTO extends RepresentationModel<OrderDetailsDTO> {

    private final Integer id;
    private final Double total;
    private final Integer paymentId;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public OrderDetailsDTO(Integer id, Double total, Integer paymentId, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.total = total;
        this.paymentId = paymentId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
