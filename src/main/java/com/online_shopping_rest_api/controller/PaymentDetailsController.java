package com.online_shopping_rest_api.controller;

import com.online_shopping_rest_api.dtos.PaymentDetailsDTO;
import com.online_shopping_rest_api.models.PaymentDetails;
import com.online_shopping_rest_api.exceptions.BadRequestException;
import com.online_shopping_rest_api.services.PaymentDetailsService;
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
public class PaymentDetailsController {

    @Autowired
    PaymentDetailsService paymentDetailsService;

    @PostMapping(path = "/paymentDetails")
    ResponseEntity<Object> addPaymentDetails(@RequestBody PaymentDetailsDTO paymentDetailsDTO){

        PaymentDetailsDTO paymentDetailsJson = entityToDTO(paymentDetailsService.addPaymentDetails(dtoToEntity(paymentDetailsDTO)));

        Link selfLink = linkTo(methodOn(PaymentDetailsController.class).addPaymentDetails(paymentDetailsDTO)).slash(paymentDetailsJson.getId()).withSelfRel();
        paymentDetailsJson.add(Collections.singleton(selfLink));

        return new ResponseEntity<>(paymentDetailsJson, HttpStatus.CREATED);
    }

    @GetMapping(path = "/paymentsDetails")
    ResponseEntity<Object> getPaymentDetails(@RequestParam(defaultValue = "0") Integer pageNo,
                                            @RequestParam(defaultValue = "2") Integer pageSize,
                                            @RequestParam(defaultValue = "paymentId") String[] sortBy,
                                            @RequestParam (defaultValue = "desc")String sortDirection){

        Page<PaymentDetails> pageResult = paymentDetailsService.getPaymentDetails(pageNo, pageSize, sortBy, sortDirection);

        return new ResponseEntity<>(pageableToDTOList(pageResult), HttpStatus.OK);
    }

    @GetMapping(value = {"/paymentDetails","paymentDetails/{id}"})
    public ResponseEntity<Object> getPaymentDetails(@RequestParam Optional<String> orderId, @PathVariable Optional<Integer>  id){

        PaymentDetailsDTO paymentDetailsDTO = null;

        if(id.get() <= 0 ){
            throw new IllegalArgumentException("Invalid argument.");
        }else if(orderId.isPresent()){
            paymentDetailsDTO = addSelfLinkToPaymentDetailsDTO(entityToDTO(paymentDetailsService.getPaymentDetails(orderId.get())));
        }else if(id.isPresent() ){
            paymentDetailsDTO = addSelfLinkToPaymentDetailsDTO(entityToDTO(paymentDetailsService.getPaymentDetails(id.get())));
        }

        return new ResponseEntity<>(paymentDetailsDTO, HttpStatus.OK);
    }

    @PatchMapping(path="/paymentDetails/{id}", produces = "application/json")
    public ResponseEntity<Object> updateOrderDetails(@RequestBody PaymentDetailsDTO paymentDetailsDTO, @PathVariable int id) {

        if(id < 0)
            throw new BadRequestException("The value of the param id must be equal or greater than zero.");

        PaymentDetailsDTO paymentDetailsJson = entityToDTO(paymentDetailsService.updatePaymentDetails(dtoToEntity(paymentDetailsDTO) ,id));

        Link selfLink = linkTo(methodOn(PaymentDetailsController.class).addPaymentDetails(paymentDetailsDTO)).slash(paymentDetailsJson.getId()).withSelfRel();
        paymentDetailsJson.add(Collections.singleton(selfLink));

        return new ResponseEntity<>(paymentDetailsJson, HttpStatus.OK);
    }

