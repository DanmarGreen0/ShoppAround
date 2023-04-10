package com.online_shopping_rest_api.utils;

import com.online_shopping_rest_api.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

//checks if a user has the authorization to complete a database operation
public class OperationAuthorizationChecker {

    private final User user;
    private final Authentication authentication;

    public OperationAuthorizationChecker(User user, Authentication authentication) {
        this.user = user;
        this.authentication = authentication;
    }

    public boolean check(){

       final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
       final String loginUserUsername = userDetails.getUsername();
       final String usernameOnRecord = user.getUsername();
       boolean is_authorized = false;

        if(usernameOnRecord.equals(loginUserUsername)){
           is_authorized = true;
        }

        return is_authorized;
    }
}
