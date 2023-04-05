package com.online_shopping_rest_api.services;

import com.online_shopping_rest_api.models.Role;
import com.online_shopping_rest_api.exceptions.IllegalArgumentException;
import com.online_shopping_rest_api.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Business logic for role
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleRepository roleRepository;


    /**
     * Retrieve a role by id from the database
     *
     * @param id primary key
     * @return Role object
     * @throws Exception
     */
    @Override
    public Role getRole(int id){
        return this.roleRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("The is no role found with id = " + id));
    }


    /**
     * Retrieve all the role entries store in the Role table within the database
     *
     * @return a list of all the role entry found in the database.
     * Return empty list if there are no entries.
     */
    @Override
    public List<Role> getRoles() {
        return this.roleRepository.findAll();
    }
}
