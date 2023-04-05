package com.online_shopping_rest_api.models;

import com.online_shopping_rest_api.exceptions.IllegalArgumentException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


/*
    This entity class persist data to the table of a relational database.
    It encapsulates the receipt details for a product bought form a
    merchant by a user.

    Relationship:
        - As a one-to-one relationship with the Order_Details class
 */
@Entity
@Table(name="Payment_Details")
@Getter
@NoArgsConstructor
public class PaymentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false, unique = true)
    private Integer orderId;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    public PaymentDetails(Integer id, Integer orderId, Double amount,
                          String provider, String status, LocalDateTime createdAt, LocalDateTime modifiedAt) {

        if(amount <= 0) throw new IllegalArgumentException("Payment amount can't be negative.");

        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.provider = provider;
        this.status = status;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
