package com.online_shopping_rest_api.repositories;

import com.online_shopping_rest_api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Use for managing or persisting data (User object) to rational database;
 * It allows a client to perform CRUD operations on a rational database.
 */
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    /**
     * Find user by their username optional.
     *
     * @param username the username
     * @return the optional user entity object
     */
    Optional<User> findByUsername(String username);
    User findByEmail(String email);

    //To do:
    //    User getUser(String name);
    //    Iterable<User> getUsers();
    //    Iterable<User> getUserByYear();
    //    Iterable<User> getUserByMonth();
    //    Iterable<User> getUserByWeek();
    //    Iterable<User> getUserByDay();

}
