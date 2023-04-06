package com.online_shopping_rest_api.models;

import com.online_shopping_rest_api.utils.DateGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.springframework.security.config.authentication.AuthenticationManagerBeanDefinitionParser.NullAuthenticationProvider;

import java.time.LocalDateTime;

/*
    This class is responsible for persisting data for a product to the database.
    It represents products that a merchant sells, which a customer buys. It uses a
    builder pattern to reduce ambiguity when writing client code and to facilitate
    scalability when it comes on to adding optional parameters.

     Relationship:
        - As a one-to-one relationship with the Order_Items class
        - As a one-to-one relationship with the Cart_Items class
        - As a Many-to-one relationship with the Discount class
 */
@Entity
@Table(name = "Product")
@Getter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, unique = true)
    private String sku; // A stock-keeping unit, or SKU, is a unique code that a seller assigns to every
                        // type of item it sells.

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Double price;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Discount discount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    public static class Builder {

        private Integer id = 0;
        private final String name;
        private final String description;
        private final String sku; // A stock-keeping unit, or SKU, is a unique code that a seller assigns to every
                                  // type of item it sells.
        private final String category;
        private final Double price;
        private final LocalDateTime date = new DateGenerator().getLocalDate();
        private LocalDateTime createdAt = date;
        private LocalDateTime modifiedAt = date;
        private Discount discount;

        public Builder(String name, String description, String sku, String category, Double price) {
            this.name = name;
            this.description = description;
            this.sku = sku;
            this.category = category;
            this.price = price;
        }

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder discount(Discount discount) {
            if (discount == null)
                this.discount = null;
            else
                this.discount = new Discount(discount.getId(), discount.getName(), discount.getDescription(),
                        discount.getDiscountPercent(), discount.getProducts(), discount.getCreatedAt(),
                        discount.getModifiedAt());
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder modifiedAt(LocalDateTime modifiedAt) {
            this.modifiedAt = modifiedAt;
            return this;
        }

        public Product build() {
            return new Product(this);
        }

    }

    private Product(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.sku = builder.sku;
        this.category = builder.category;
        this.price = builder.price;
        this.discount = builder.discount;
        this.createdAt = builder.createdAt;
        this.modifiedAt = builder.modifiedAt;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", sku=" + sku +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }

}
