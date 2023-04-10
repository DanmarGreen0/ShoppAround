package com.online_shopping_rest_api.services;

import com.online_shopping_rest_api.models.Discount;
import com.online_shopping_rest_api.exceptions.IllegalArgumentException;
import com.online_shopping_rest_api.repositories.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    DiscountRepository discountRepository;

    @Override
    public Page<Discount> getDiscounts(Integer pageNo, Integer pageSize, String[] sortBy, String sortDirection) {

        final Pageable pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(getSortDirection(sortDirection), sortBy));

        return discountRepository.findAll(pageRequest);
    }

    @Override
    public <T> Discount getDiscount(T value) {

        if (value instanceof String) {
            return discountRepository.findByName((String) value);
        }

        return discountRepository.findById((int) value).get();
    }

    @Override
    public Discount addDiscount(Discount discount) {
        return discountRepository.saveAndFlush(discount);
    }

    @Override
    public Discount updateDiscount(Discount discountChanges, int id) {

        final Discount oldDiscount = discountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Could not be found a product +"
                        + " associated with the id = " + id + "."));

        Discount updatedDiscount = new Discount(
                oldDiscount.getId(),
                discountChanges.getName() == null ? oldDiscount.getName() : discountChanges.getName(),
                discountChanges.getDescription() == null ? oldDiscount.getDescription()
                        : discountChanges.getDescription(),
                discountChanges.getDiscountPercent() == 0.0 ? oldDiscount.getDiscountPercent()
                        : discountChanges.getDiscountPercent(),
                discountChanges.getProducts() == null ? oldDiscount.getProducts() : discountChanges.getProducts(),
                discountChanges.getCreatedAt() == null ? oldDiscount.getCreatedAt() : discountChanges.getCreatedAt(),
                discountChanges.getModifiedAt() == null ? oldDiscount.getModifiedAt()
                        : discountChanges.getModifiedAt());

        return discountRepository.save(updatedDiscount);
    }

    @Override
    public <T> String deleteDiscount(T value) {

        try {
            if (value instanceof Integer) {

                discountRepository.deleteById((int) value);

            } else if (value instanceof String) {

                discountRepository.deleteByName((String) value);

            }
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Discount doesn't exists.");
        }

        return "Discount successfully deleted.";
    }

    private Sort.Direction getSortDirection(String direction) {

        if (direction.equalsIgnoreCase("asc")) {
            return Sort.Direction.ASC;
        }

        return Sort.Direction.DESC;
    }

}
