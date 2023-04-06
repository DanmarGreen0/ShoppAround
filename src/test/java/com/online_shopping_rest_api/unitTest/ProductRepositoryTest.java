package com.online_shopping_rest_api.unitTest;

import com.online_shopping_rest_api.models.Product;
import com.online_shopping_rest_api.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    // creates a product entry, then insert it into the database inside the Product
    // table
    @Test
    public void addProduct() {

        final String name = "TENS Unit and EMS Muscle Stimulator";
        final String description = "TENS Unit and EMS Muscle Stimulator Combination for Pain Relief, Arthrits and Muscle Recovery - Treats Tired and Sore Muscles in Your Shoulders, Back, Ab's, Legs, Knee's and More";
        final String category = "Health & Personal Care";
        final String sku = "125680664";
        double price = 45.00;

        final Product product1 = new Product.Builder(name, description, sku, category, price).build();
        final Product product2 = productRepository.save(product1);
        final Product product3 = productRepository.findById(product2.getId()).get();

        assertThat(name).isEqualTo(product3.getName());
        assertThat(description).isEqualTo(product3.getDescription());
        assertThat(category).isEqualTo(product3.getCategory());
        assertThat(sku).isEqualTo(product3.getSku());
        assertThat(price).isEqualTo(product3.getPrice());
    }

}
