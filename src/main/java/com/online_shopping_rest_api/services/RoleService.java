package com.online_shopping_rest_api.services;

import com.online_shopping_rest_api.models.Role;

import java.util.List;

/**
 * These interface methods are responsible for implementing the business
 * logic on the Role entity stored in a rational database.The class
 * RoleServiceImpl implements the interface methods.
 * @see RoleServiceImpl
 */
public interface RoleService {
    Role getRole(int id) throws Exception;
    List<Role> getRoles();
}
