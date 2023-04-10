package com.online_shopping_rest_api.controller;

import com.online_shopping_rest_api.dtos.DiscountDTO;
import com.online_shopping_rest_api.models.Discount;
import com.online_shopping_rest_api.exceptions.BadRequestException;
import com.online_shopping_rest_api.services.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class DiscountController {
    @Autowired
    DiscountService discountService;

    @PostMapping(path = "/discount", consumes = "application/json")
    ResponseEntity<Object> addDiscount(@RequestBody DiscountDTO discountDTO) {

        DiscountDTO discountJson = entityToDTO(discountService.addDiscount(dtoToEntity(discountDTO)));

        Link selfLink = linkTo(methodOn(DiscountController.class).addDiscount(discountDTO)).slash(discountJson.getId())
                .withSelfRel();
        discountJson.add(Collections.singleton(selfLink));

        return new ResponseEntity<>(discountJson, HttpStatus.CREATED);
    }

    @GetMapping(path = "/discounts")
    ResponseEntity<Object> getDiscounts(@RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "2") Integer pageSize,
            @RequestParam(defaultValue = "name") String[] sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Page<Discount> pageResult = discountService.getDiscounts(pageNo, pageSize, sortBy, sortDirection);

        return new ResponseEntity<>(pageableToDTOList(pageResult), HttpStatus.OK);
    }

    @GetMapping(value = { "/discount", "discount/{id}" })
    public ResponseEntity<Object> getDiscount(@RequestParam Optional<String> name, @PathVariable Optional<Integer> id) {

        DiscountDTO discountDTO = null;

        if (id.get() <= 0) {
            throw new IllegalArgumentException("Invalid argument.");
        } else if (name.isPresent()) {
            discountDTO = addSelfLinkToDiscountDTO(entityToDTO(discountService.getDiscount(name.get())));
        } else if (id.isPresent()) {
            discountDTO = addSelfLinkToDiscountDTO(entityToDTO(discountService.getDiscount(id.get())));
        }

        return new ResponseEntity<>(discountDTO, HttpStatus.OK);
    }

    @PatchMapping(path = "/discount/{id}", produces = "application/json")
    public ResponseEntity<Object> updateDiscount(@RequestBody DiscountDTO discountDTO, @PathVariable int id) {

        if (id < 0)
            throw new BadRequestException("The value of the param id must be equal or greater than zero.");

        DiscountDTO discountJson = entityToDTO(discountService.updateDiscount(dtoToEntity(discountDTO), id));

        Link selfLink = linkTo(methodOn(DiscountController.class).addDiscount(discountDTO)).slash(discountJson.getId())
                .withSelfRel();
        discountJson.add(Collections.singleton(selfLink));

        return new ResponseEntity<>(discountJson, HttpStatus.OK);
    }

    @DeleteMapping(value = { "/discount", "/discount/{id}" })
    public ResponseEntity deleteDiscount(@RequestParam Optional<String> name, @PathVariable Optional<Integer> id) {

        if (id.get() <= 0) {
            throw new IllegalArgumentException("Invalid argument.");
        } else if (name.isPresent()) {
            discountService.deleteDiscount(name.get());
        } else if (id.isPresent()) {
            discountService.deleteDiscount(id.get());
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private static DiscountDTO addSelfLinkToDiscountDTO(DiscountDTO discountDTO) {

        Link selfLink = linkTo(methodOn(DiscountController.class).addDiscount(discountDTO)).slash(discountDTO.getId())
                .withSelfRel();
        discountDTO.add(Collections.singleton(selfLink));

        return discountDTO;
    }

    private DiscountDTO entityToDTO(Discount discount) {
        return new DiscountDTO(discount.getId(), discount.getName(), discount.getDescription(),
                discount.getDiscountPercent(),
                discount.getProducts(), discount.getCreatedAt(), discount.getModifiedAt());
    }

    private Discount dtoToEntity(DiscountDTO discountDTO) {

        return new Discount(discountDTO.getId(), discountDTO.getName(), discountDTO.getDescription(),
                discountDTO.getDiscountPercent(),
                discountDTO.getProducts(), discountDTO.getCreatedAt(), discountDTO.getModifiedAt());
    }

    // add self links to the productDTO collection object and paging details
    private static Object pageableToDTOList(Page<Discount> pageResult) {

        List<DiscountDTO> discountDTOList = new ArrayList<>();

        // add self-link that supports each product item of the product collection
        for (int i = 0; i < pageResult.getContent().size(); i++) {

            discountDTOList.add(new DiscountDTO(pageResult.getContent().get(i).getId(),
                    pageResult.getContent().get(i).getName(), pageResult.getContent().get(i).getDescription(),
                    pageResult.getContent().get(i).getDiscountPercent(), pageResult.getContent().get(i).getProducts(),
                    pageResult.getContent().get(i).getCreatedAt(), pageResult.getContent().get(i).getModifiedAt()));

            Link selfLink = linkTo(methodOn(DiscountController.class).addDiscount(discountDTOList.get(0)))
                    .slash(pageResult.getContent().get(i).getId()).withSelfRel();

            discountDTOList.get(i).add(Collections.singleton(selfLink));
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
        Link discountsLink = linkTo(
                methodOn(DiscountController.class).getDiscounts(pageResult.getPageable().getPageNumber(),
                        pageResult.getPageable().getPageSize(), sortBy, sortDirection[0]))
                .withSelfRel();

        Map<String, Object> jsonObj = new LinkedHashMap<>();
        jsonObj.put("discounts", CollectionModel.of(discountDTOList, discountsLink));

        for (Sort.Order order : pageResult.nextPageable().getSort()) {
            if (pageResult.nextPageable().isPaged())
                jsonObj.put("next_page_url",
                        linkTo(methodOn(DiscountController.class).getDiscounts(
                                pageResult.nextPageable().getPageNumber(), pageResult.nextPageable().getPageSize(),
                                sortBy, sortDirection[0])).withSelfRel());
        }

        for (Sort.Order order : pageResult.previousPageable().getSort()) {
            if (pageResult.previousPageable().isPaged())
                jsonObj.put("previous_page_url",
                        linkTo(methodOn(DiscountController.class).getDiscounts(
                                pageResult.previousPageable().getPageNumber(),
                                pageResult.previousPageable().getPageSize(), sortBy, sortDirection[0])).withSelfRel());
        }

        jsonObj.put("other-info", pageResult.getPageable());

        return Collections.unmodifiableMap(jsonObj);
    }
}
