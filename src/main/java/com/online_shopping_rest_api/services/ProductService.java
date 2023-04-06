package com.online_shopping_rest_api.services;

import com.online_shopping_rest_api.dtos.ProductDTO;
import com.online_shopping_rest_api.models.Product;

import org.springframework.data.domain.Page;

public interface ProductService {

    Page<Product> getProducts(Integer pageNo, Integer pageSize, String[] sortBy, String sortDirection);

    <T> ProductDTO getProduct(T value);

    ProductDTO addProduct(Product product);

    ProductDTO updateProduct(Product productChanges, int id);

    <T> String deleteProduct(T value);

}
