package com.online_shopping_rest_api.integrationTest;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {
    @Autowired
    public TestRestTemplate restTemplate;

    //Add a product entry via a post request.
    //Get a response having the new add product from the database
//    @Test
//    public void addProduct() throws IllegalArgumentException{
//
//        String name = "TENS Unit and EMS Muscle Stimulator";
//        String description = "TENS Unit and EMS Muscle Stimulator Combination for Pain Relief, Arthrits and Muscle Recovery - Treats Tired and Sore Muscles in Your Shoulders, Back, Ab's, Legs, Knee's and More";
//        String category = "Health & Personal Care";
//        long sku = 125680664;
//        double price = 45.00;
//
//        final HttpEntity<ProductDTOss> request = new HttpEntity<>(new ProductDTOss(0,name,description, sku, category, price, null));
//        final ProductDTOss response = restTemplate.withBasicAuth("danmargreen","Terren17").postForObject("/product", request, ProductDTOss.class);
//
//        assertThat(name).isEqualTo(response.name());
//        assertThat(description).isEqualTo(response.description());
//        assertThat(category).isEqualTo(response.category());
//        assertThat(sku).isEqualTo(response.sku());
//        assertThat(price).isEqualTo(response.price());
//
//    }
//
//    @Test
//    public void editProduct() throws IllegalArgumentException{
//
//        String name = "new product name";
//        String description = "new description";
//        String category = null;
//        long sku = 0;
//        double price = 0;
//        Discount discount = null;
//
//        final HttpEntity<ProductDTOss> request = new HttpEntity<>(new ProductDTOss(0,name,description, sku, category, price, discount));
//        final ProductDTOss response = restTemplate.withBasicAuth("danmargreen","Terren17").postForObject("/product", request, ProductDTOss.class);
//
//        assertThat(name).isEqualTo(response.name());
//        assertThat(description).isEqualTo(response.description());
//        assertThat(category).isEqualTo(response.category());
//        assertThat(sku).isEqualTo(response.sku());
//        assertThat(price).isEqualTo(response.price());
//    }

}
