package com.online_shopping_rest_api.repositories;

import com.online_shopping_rest_api.models.Product;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

//@RepositoryRestResource(collectionResourceRel = "products", path = "products")
@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {
    void deleteByName(String name);

    Product findByName(String name);
}
