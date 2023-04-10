package com.online_shopping_rest_api.configs;

import com.online_shopping_rest_api.models.User;
import com.online_shopping_rest_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * The type Application user details service.
 */
@Service
public class ApplicationUserDetailsService implements UserDetailsService {
    @Autowired
    private final UserRepository userRepository;

    /**
     * The Logger.
     */
    static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Instantiates a new Application user details service.
     *
     * @param userRepository      the user repository
     * @param authGroupRepository the auth group repository
     */
    public ApplicationUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * find the login user by username
     *
     * @throws UsernameNotFoundException if the user's username isn't found in the
     *                                   database
     * @return return new ApplicationUserPrinciple object. Its constructor is being
     *         initialized using the returned user object
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final Optional<User> user = this.userRepository.findByUsername(username);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User with the username " + username + " does not exist.");
        }

        return new ApplicationUserPrincipal(user.get());
    }
}
