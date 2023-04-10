package com.online_shopping_rest_api.controller;

import com.online_shopping_rest_api.exceptions.BadRequestException;
import com.online_shopping_rest_api.services.RoleServiceImpl;
import com.online_shopping_rest_api.utils.MapRoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private MapRoleEntity mapRoleEntity;

    @GetMapping(path="/roles", produces = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_MASTER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<Object> getRoles(){

        Object json = mapRoleEntity.toJson(roleService.getRoles());

        

        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @GetMapping(path="/role/{id}", produces = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_MASTER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<Object> getRole(@PathVariable int id) throws Exception{

        if(id < 0)
            throw new BadRequestException("The value of the param id must be equal to or greater than zero.");

        Object json = mapRoleEntity.toJson(roleService.getRole(id));

        return new ResponseEntity<>(json, HttpStatus.OK);
    }



}
