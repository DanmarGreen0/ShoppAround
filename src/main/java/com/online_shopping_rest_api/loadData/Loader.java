package com.online_shopping_rest_api.loadData;

import com.online_shopping_rest_api.models.Product;
import com.online_shopping_rest_api.utils.DateGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class Loader {

    private String randomNumber() {

        int min = 000000;
        int max = 999999;
        Random random = new Random();

        return Integer.toString(random.ints(min, max)
                .findFirst()
                .getAsInt());
    }

    private String randomString() {

        Random rnd = new Random();
        String str = "";
        int strLen = 2;

        for (int i = 0; i < strLen; i++) {
            str = str + (char) ('a' + rnd.nextInt(26));
        }

        return str.toLowerCase();
    }

    private Product createProduct(String name, String description, String sku, String category, Double price,
            LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return new Product.Builder(name, description, sku, category, price).createdAt(createdAt).modifiedAt(modifiedAt)
                .build();
    }

    public List<Product> products() {

        List<Product> newProducts = new ArrayList<>();

        newProducts.add(createProduct("Apple 2022 MacBook Pro Laptop",
                "Apple 2022 MacBook Pro Laptop with M2 chip: 13-inch Retina Display, 8GB RAM, 256GB Storage, Touch Bar, Backlit Keyboard, FaceTime HD Camera. Works with iPhone and iPad; Space Gray",
                randomString() + randomNumber(),
                "Devices",
                1149.00, DateGenerator.getLocalDate(), DateGenerator.getLocalDate()));

        newProducts.add(createProduct("Rich Dad Poor Dad",
                " What the Rich Teach Their Kids About Money That the Poor and Middle Class Do Not! ",
                randomString() + randomNumber(),
                "Book",
                12.99, DateGenerator.getLocalDate(), DateGenerator.getLocalDate()));

        newProducts.add(createProduct("TOSHIBA EM925A5A-BS Countertop Microwave Oven",
                "0.9 Cu Ft With 10.6 Inch Removable Turntable, 900W, 6 Auto Menus, Mute Function & ECO Mode, Child Lock, LED Lighting, Black Stainless Steel",
                randomString() + randomNumber(),
                "Appliances",
                114.00, DateGenerator.getLocalDate(), DateGenerator.getLocalDate()));

        return newProducts;

    }

}