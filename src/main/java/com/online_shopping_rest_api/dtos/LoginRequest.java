package com.online_shopping_rest_api.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {

    private String password;
    private String username;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
