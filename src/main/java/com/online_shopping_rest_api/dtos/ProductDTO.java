package com.online_shopping_rest_api.dtos;

import com.online_shopping_rest_api.models.Discount;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

/**
 * Transfers product data between the controller and service layer of the
 * application.
 */
@Getter
@NoArgsConstructor(force = true)
// @Relation(collectionRelation = "usersss", itemRelation = "user")
public class ProductDTO extends RepresentationModel<ProductDTO> {
    private final Integer id;
    private final String name;
    private final String description;
    private final String sku;
    private final String category;
    private final Double price;
    private final DiscountDTO discount;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public ProductDTO(Integer id, String name, String description, String sku, String category, Double price,
            DiscountDTO discount, LocalDateTime createdAt, LocalDateTime modifiedAt) {

        if (name == null || description == null || sku == null ||
                category == null || price == 0) {

            String msg = null;

            if (name == null) {
                msg = "A name is required";
            } else if (description == null) {
                msg = "A description is required.";
            } else if (sku == null) {
                msg = "A description is required.";
            } else if (category == null) {
                msg = "A category is required.";
            } else if (price <= 0.0) {
                msg = "A price is required.";
            }

            throw new IllegalArgumentException(msg);
        }

        this.id = id;
        this.name = name;
        this.description = description;
        this.sku = sku;
        this.category = category;
        this.price = price;
        if (discount != null) {
            this.discount = new DiscountDTO(discount.getId(), discount.getName(), discount.getDescription(),
                    discount.getDiscountPercent(), discount.getProducts(), discount.getCreatedAt(),
                    discount.getModifiedAt());
        } else {
            this.discount = null;
        }
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;

    }

}