    @DeleteMapping(value = {"/paymentDetails", "/paymentDetails/{id}"})
    public ResponseEntity deletePaymentDetails(@RequestParam Optional<Integer> orderId, @PathVariable Optional<Integer> id){

        if(id.get() != null){
            paymentDetailsService.deletePaymentDetailsById(id.get());
        }else if(orderId.get() != null){
            paymentDetailsService.deletePaymentDetailsByPaymentId(orderId.get());
        }else if(id.get() <= 0 && orderId.get() <= 0){
            throw new IllegalArgumentException("Invalid argument.");
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private static PaymentDetailsDTO addSelfLinkToPaymentDetailsDTO(PaymentDetailsDTO paymentDetailsDTO){

        Link selfLink = linkTo(methodOn(PaymentDetailsController.class).addPaymentDetails(paymentDetailsDTO)).slash(paymentDetailsDTO.getId()).withSelfRel();
        paymentDetailsDTO.add(Collections.singleton(selfLink));

        return paymentDetailsDTO;
    }

    private PaymentDetailsDTO entityToDTO(PaymentDetails paymentDetails){
        return new PaymentDetailsDTO(paymentDetails.getId(), paymentDetails.getOrderId(), paymentDetails.getAmount(),
                paymentDetails.getProvider(), paymentDetails.getStatus(), paymentDetails.getCreatedAt(), paymentDetails.getModifiedAt());
    }
    private PaymentDetails dtoToEntity(PaymentDetailsDTO paymentDetailsDTO){

        return new PaymentDetails(paymentDetailsDTO.getId(), paymentDetailsDTO.getOrderId(), paymentDetailsDTO.getAmount(), paymentDetailsDTO.getProvider(),
                paymentDetailsDTO.getStatus(), paymentDetailsDTO.getCreatedAt(), paymentDetailsDTO.getModifiedAt());
    }

    //add self links to the productDTO collection object and paging details
    private static Object pageableToDTOList(Page<PaymentDetails> pageResult){

        List<PaymentDetailsDTO> paymentDetailsDTOList = new ArrayList<>();

        //add self-link that supports each product item of the product collection
        for(int i = 0; i < pageResult.getContent().size(); i++){

            paymentDetailsDTOList.add(new PaymentDetailsDTO(pageResult.getContent().get(i).getId(),pageResult.getContent().get(i).getOrderId(),pageResult.getContent().get(i).getAmount(), pageResult.getContent().get(i).getProvider(),
                    pageResult.getContent().get(i).getStatus(), pageResult.getContent().get(i).getCreatedAt(),pageResult.getContent().get(i).getModifiedAt()));

            Link selfLink = linkTo(methodOn(PaymentDetailsController.class).addPaymentDetails(paymentDetailsDTOList.get(0))).slash(pageResult.getContent().get(i).getId()).withSelfRel();

            paymentDetailsDTOList.get(i).add(Collections.singleton(selfLink));
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
        Link OrderDetailsLink = linkTo(methodOn(PaymentDetailsController.class).getPaymentDetails(pageResult.getPageable().getPageNumber(), pageResult.getPageable().getPageSize(), sortBy, sortDirection[0])).withSelfRel();

        Map<String, Object> jsonObj = new LinkedHashMap<>();
        jsonObj.put("data", CollectionModel.of(paymentDetailsDTOList,OrderDetailsLink));

        for(Sort.Order order: pageResult.nextPageable().getSort()) {
            if (pageResult.nextPageable().isPaged())
                jsonObj.put("next_page_url", linkTo(methodOn(PaymentDetailsController.class).getPaymentDetails(pageResult.nextPageable().getPageNumber(), pageResult.nextPageable().getPageSize(), sortBy, sortDirection[0])).withSelfRel());
        }

        for(Sort.Order order: pageResult.previousPageable().getSort()){
            if(pageResult.previousPageable().isPaged())
                jsonObj.put("previous_page_url", linkTo(methodOn(PaymentDetailsController.class).getPaymentDetails(pageResult.previousPageable().getPageNumber(), pageResult.previousPageable().getPageSize(), sortBy, sortDirection[0])).withSelfRel());
        }

        jsonObj.put("other-info", pageResult.getPageable());

        return Collections.unmodifiableMap(jsonObj);
    }
}
