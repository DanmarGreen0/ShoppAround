package com.online_shopping_rest_api.dtos;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

/**
 * Transfers CartItem data between the controller and service layer of the application.
 */
@Getter
public class CartItemDTO extends RepresentationModel<CartItemDTO>
{
    private final Integer id;
    private final Integer sessionId;
    private final Integer quantity;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public CartItemDTO(Integer id, Integer sessionId, Integer quantity, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.sessionId = sessionId;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

}
