package com.online_shopping_rest_api.models;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/*
    This entity class is responsible for persisting a cart item data to the Cart_Item table in a rational database.
    It encapsulates data of a product or item a user inserted into their digital cart. These items maybe
    purchased at any time the user wishes if still in stock at that specific time.

    Relationship:
        - As a many-to-one relationship with ShoppingSession class
        - As a one-to-one relationship with the Product class.
*/
@Entity
@Table(name = "Cart_Item")
@Getter
@NoArgsConstructor
public class CartItem { //Still in construction

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cartItem_id",nullable = false)
    private Integer id;

    @Column(nullable = false)
    private Integer sessionId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    public CartItem(Integer id, Integer sessionId, Integer quantity, LocalDateTime createdAt, LocalDateTime modifiedAt) {

        this.id = id;
        this.sessionId = sessionId;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
