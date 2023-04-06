package com.online_shopping_rest_api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/*
    This class is responsible for persisting data based on users' shopping sessions.
    A Shopping Session is the average time a customer spends on the site before
    leaving the online store.

    Relationship:
        - As a one-to-one relationship with the User class
        - As a one-to-many relationship with the Cart_Items class

 */
@Entity
@Table(name = "Shopping_Session")
@Getter
@NoArgsConstructor
public class ShoppingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private Integer user_id;

    @Column(nullable = false)
    private Integer total;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;


    public ShoppingSession(Integer id, Integer user_id, Integer total, LocalDateTime createAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.user_id = user_id;
        this.total = total;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
    }
}
