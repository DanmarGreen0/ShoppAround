package com.online_shopping_rest_api.dtos;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

/**
 * Transfers order item data between the controller and service layer of the application.
 */
@Getter
public class OrderItemDTO extends RepresentationModel<OrderItemDTO> {

    private final Integer id;
    private final Integer productId;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public OrderItemDTO(Integer id, Integer productId, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.productId = productId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
