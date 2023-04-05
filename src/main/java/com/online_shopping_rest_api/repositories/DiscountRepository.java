package com.online_shopping_rest_api.repositories;

import com.online_shopping_rest_api.models.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount,Integer> {
    void deleteByName(String name);
    Discount findByName(String name);
}
