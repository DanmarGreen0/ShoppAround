package com.online_shopping_rest_api.controller;

import com.online_shopping_rest_api.dtos.DiscountDTO;
import com.online_shopping_rest_api.dtos.ProductDTO;
import com.online_shopping_rest_api.models.Product;
import com.online_shopping_rest_api.exceptions.BadRequestException;
import com.online_shopping_rest_api.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping(path = "/products")
    public ResponseEntity<Object> getProducts(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "100") Integer pageSize,
            @RequestParam(defaultValue = "name") String[] sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Page<Product> pageResult = productService.getProducts(pageNo, pageSize, sortBy, sortDirection);

        return new ResponseEntity<>(pageableToDTOList(pageResult), HttpStatus.OK);
    }

    @GetMapping(value = { "/product", "product/{id}" })
    public ResponseEntity<Object> getProduct(@RequestParam Optional<String> name, @PathVariable Optional<Integer> id) {

        ProductDTO productDTO = null;

        if (id.get() <= 0) {
            throw new IllegalArgumentException("Invalid argument.");
        } else if (name.isPresent()) {
            productDTO = addSelfLinkToProductDTO(productService.getProduct(name.get()));
        } else if (id.isPresent()) {
            productDTO = addSelfLinkToProductDTO(productService.getProduct(id.get()));
        }

        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @PostMapping(path = "/product")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO) {

        ProductDTO productJson = productService.addProduct(dtoToEntity(productDTO));

        Link selfLink = linkTo(methodOn(ProductController.class).addProduct(productDTO)).slash(productJson.getId())
                .withSelfRel();
        productJson.add(Collections.singleton(selfLink));

        return new ResponseEntity<>(productJson, HttpStatus.CREATED);
    }

    @PatchMapping(path = "/product/{id}", produces = "application/json")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO, @PathVariable int id) {

        if (id < 0)
            throw new BadRequestException("The value of the param id must be equal or greater than zero.");

        ProductDTO productJson = productService.updateProduct(dtoToEntity(productDTO), id);

        Link selfLink = linkTo(methodOn(ProductController.class).addProduct(productDTO)).slash(productJson.getId())
                .withSelfRel();
        productJson.add(Collections.singleton(selfLink));

        return new ResponseEntity<>(productJson, HttpStatus.OK);
    }

    @DeleteMapping(value = { "/product", "/product/{id}" })
    public ResponseEntity<String> deleteProduct(@RequestParam Optional<String> name,
            @PathVariable Optional<Integer> id) {

        if (id.get() <= 0) {
            throw new IllegalArgumentException("Invalid argument.");
        } else if (name.isPresent()) {
            productService.deleteProduct(name.get());
        } else if (id.isPresent()) {
            productService.deleteProduct(id.get());
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private static Product dtoToEntity(ProductDTO productDTO) {

        return new Product.Builder(
                productDTO.getName(), productDTO.getDescription(), productDTO.getSku(),
                productDTO.getCategory(), productDTO.getPrice()).discount(null).build();
    }

    private static ProductDTO addSelfLinkToProductDTO(ProductDTO productDTO) {

        Link selfLink = linkTo(methodOn(ProductController.class).addProduct(productDTO)).slash(productDTO.getId())
                .withSelfRel();
        productDTO.add(Collections.singleton(selfLink));

        return productDTO;
    }

    // add self links to the productDTO collection object and paging details
    private static Object pageableToDTOList(Page<Product> pageResult) {

        List<ProductDTO> productDTOList = new ArrayList<>();

        // add self-link that supports each product item of the product collection
        for (int i = 0; i < pageResult.getContent().size(); i++) {

            DiscountDTO discount = null;

            if (pageResult.getContent().get(i).getDiscount() != null)
                discount = new DiscountDTO(
                        pageResult.getContent().get(i).getDiscount().getId(),
                        pageResult.getContent().get(i).getDiscount().getName(),
                        pageResult.getContent().get(i).getDiscount().getDescription(),
                        pageResult.getContent().get(i).getDiscount().getDiscountPercent(),
                        null,
                        pageResult.getContent().get(i).getDiscount().getCreatedAt(),
                        pageResult.getContent().get(i).getDiscount().getModifiedAt());

            productDTOList.add(new ProductDTO(
                    pageResult.getContent().get(i).getId(),
                    pageResult.getContent().get(i).getName(),
                    pageResult.getContent().get(i).getDescription(),
                    pageResult.getContent().get(i).getSku(),
                    pageResult.getContent().get(i).getCategory(),
                    pageResult.getContent().get(i).getPrice(),
                    discount,
                    pageResult.getContent().get(i).getCreatedAt(),
                    pageResult.getContent().get(i).getModifiedAt()));

            Link selfLink = linkTo(methodOn(ProductController.class).addProduct(productDTOList.get(0)))
                    .slash(pageResult.getContent().get(i).getId()).withSelfRel();

            productDTOList.get(i).add(Collections.singleton(selfLink));
        }

        // get sortBy and orderBy property requested page result.
        List<Sort.Order> orders = pageResult.getSort().stream().collect(Collectors.toList());

        String[] sortBy = new String[orders.size()];
        String[] sortDirection = new String[orders.size()];

        for (int i = 0; i < orders.size(); i++) {
            sortBy[i] = orders.get(i).getProperty();
            sortDirection[i] = orders.get(i).getDirection().toString();
        }

        // add outer self-link that supports retrieving the product collection
        Link productsLink = linkTo(
                methodOn(ProductController.class).getProducts(pageResult.getPageable().getPageNumber(),
                        pageResult.getPageable().getPageSize(), sortBy, sortDirection[0]))
                .withSelfRel();

        Map<String, Object> jsonObj = new LinkedHashMap<>();
        jsonObj.put("products", CollectionModel.of(productDTOList, productsLink));

        for (Sort.Order order : pageResult.nextPageable().getSort()) {
            if (pageResult.nextPageable().isPaged())
                jsonObj.put("next_page_url",
                        linkTo(methodOn(ProductController.class).getProducts(pageResult.nextPageable().getPageNumber(),
                                pageResult.nextPageable().getPageSize(), sortBy, sortDirection[0])).withSelfRel());
        }

        for (Sort.Order order : pageResult.previousPageable().getSort()) {
            if (pageResult.previousPageable().isPaged())
                jsonObj.put("previous_page_url",
                        linkTo(methodOn(ProductController.class).getProducts(
                                pageResult.previousPageable().getPageNumber(),
                                pageResult.previousPageable().getPageSize(), sortBy, sortDirection[0])).withSelfRel());
        }

        jsonObj.put("other-info", pageResult.getPageable());

        return Collections.unmodifiableMap(jsonObj);
    }

}