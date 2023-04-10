package com.online_shopping_rest_api.utils;

import com.online_shopping_rest_api.models.Role;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class MapRoleEntity {
    public Object toJson(List<Role> roles){

        List<Map<String,Object>> json = new ArrayList<>();

        roles.forEach(role -> {

            LinkedHashMap<String,Object> json1 = new LinkedHashMap<>();
            List<Map<String,String>> json3 = new ArrayList<>();

            json1.put("id" , role.getId().toString());
            json1.put("name" , role.getRole());

            role.getUsers().forEach(user -> {
                LinkedHashMap<String,String> json2 = new LinkedHashMap<>();
                json2.put("id" , user.getId().toString());
                json2.put("username" , user.getUsername());
                json2.put("firstName" , user.getFirstName());
                json2.put("lastName", user.getLastName());
                json2.put("password",user.getPassword());
                json2.put("dateOfBirth" , user.getDateOfBirth());
                json2.put("email" , user.getEmail());
                json2.put("address" , user.getAddress());
                json2.put("phoneNo" , user.getPhoneNo());
                json2.put("createAt" , user.getCreatedAt().toString());
                json2.put("modifiedAt" , user.getModifiedAt().toString());
                json3.add(json2);
            });

            json1.put("users" , json3);

            json.add(json1);
       });

        return json;
    }

    public  Object toJson(Role role){

        LinkedHashMap<String,Object> json = new LinkedHashMap<>();
        LinkedHashMap<String,String> json2 = new LinkedHashMap<>();

        json.put("id" , role.getId().toString());
        json.put("role" , role.getRole());

        role.getUsers().forEach(user -> {
            json2.put("id" , user.getId().toString());
            json2.put("username" , user.getUsername());
            json2.put("firstName" , user.getFirstName());
            json2.put("lastName", user.getLastName());
            json2.put("password", user.getPassword());
            json2.put("dateOfBirth" , user.getDateOfBirth());
            json2.put("email" , user.getEmail());
            json2.put("address" , user.getAddress());
            json2.put("phoneNo" , user.getPhoneNo());
            json2.put("createAt" , user.getCreatedAt().toString());
            json2.put("modifiedAt" , user.getModifiedAt().toString());
        });

        json.put("users" , json2);

        return json;
    }

}
