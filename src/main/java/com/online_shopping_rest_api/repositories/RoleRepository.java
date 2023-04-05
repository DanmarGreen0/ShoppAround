package com.online_shopping_rest_api.repositories;

import com.online_shopping_rest_api.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Use for managing or persisting data (Role object) to rational database;
 * It allows a client to perform CRUD operations on a rational database.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    /**
     * Find by role optional.
     *
     * @param role the role
     * @return the optional
     */
    Optional<Role> findByRole(String role);
}
