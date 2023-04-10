package com.online_shopping_rest_api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/*
    This entity class is persisting the orders item data to the table of a database.

    Relationship:
        - As a meany-to-one relationship with the Order_Details class
        - As a one-to-one relationship with the Product class
 */
@Entity
@Table(name="Order_Items")
@Getter
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private Integer productId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    public OrderItem(Integer id, Integer productId, LocalDateTime createdAt, LocalDateTime modifiedAt) {

        this.id = id;
        this.productId = productId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
