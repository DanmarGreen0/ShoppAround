package com.online_shopping_rest_api.services;

import com.online_shopping_rest_api.models.Discount;
import org.springframework.data.domain.Page;

public interface DiscountService {

    Page<Discount> getDiscounts(Integer pageNo, Integer pageSize, String[] sortBy, String sortDirection);

    <T> Discount getDiscount(T value);

    Discount addDiscount(Discount discount);

    Discount updateDiscount(Discount discountChanges, int id);

    <T> String deleteDiscount(T value);
}
