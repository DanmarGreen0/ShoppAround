package com.online_shopping_rest_api.controller;

import com.online_shopping_rest_api.dtos.OrderDetailsDTO;
import com.online_shopping_rest_api.models.OrderDetails;
import com.online_shopping_rest_api.exceptions.BadRequestException;
import com.online_shopping_rest_api.services.OrderDetailsService;
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
public class OrderDetailsController {
    @Autowired
    OrderDetailsService orderDetailsService;

    @PostMapping(path = "/orderDetails")
    ResponseEntity<Object> addOrderDetails(@RequestBody OrderDetailsDTO orderDetailsDTO){

        OrderDetailsDTO orderDetailsJson = entityToDTO(orderDetailsService.addOrderDetails(dtoToEntity(orderDetailsDTO)));

        Link selfLink = linkTo(methodOn(OrderDetailsController.class).addOrderDetails(orderDetailsDTO)).slash(orderDetailsJson.getId()).withSelfRel();
        orderDetailsJson.add(Collections.singleton(selfLink));

        return new ResponseEntity<>(orderDetailsJson, HttpStatus.CREATED);
    }

    @GetMapping(path = "/ordersDetails")
    ResponseEntity<Object> getOrdersDetails(@RequestParam(defaultValue = "0") Integer pageNo,
                                        @RequestParam(defaultValue = "2") Integer pageSize,
                                        @RequestParam(defaultValue = "paymentId") String[] sortBy,
                                        @RequestParam (defaultValue = "desc")String sortDirection){

        Page<OrderDetails> pageResult = orderDetailsService.getOrdersDetails(pageNo, pageSize, sortBy, sortDirection);

        return new ResponseEntity<>(pageableToDTOList(pageResult), HttpStatus.OK);
    }

    @GetMapping(value = {"/orderDetails","orderDetails/{id}"})
    public ResponseEntity<Object> getOrderDetails(@RequestParam Optional<String> name, @PathVariable Optional<Integer>  id){

        OrderDetailsDTO orderDetailsDTO = null;

        if(id.get() <= 0 ){
            throw new IllegalArgumentException("Invalid argument.");
        }else if(name.isPresent()){
            orderDetailsDTO = addSelfLinkToOrderDetailsDTO(entityToDTO(orderDetailsService.getOrderDetails(name.get())));
        }else if(id.isPresent() ){
            orderDetailsDTO = addSelfLinkToOrderDetailsDTO(entityToDTO(orderDetailsService.getOrderDetails(id.get())));
        }

        return new ResponseEntity<>(orderDetailsDTO, HttpStatus.OK);
    }

    @PatchMapping(path="/orderDetails/{id}", produces = "application/json")
    public ResponseEntity<Object> updateOrderDetails(@RequestBody OrderDetailsDTO orderDetailsDTO, @PathVariable int id) {

        if(id < 0)
            throw new BadRequestException("The value of the param id must be equal or greater than zero.");

        OrderDetailsDTO OrderDetailsJson = entityToDTO(orderDetailsService.updateOrderDetails(dtoToEntity(orderDetailsDTO) ,id));

        Link selfLink = linkTo(methodOn(OrderDetailsController.class).addOrderDetails(orderDetailsDTO)).slash(OrderDetailsJson.getId()).withSelfRel();
        OrderDetailsJson.add(Collections.singleton(selfLink));

        return new ResponseEntity<>(OrderDetailsJson, HttpStatus.OK);
    }

    @DeleteMapping(value = {"/orderDetails", "/orderDetails/{id}"})
    public ResponseEntity deleteOrderDetails(@RequestParam Optional<Integer> paymentId, @PathVariable Optional<Integer> id){

        if(id.get() != null){
            orderDetailsService.deleteOrderDetailsById(id.get());
        }else if(paymentId.get() != null){
            orderDetailsService.deleteOrderDetailsByPaymentId(paymentId.get());
        }else if(id.get() <= 0 && paymentId.get() <= 0){
            throw new IllegalArgumentException("Invalid argument.");
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private static OrderDetailsDTO addSelfLinkToOrderDetailsDTO(OrderDetailsDTO orderDetailsDTO){

        Link selfLink = linkTo(methodOn(OrderDetailsController.class).addOrderDetails(orderDetailsDTO)).slash(orderDetailsDTO.getId()).withSelfRel();
        orderDetailsDTO.add(Collections.singleton(selfLink));

        return orderDetailsDTO;
    }

    private OrderDetailsDTO entityToDTO(OrderDetails orderDetails){
        return new OrderDetailsDTO(orderDetails.getId(), orderDetails.getTotal(), orderDetails.getPaymentId(),
                orderDetails.getCreatedAt(), orderDetails.getModifiedAt());
    }
    private OrderDetails dtoToEntity(OrderDetailsDTO orderDetailsDTO){

        return new OrderDetails(orderDetailsDTO.getId(), orderDetailsDTO.getTotal(), orderDetailsDTO.getPaymentId(),
                orderDetailsDTO.getCreatedAt(), orderDetailsDTO.getModifiedAt());
    }

    //add self links to the productDTO collection object and paging details
    private static Object pageableToDTOList(Page<OrderDetails> pageResult){

        List<OrderDetailsDTO> OrderDetailsDTOList = new ArrayList<>();

        //add self-link that supports each product item of the product collection
        for(int i = 0; i < pageResult.getContent().size(); i++){

            OrderDetailsDTOList.add(new OrderDetailsDTO(pageResult.getContent().get(i).getId(),pageResult.getContent().get(i).getTotal(), pageResult.getContent().get(i).getPaymentId(),
                    pageResult.getContent().get(i).getCreatedAt(),pageResult.getContent().get(i).getModifiedAt()));

            Link selfLink = linkTo(methodOn(OrderDetailsController.class).addOrderDetails(OrderDetailsDTOList.get(0))).slash(pageResult.getContent().get(i).getId()).withSelfRel();

            OrderDetailsDTOList.get(i).add(Collections.singleton(selfLink));
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
        Link OrderDetailsLink = linkTo(methodOn(OrderDetailsController.class).getOrdersDetails(pageResult.getPageable().getPageNumber(), pageResult.getPageable().getPageSize(), sortBy, sortDirection[0])).withSelfRel();

        Map<String, Object> jsonObj = new LinkedHashMap<>();
        jsonObj.put("data", CollectionModel.of(OrderDetailsDTOList,OrderDetailsLink));

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
