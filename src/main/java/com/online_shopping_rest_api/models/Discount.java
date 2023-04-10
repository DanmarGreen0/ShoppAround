package com.online_shopping_rest_api.models;

import com.online_shopping_rest_api.exceptions.IllegalArgumentException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
    This entity class persists data to the Discount table in a rational database.
    A discount can be associated with zero-many products.

    Relationship:
        - As one-many relationship with the class Product.
 */
@Entity
@Table(name = "Discount")
@Getter
@NoArgsConstructor
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double discountPercent;

    @OneToMany(mappedBy = "discount")
    private List<Product> products;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    /**
     * Returns an Instantiation of a new Discount.
     *
     * @throws IllegalArgumentException if name is empty
     * @throws IllegalArgumentException if description is empty
     * @throws IllegalArgumentException if discountPercentage is less than or equal
     *                                  to zero
     */
    public Discount(Integer id, String name, String description, Double discountPercent,
            List<Product> products, LocalDateTime createdAt, LocalDateTime modifiedAt) {

        if (name.isEmpty())
            throw new IllegalArgumentException("Discount name is empty.");
        if (description.isEmpty())
            throw new IllegalArgumentException("Discount description is empty.");
        if (discountPercent <= 0)
            throw new IllegalArgumentException("Discount percentage must be grater than zero.");

        this.id = id;
        this.name = name;
        this.description = description;
        this.discountPercent = discountPercent;
        this.products = (products != null) ? new ArrayList<>(products) : new ArrayList<>();
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
