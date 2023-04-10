package com.online_shopping_rest_api.dtos;

import com.online_shopping_rest_api.configs.PasswordEncoder;
import com.online_shopping_rest_api.exceptions.IllegalArgumentException;
import com.online_shopping_rest_api.models.Role;
import com.online_shopping_rest_api.models.User;
import com.online_shopping_rest_api.utils.InputValidator;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.hateoas.server.mvc.BasicLinkBuilder;
import org.springframework.hateoas.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Transfers product data between the controller and service layer of the
 * application.
 *
 *
 * @throws BadRequestException if all the param equals this
 * @return immutable record object.
 */
@Getter
@NoArgsConstructor(force = true)
// @Relation(collectionRelation = "usersss", itemRelation = "user")
// @JsonInclude(Include.NON_NULL)
public final class UserDTO extends RepresentationModel<UserDTO> {

    private final Integer id;
    private final String username, firstName, lastName, password, address, dateOfBirth, email, phoneNo;
    private final List<RoleDTO> roles;
    private final LocalDateTime createdAt, modifiedAt;

    public static class Builder {
        private Integer id = 0;
        private List<RoleDTO> roles;
        private LocalDateTime createdAt, modifiedAt;
        private String username, firstName, lastName, address, dateOfBirth, email, phoneNo, password;

        /**
         * Returns a builder object to the outer class.
         */
        public UserDTO.Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public UserDTO.Builder username(String username) {
            if (username == null || !InputValidator.checkName(username)) {
                throw new IllegalArgumentException("Invalid username. Enter username.");
            }
            this.username = username;
            return this;
        }

        public UserDTO.Builder firstName(String firstName) {
            if (firstName == null || !InputValidator.checkName(firstName)) {
                throw new IllegalArgumentException("Invalid first name. Enter first name.");
            }
            this.firstName = firstName;
            return this;
        }

        public UserDTO.Builder lastName(String lastName) {
            if (lastName == null || !InputValidator.checkName(lastName)) {
                throw new IllegalArgumentException("Invalid last name. Enter last name.");
            }
            this.lastName = lastName;
            return this;
        }

        public UserDTO.Builder address(String address) {
            this.address = address;
            return this;
        }

        public UserDTO.Builder dateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        };

        public UserDTO.Builder email(String email) {
            if (email == null || !InputValidator.checkEmail(email)) {
                throw new IllegalArgumentException("Invalid email. Enter email.");
            }
            this.email = email;
            return this;
        };

        public UserDTO.Builder phoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
            return this;
        };

        public UserDTO.Builder password(String password) {
            if (password == null) {
                throw new IllegalArgumentException("Invalid password. Enter password.");
            }
            this.password = password;
            return this;
        }

        public UserDTO.Builder roles(List<Role> roles) {

            List<RoleDTO> roleDTOList = new ArrayList<>();

            for (Role role : roles) {
                roleDTOList.add(new RoleDTO(role.getId(), role.getRole()));
            }

            this.roles = Collections.unmodifiableList(roleDTOList);
            return this;
        }

        public UserDTO.Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public UserDTO.Builder modifiedAt(LocalDateTime modifiedAt) {
            this.modifiedAt = modifiedAt;
            return this;
        }

        public UserDTO build() {
            return new UserDTO(this);
        }

    }

    /**
     * Return a partial copy of the builder object's state.
     * <p>
     * This constructor creates a partial copy of the builder object's state.
     * It is a partial copy because the value of the password field of the builder
     * object got encrypted before being stored in the instance variable password of
     * the User class {@link PasswordEncoder#getEncodedPassword}.
     *
     * @param builder a builder object which store state of the class
     *                {@link User.Builder}
     */
    private UserDTO(UserDTO.Builder builder) {

        String baseUri = BasicLinkBuilder.linkToCurrentMapping().toString();
        String selfLink = baseUri + "/user/" + builder.id;

        id = builder.id;
        username = builder.username;
        firstName = builder.firstName;
        lastName = builder.lastName;
        password = builder.password;
        address = builder.address;
        dateOfBirth = builder.dateOfBirth;
        email = builder.email;
        phoneNo = builder.phoneNo;
        roles = builder.roles;
        createdAt = builder.createdAt;
        modifiedAt = builder.modifiedAt;

        // construct selfLink var
        add(Link.of(selfLink));
    }

}
