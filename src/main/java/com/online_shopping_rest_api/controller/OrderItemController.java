package com.online_shopping_rest_api.controller;

import com.online_shopping_rest_api.dtos.OrderItemDTO;
import com.online_shopping_rest_api.models.OrderItem;
import com.online_shopping_rest_api.exceptions.BadRequestException;
import com.online_shopping_rest_api.services.OrderItemService;
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
public class OrderItemController {

    @Autowired
    OrderItemService orderItemService;

    @PostMapping(path = "/orderItem")
    ResponseEntity<Object> addOrderItem(@RequestBody OrderItemDTO orderItemDTO){

        OrderItemDTO orderItemJson = entityToDTO(orderItemService.addOrderItem(dtoToEntity(orderItemDTO)));

        Link selfLink = linkTo(methodOn(OrderItemController.class).addOrderItem(orderItemDTO)).slash(orderItemJson.getId()).withSelfRel();
        orderItemJson.add(Collections.singleton(selfLink));

        return new ResponseEntity<>(orderItemJson, HttpStatus.CREATED);
    }

    @GetMapping(path = "/orderItems")
    ResponseEntity<Object> getOrderItems(@RequestParam(defaultValue = "0") Integer pageNo,
                                            @RequestParam(defaultValue = "2") Integer pageSize,
                                            @RequestParam(defaultValue = "productId") String[] sortBy,
                                            @RequestParam (defaultValue = "desc")String sortDirection){

        Page<OrderItem> pageResult = orderItemService.getOrderItems(pageNo, pageSize, sortBy, sortDirection);

        return new ResponseEntity<>(pageableToDTOList(pageResult), HttpStatus.OK);
    }

    @GetMapping(value = {"/orderItem","orderItem/{id}"})
    public ResponseEntity<Object> getOrderItem(@RequestParam Optional<String> name, @PathVariable Optional<Integer>  id){

        OrderItemDTO orderItemDTO = null;

        if(id.get() <= 0 ){
            throw new IllegalArgumentException("Invalid argument.");
        }else if(name.isPresent()){
            orderItemDTO = addSelfLinkToOrderItemDTO(entityToDTO(orderItemService.getOrderItem(name.get())));
        }else if(id.isPresent() ){
            orderItemDTO = addSelfLinkToOrderItemDTO(entityToDTO(orderItemService.getOrderItem(id.get())));
        }

        return new ResponseEntity<>(orderItemDTO, HttpStatus.OK);
    }

    @PatchMapping(path="/orderItem/{id}", produces = "application/json")
    public ResponseEntity<Object> updateOrderItems(@RequestBody OrderItemDTO orderItemDTO, @PathVariable int id) {

        if(id < 0)
            throw new BadRequestException("The value of the param id must be equal or greater than zero.");

        OrderItemDTO OrderItemJson = entityToDTO(orderItemService.updateOrderItem(dtoToEntity(orderItemDTO) ,id));

        Link selfLink = linkTo(methodOn(OrderItemController.class).addOrderItem(orderItemDTO)).slash(OrderItemJson.getId()).withSelfRel();
        OrderItemJson.add(Collections.singleton(selfLink));

        return new ResponseEntity<>(OrderItemJson, HttpStatus.OK);
    }

    @DeleteMapping(value = {"/orderItem", "/orderItem/{id}"})
    public ResponseEntity deleteOrderItem(@RequestParam Optional<Integer> productId, @PathVariable Optional<Integer> id){

        if(id.get() != null){
            orderItemService.deleteOrderItemById(id.get());
        }else if(productId.get() != null){
            orderItemService.deleteOrderItemByPaymentId(productId.get());
        }else if(id.get() <= 0 && productId.get() <= 0){
            throw new IllegalArgumentException("Invalid argument.");
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private static OrderItemDTO addSelfLinkToOrderItemDTO(OrderItemDTO orderItemDTO){

        Link selfLink = linkTo(methodOn(OrderItemController.class).addOrderItem(orderItemDTO)).slash(orderItemDTO.getId()).withSelfRel();
        orderItemDTO.add(Collections.singleton(selfLink));

        return orderItemDTO;
    }

    private OrderItemDTO entityToDTO(OrderItem orderItem){
        return new OrderItemDTO(orderItem.getId(), orderItem.getProductId(),
                orderItem.getCreatedAt(), orderItem.getModifiedAt());
    }
    private OrderItem dtoToEntity(OrderItemDTO orderItemDTO){

        return new OrderItem(orderItemDTO.getId(), orderItemDTO.getProductId(),
                orderItemDTO.getCreatedAt(), orderItemDTO.getModifiedAt());
    }

    //add self links to the productDTO collection object and paging details
    private static Object pageableToDTOList(Page<OrderItem> pageResult){

        List<OrderItemDTO> OrderItemDTOList = new ArrayList<>();

        //add self-link that supports each product item of the product collection
        for(int i = 0; i < pageResult.getContent().size(); i++){

            OrderItemDTOList.add(new OrderItemDTO(pageResult.getContent().get(i).getId(), pageResult.getContent().get(i).getProductId(),
                    pageResult.getContent().get(i).getCreatedAt(),pageResult.getContent().get(i).getModifiedAt()));

            Link selfLink = linkTo(methodOn(OrderItemController.class).addOrderItem(OrderItemDTOList.get(0))).slash(pageResult.getContent().get(i).getId()).withSelfRel();

            OrderItemDTOList.get(i).add(Collections.singleton(selfLink));
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
        Link orderItemLink = linkTo(methodOn(OrderDetailsController.class).getOrdersDetails(pageResult.getPageable().getPageNumber(), pageResult.getPageable().getPageSize(), sortBy, sortDirection[0])).withSelfRel();

        Map<String, Object> jsonObj = new LinkedHashMap<>();
        jsonObj.put("data", CollectionModel.of(OrderItemDTOList,orderItemLink));

        for(Sort.Order order: pageResult.nextPageable().getSort()) {
            if (pageResult.nextPageable().isPaged())
                jsonObj.put("next_page_url", linkTo(methodOn(OrderDetailsController.class).getOrdersDetails(pageResult.nextPageable().getPageNumber(), pageResult.nextPageable().getPageSize(), sortBy, sortDirection[0])).withSelfRel());
        }

        for(Sort.Order order: pageResult.previousPageable().getSort()){
            if(pageResult.previousPageable().isPaged())
                jsonObj.put("previous_page_url", linkTo(methodOn(OrderDetailsController.class).getOrdersDetails(pageResult.previousPageable().getPageNumber(), pageResult.previousPageable().getPageSize(), sortBy, sortDirection[0])).withSelfRel());
        }

        jsonObj.put("other-info", pageResult.getPageable());

        return Collections.unmodifiableMap(jsonObj);
    }
}
