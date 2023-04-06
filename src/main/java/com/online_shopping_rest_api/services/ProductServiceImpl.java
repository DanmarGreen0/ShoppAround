package com.online_shopping_rest_api.services;

import com.online_shopping_rest_api.dtos.DiscountDTO;
import com.online_shopping_rest_api.dtos.ProductDTO;
import com.online_shopping_rest_api.exceptions.IllegalArgumentException;
import com.online_shopping_rest_api.models.Product;
import com.online_shopping_rest_api.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public Page<Product> getProducts(Integer pageNo, Integer pageSize, String[] sortBy, String sortDirection) {

        final Pageable pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(getSortDirection(sortDirection), sortBy));

        return productRepository.findAll(pageRequest);
    }

    @Override
    public <T> ProductDTO getProduct(T value) {

        if (value instanceof String) {
            return entityToDTO(productRepository.findByName((String) value));
        }

        return entityToDTO(productRepository.findById((int) value).get());
    }

    @Override
    public ProductDTO addProduct(Product product) {
        return entityToDTO(productRepository.save(product));
    }

    @Override
    public ProductDTO updateProduct(Product productChanges, int id) {

        final Product oldProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Could not be found a product +"
                        + " associated with the id = " + id + "."));

        Product updatedProduct = new Product.Builder(
                productChanges.getName() == null ? oldProduct.getName() : productChanges.getName(),
                productChanges.getDescription() == null ? oldProduct.getDescription() : productChanges.getDescription(),
                productChanges.getSku() == null ? oldProduct.getSku() : productChanges.getSku(),
                productChanges.getCategory() == null ? oldProduct.getCategory() : productChanges.getCategory(),
                productChanges.getPrice() == 0 ? oldProduct.getPrice() : productChanges.getPrice()).discount(
                        (productChanges.getDiscount() == null) ? oldProduct.getDiscount()
                                : productChanges.getDiscount())
                .id(
                        oldProduct.getId())
                .build();

        return entityToDTO(productRepository.save(updatedProduct));
    }

    @Override
    public <T> String deleteProduct(T value) {

        try {
            if (value instanceof Integer) {

                productRepository.deleteById((int) value);

            } else if (value instanceof String) {

                productRepository.deleteByName((String) value);

            }
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Product doesn't exists.");
        }

        return "Product successfully deleted.";
    }

    ///////////////////////////////////////////////////////// remove functions below

    private static ProductDTO entityToDTO(Product product) {

        DiscountDTO discount = null;

        if (product.getDiscount() != null)
            discount = new DiscountDTO(
                    product.getDiscount().getId(),
                    product.getDiscount().getName(),
                    product.getDiscount().getDescription(),
                    product.getDiscount().getDiscountPercent(),
                    null,
                    product.getDiscount().getCreatedAt(),
                    product.getDiscount().getModifiedAt());

        return new ProductDTO(product.getId(), product.getName(), product.getDescription(), product.getSku(),
                product.getCategory(), product.getPrice(), discount, product.getCreatedAt(),
                product.getModifiedAt());
    }

    private Sort.Direction getSortDirection(String direction) {

        if (direction.equalsIgnoreCase("asc")) {
            return Sort.Direction.ASC;
        }

        return Sort.Direction.DESC;
    }

}
