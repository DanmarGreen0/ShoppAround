package com.online_shopping_rest_api.models;

import com.online_shopping_rest_api.exceptions.IllegalArgumentException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


/*
    This entity class persist data to the Order_Details table in a rational database.

    Relationship:
        - As a one-to-one relationship with the OrderItem class
        - As a one-to-one relationship with the User class
 */
@Entity
@Table(name="Order_Details")
@Getter
@NoArgsConstructor
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false, unique = true)
    private Integer paymentId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    /**
     * Returns an instantiation of a new OrderDetails
     * @throws IllegalArgumentException if total is a negative value
     */
    public OrderDetails(Integer id, Double total, Integer paymentId, LocalDateTime createdAt, LocalDateTime modifiedAt) {

        if(total < 0 || total == null) throw new IllegalArgumentException("Total can not be negative");

        this.id = id;
        this.total = total;
        this.paymentId = paymentId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
