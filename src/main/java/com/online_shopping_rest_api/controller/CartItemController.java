package com.online_shopping_rest_api.controller;

import com.online_shopping_rest_api.dtos.CartItemDTO;
import com.online_shopping_rest_api.models.CartItem;
import com.online_shopping_rest_api.exceptions.BadRequestException;
import com.online_shopping_rest_api.services.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class CartItemController {

    @Autowired
    CartItemService cartItemService;

    @PostMapping(path = "/cartItem")
    ResponseEntity<Object> addCartItem(@RequestBody CartItemDTO cartItemDTO){

        CartItemDTO cartItemJson = entityToDTO(cartItemService.addCartItem(dtoToEntity(cartItemDTO)));

        Link selfLink = linkTo(methodOn(CartItemController.class).addCartItem(cartItemDTO)).slash(cartItemJson.getId()).withSelfRel();
        cartItemJson.add(Collections.singleton(selfLink));

        return new ResponseEntity<>(cartItemJson, HttpStatus.CREATED);
    }

    @GetMapping(path = "/cartItems")
    ResponseEntity<Object> getCartItems(@RequestParam(defaultValue = "0") Integer pageNo,
                                        @RequestParam(defaultValue = "2") Integer pageSize,
                                        @RequestParam(defaultValue = "sessionId") String[] sortBy,
                                        @RequestParam (defaultValue = "desc")String sortDirection){

        Page<CartItem> pageResult = cartItemService.getCartItems(pageNo, pageSize, sortBy, sortDirection);

        return new ResponseEntity<>(pageableToDTOList(pageResult), HttpStatus.OK);
    }

    @GetMapping(value = {"/cartItem","cartItem/{id}"})
    public ResponseEntity<Object> getCartItem(@RequestParam Optional<Integer> sessionId, @PathVariable Optional<Integer>  id){

        CartItemDTO cartItemDTO = null;

        if(id.get() <= 0 ){
            throw new IllegalArgumentException("Invalid argument.");
        }else if(sessionId.isPresent()){
            cartItemDTO = addSelfLinkToCartItemDTO(entityToDTO(cartItemService.getCartItem(sessionId.get())));
        }else if(id.isPresent() ){
            cartItemDTO = addSelfLinkToCartItemDTO(entityToDTO(cartItemService.getCartItem(id.get())));
        }

        return new ResponseEntity<>(cartItemDTO, HttpStatus.OK);
    }

    @PatchMapping(path="/cartItem/{id}", produces = "application/json")
    public ResponseEntity<Object> updateCartItem(@RequestBody CartItemDTO cartItemDTO, @PathVariable int id) {

        if(id < 0)
            throw new BadRequestException("The value of the param id must be equal or greater than zero.");

        CartItemDTO cartItemJson = entityToDTO(cartItemService.updateCartItem(dtoToEntity(cartItemDTO) ,id));

        Link selfLink = linkTo(methodOn(CartItemController.class).addCartItem(cartItemDTO)).slash(cartItemDTO.getId()).withSelfRel();
        cartItemJson.add(Collections.singleton(selfLink));

        return new ResponseEntity<>(cartItemJson, HttpStatus.OK);
    }

    @DeleteMapping(value = {"/cartItem", "/cartItem/{id}"})
    public ResponseEntity deleteCartItem(@RequestParam Optional<Integer> sessionId, @PathVariable Optional<Integer> id){

        if(id.get() != null){
            cartItemService.deleteCartItemById(id.get());
        }else if(sessionId.get() != null){
            cartItemService.deleteCartItemBySessionId(sessionId.get());
        }else if(id.get() <= 0 && sessionId.get() <= 0){
            throw new IllegalArgumentException("Invalid argument.");
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private static CartItemDTO addSelfLinkToCartItemDTO(CartItemDTO cartItemDTO){

        Link selfLink = linkTo(methodOn(CartItemController.class).addCartItem(cartItemDTO)).slash(cartItemDTO.getId()).withSelfRel();
        cartItemDTO.add(Collections.singleton(selfLink));

        return cartItemDTO;
    }

    private CartItemDTO entityToDTO(CartItem cartItem){
        return new CartItemDTO(cartItem.getId(), cartItem.getSessionId(), cartItem.getQuantity(), cartItem.getCreatedAt(),
                cartItem.getModifiedAt());
    }

    private CartItem dtoToEntity(CartItemDTO cartItemDTO){

        return new CartItem(cartItemDTO.getId(), cartItemDTO.getSessionId(), cartItemDTO.getQuantity(), cartItemDTO.getCreatedAt(),
                cartItemDTO.getModifiedAt());
    }

    //add self links to the productDTO collection object and paging details
    private static Object pageableToDTOList(Page<CartItem> pageResult){

        List<CartItemDTO> cartItemDTOList = new ArrayList<>();

        //add self-link that supports each product item of the product collection
        for(int i = 0; i < pageResult.getContent().size(); i++){

            cartItemDTOList.add(new CartItemDTO(pageResult.getContent().get(i).getId(),pageResult.getContent().get(i).getSessionId(), pageResult.getContent().get(i).getQuantity(), pageResult.getContent().get(i).getCreatedAt(),
                    pageResult.getContent().get(i).getModifiedAt()));

            Link selfLink = linkTo(methodOn(CartItemController.class).addCartItem(cartItemDTOList.get(0))).slash(pageResult.getContent().get(i).getId()).withSelfRel();

            cartItemDTOList.get(i).add(Collections.singleton(selfLink));
        }

        //get sortBy and orderBy property requested page result.
        List<Sort.Order> orders = pageResult.getSort().stream().collect(Collectors.toList());

        String[] sortBy = new String[orders.size()];
        String[] sortDirection = new String[orders.size()];

        for(int i = 0; i < orders.size(); i++){
            sortBy[i] = orders.get(i).getProperty();
            sortDirection[i] = orders.get(i).getDirection().toString();
        }

        //add outer self-link that supports retrieving the product collection
        Link cartItemLink = linkTo(methodOn(CartItemController.class).getCartItems(pageResult.getPageable().getPageNumber(), pageResult.getPageable().getPageSize(), sortBy, sortDirection[0])).withSelfRel();

        Map<String, Object> jsonObj = new LinkedHashMap<>();
        jsonObj.put("data", CollectionModel.of(cartItemDTOList,cartItemLink));

        for(Sort.Order order: pageResult.nextPageable().getSort()) {
            if (pageResult.nextPageable().isPaged())
                jsonObj.put("next_page_url", linkTo(methodOn(CartItemController.class).getCartItems(pageResult.nextPageable().getPageNumber(), pageResult.nextPageable().getPageSize(), sortBy, sortDirection[0])).withSelfRel());
        }

        for(Sort.Order order: pageResult.previousPageable().getSort()){
            if(pageResult.previousPageable().isPaged())
                jsonObj.put("previous_page_url", linkTo(methodOn(CartItemController.class).getCartItems(pageResult.previousPageable().getPageNumber(), pageResult.previousPageable().getPageSize(), sortBy, sortDirection[0])).withSelfRel());
        }

        jsonObj.put("other-info", pageResult.getPageable());

        return Collections.unmodifiableMap(jsonObj);
    }
}

