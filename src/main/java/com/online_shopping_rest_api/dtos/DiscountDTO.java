package com.online_shopping_rest_api.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.online_shopping_rest_api.exceptions.IllegalArgumentException;
import com.online_shopping_rest_api.models.Product;
import com.online_shopping_rest_api.utils.DateGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Transfers Discount data between the controller and service layer of the
 * application.
 */
@Getter
@NoArgsConstructor(force = true)
public class DiscountDTO extends RepresentationModel<DiscountDTO> {

    private Integer id;
    private String name;
    private String description;
    private Double discountPercent;
    private List<Product> products;
    private LocalDateTime createdAt = DateGenerator.getLocalDate();
    private LocalDateTime modifiedAt = DateGenerator.getLocalDate();

    public DiscountDTO(Integer id, String name, String description, Double discountPercent,
            List<Product> products, LocalDateTime createdAt, LocalDateTime modifiedAt) {

        if (name == null) {
            throw new IllegalArgumentException("Discount must have a name.");
        }

        if (discountPercent == 0) {
            throw new IllegalArgumentException("Discount must have a percentage greater than 0");
        }

        if (createdAt != null) {
            this.createdAt = createdAt;
        }

        if (createdAt != null) {
            this.modifiedAt = modifiedAt;
        }

        this.id = id;
        this.name = name;
        this.description = description;
        this.discountPercent = discountPercent;
        this.products = (products != null) ? Collections.unmodifiableList(products) : new ArrayList<>();

    }

}
