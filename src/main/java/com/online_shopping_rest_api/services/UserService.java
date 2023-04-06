package com.online_shopping_rest_api.services;

import com.online_shopping_rest_api.dtos.UserDTO;
import com.online_shopping_rest_api.models.User;

import java.util.*;

import org.springframework.security.core.Authentication;

/**
 * These interface methods are responsible for implementing the business
 * logic on the User entity stored in a rational database.The class
 * UserServiceImpl implements the interface methods.
 * 
 * @see UserServiceImpl
 */
public interface UserService {

    User getUserAccountById(int id);

    UserDTO getUserAccountByUsername(String Username);

    Map<String, Object> getUsersAccounts(Integer pageNo, Integer pageSize, String[] sortBy, String sortDirection);

    User createAdminUser(UserDTO userDTO);

    User updateUser(Authentication authentication, UserDTO userDTO, int id);

    String deleteUser(Authentication authentication, int id);

    // To do:
    /*
     * Iterable<User> getUserByYear();
     * Iterable<User> getUserByMonth();
     * Iterable<User> getUserByWeek();
     * Iterable<User> getUserByDay();
     */

}
