package com.online_shopping_rest_api.configs;

import com.online_shopping_rest_api.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class implements the UserDetails interface and overrides its methods.
 * The UserDetails is responsible
 * for storing user-related data in an Authentication object, which can be
 * accessed using the web controller methods.
 */
public class ApplicationUserPrincipal implements UserDetails {
    private final User user;

    /**
     * Instantiates a new Application user principal.
     *
     * @param user contains the current user's account information upon success
     *             of the
     *             {@link ApplicationUserDetailsService#loadUserByUsername(String username)}
     *             method
     */
    public ApplicationUserPrincipal(User user) {
        this.user = user;
    }

    /**
     * get the user granted authority (user's roles) from the user object
     *
     * @return a set of granted authority of the user upon success
     *         of the
     *         {@link ApplicationUserDetailsService#loadUserByUsername(String username)}
     *         method.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user == null) {
            return Collections.emptySet();
        }
        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
        });
        return grantedAuthorities;
    }

    public Integer getId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
