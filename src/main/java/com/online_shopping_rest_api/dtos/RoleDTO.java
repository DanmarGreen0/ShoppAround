package com.online_shopping_rest_api.dtos;

import com.online_shopping_rest_api.models.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Transfers product data between the controller and service layer of the application.
 */
@Getter
public class RoleDTO{

    private final Integer id;
    private final String role;
    private final List<User> users;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public RoleDTO(Integer id, String role, List<User> users, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.role = role;
        this.users = Collections.unmodifiableList(users) ;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
    public RoleDTO(Integer id, String role) {
        this.id = id;
        this.role = role;
        this.users = new ArrayList<>();
        this.createdAt = null;
        this.modifiedAt = null;
    }
}
