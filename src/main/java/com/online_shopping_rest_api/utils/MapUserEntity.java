package com.online_shopping_rest_api.utils;

import com.online_shopping_rest_api.models.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Component
public class MapUserEntity {
    public Object toJson(List<User> users){

        List<LinkedHashMap<String,Object>> json = new ArrayList<>();

        users.forEach(user -> {

            List<LinkedHashMap<String,String>> json3 = new ArrayList<>();

            user.getRoles().forEach(role -> {
                LinkedHashMap<String,String> json2 = new LinkedHashMap<>();
                json2.put("id" , role.getId().toString());
                json2.put("name" , role.getRole());
                json3.add(json2);
            });

            LinkedHashMap<String,Object> json1 = new LinkedHashMap<>();
            json1.put("id" , user.getId().toString());
            json1.put("username" , user.getUsername());
            json1.put("firstName" , user.getFirstName());
            json1.put("lastName", user.getLastName());
            json1.put("password",user.getPassword());
            json1.put("dateOfBirth" , user.getDateOfBirth());
            json1.put("email" , user.getEmail());
            json1.put("address" , user.getAddress());
            json1.put("phoneNo" , user.getPhoneNo());
            json1.put("createAt" , user.getCreatedAt().toString());
            json1.put("modifiedAt" , user.getModifiedAt().toString());
            json1.put("roles" , json3);

            json.add(json1);

        });
        return json;
    }

    public Object toJson(User user){

        List<LinkedHashMap<String,String>> listJson = new ArrayList<>();

        user.getRoles().forEach(role -> {
            LinkedHashMap<String,String> json2 = new LinkedHashMap<>();

            json2.put("id" , role.getId().toString());
            json2.put("name" , role.getRole());

            listJson.add(json2);
        });

        LinkedHashMap<String,Object> json1 = new LinkedHashMap<>();
        json1.put("id" , user.getId().toString());
        json1.put("username" , user.getUsername());
        json1.put("firstName" , user.getFirstName());
        json1.put("lastName", user.getLastName());
        json1.put("password",user.getPassword());
        json1.put("dateOfBirth" , user.getDateOfBirth());
        json1.put("email" , user.getEmail());
        json1.put("address" , user.getAddress());
        json1.put("phoneNo" , user.getPhoneNo());
        json1.put("createAt" , user.getCreatedAt().toString());
        json1.put("modifiedAt" , user.getModifiedAt().toString());
        json1.put("roles" , listJson);

        return json1;
    }

}
