package com.online_shopping_rest_api.services;

import com.online_shopping_rest_api.dtos.UserDTO;
import com.online_shopping_rest_api.exceptions.IllegalArgumentException;
import com.online_shopping_rest_api.exceptions.ResourceNotFoundException;
import com.online_shopping_rest_api.models.Role;
import com.online_shopping_rest_api.models.User;
import com.online_shopping_rest_api.repositories.RoleRepository;
import com.online_shopping_rest_api.repositories.UserRepository;
import com.online_shopping_rest_api.utils.DateGenerator;
import com.online_shopping_rest_api.utils.OperationAuthorizationChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.hateoas.server.mvc.BasicLinkBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Business logic for user
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

        @Autowired
        UserRepository userRepository;
        @Autowired
        RoleRepository roleRepository;

        /**
         * get a user entity by id
         *
         * @param id primary key
         * @throws IllegalArgumentException if an entity with the id wasn't found in the
         *                                  database
         * @return return the user entity with the id
         */
        @Override
        public User getUserAccountById(int id) {

                return this.userRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Could not be found a user account +"
                                                + " associated with the id = " + id + "."));
        }

        @Override
        public UserDTO getUserAccountByUsername(String username) {

                return userEntityToDTO(
                                userRepository.findByUsername(username)
                                                .orElseThrow(
                                                                () -> new IllegalArgumentException(
                                                                                "Could not be found a user account "
                                                                                                + " associated with the username = "
                                                                                                + username + ".")));
        }

        /**
         * Returns a list of all the user account entries found within the User table in
         * the database
         *
         * @return list of users. If there aren't any user entry then an empty list is
         *         returned
         */
        @Override
        public Map<String, Object> getUsersAccounts(Integer pageNo, Integer pageSize, String[] sortBy,
                        String sortDirection) {
                final Pageable pageRequest = PageRequest.of(pageNo, pageSize,
                                Sort.by(getSortDirection(sortDirection), sortBy));

                return userEntityToDTO(this.userRepository.findAll(pageRequest));
        }

        /**
         * creates, store and retrieve new User entry {@link User} from the database
         *
         * @param userDTO transfers user account data {@link UserDTO}
         * @return Return the newly created user entry object
         */
        @Override
        public User createAdminUser(UserDTO userDTO) {

                Role adminRole = roleRepository.findByRole("ROLE_ADMIN")
                                .orElseThrow(() -> new ResourceNotFoundException("Admin account couldn't be create " +
                                                "because the role admin isn't supported or found in database."));

                Role userRole = roleRepository.findByRole("ROLE_USER")
                                .orElseThrow(() -> new ResourceNotFoundException("User account couldn't be create " +
                                                "because the role user isn't supported or found in database."));

                List<Role> roles = new ArrayList<>();
                roles.add(adminRole);
                roles.add(userRole);

                final DateGenerator dateGenerator = new DateGenerator();

                return userRepository
                                .saveAndFlush(new User.Builder(userDTO.getUsername(), userDTO.getFirstName(),
                                                userDTO.getLastName(),
                                                userDTO.getPassword(), userDTO.getAddress(), userDTO.getDateOfBirth(),
                                                userDTO.getEmail(),
                                                userDTO.getPhoneNo(), roles, dateGenerator.getLocalDate(),
                                                dateGenerator.getLocalDate())
                                                .build());
        }

        /**
         * Update and return the user entry with the specified id in the database.
         *
         * @param authentication Represents the token for an authentication request or
         *                       for an authenticated principal once the request has
         *                       been
         *                       processed by the
         *                       AuthenticationManager.authenticate(Authentication)
         *                       method.
         * @param userDTO        userDTO transfers user account data {@link UserDTO}
         * @param id             primary key
         * @throws IllegalArgumentException if the user entry with the specified id
         *                                  isn't
         * @return the update user object with the id
         */
        @Override
        public User updateUser(Authentication authentication, UserDTO userDTO, int id) {

                final User userRecord = userRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Could not be found a user account +"
                                                + " associated with the id = " + id + "."));
                final boolean isUserAuthorized = new OperationAuthorizationChecker(userRecord, authentication).check();

                if (!isUserAuthorized) {
                        throw new IllegalArgumentException("It's forbidden to edit another User Account");
                }

                return userRepository.saveAndFlush(userDTOToUserEntity(userDTO, userRecord));
        }

        /**
         *
         * @param authentication
         * @param id
         * @return
         * @throws ResourceNotFoundException
         */
        @Override
        public String deleteUser(Authentication authentication, int id) throws ResourceNotFoundException {

                User userOnRecord = userRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Could not be found a user account +"
                                                + " associated with the id = " + id + "."));

                boolean isUserAuthorized = new OperationAuthorizationChecker(userOnRecord, authentication).check();

                if (!isUserAuthorized)
                        throw new IllegalArgumentException("Operation forbidden.");

                userRepository.deleteById(id);

                return "Account with id = " + id + " deleted";
        }

        private Map<String, Object> userEntityToDTO(Page<User> pageResult) {

                List<UserDTO> usersAccountInfo2 = new ArrayList<>();

                // add self-link that supports each product item of the product collection
                for (int i = 0; i < pageResult.getContent().size(); i++) {

                        usersAccountInfo2.add(new UserDTO.Builder()
                                        .id(pageResult.getContent().get(i).getId())
                                        .username(pageResult.getContent().get(i).getUsername())
                                        .firstName(pageResult.getContent().get(i).getFirstName())
                                        .lastName(pageResult.getContent().get(i).getLastName())
                                        .address(pageResult.getContent().get(i).getAddress())
                                        .dateOfBirth(pageResult.getContent().get(i).getDateOfBirth())
                                        .email(pageResult.getContent().get(i).getEmail())
                                        .phoneNo(pageResult.getContent().get(i).getPhoneNo())
                                        .createdAt(pageResult.getContent().get(i).getCreatedAt())
                                        .modifiedAt(pageResult.getContent().get(i).getModifiedAt())
                                        .build());

                }

                // get sortBy and orderBy property requested page result.
                List<Sort.Order> orders = pageResult.getSort().stream().collect(Collectors.toList());

                String[] sortBy = new String[orders.size()];
                String[] sortDirection = new String[orders.size()];

                for (int i = 0; i < orders.size(); i++) {
                        sortBy[i] = orders.get(i).getProperty();
                        sortDirection[i] = orders.get(i).getDirection().toString();
                }

                // add outer self-link that supports retrieving the product collection
                String baseUri = BasicLinkBuilder.linkToCurrentMapping().toString();
                String selfLink = baseUri + "/users" + "?" + "pageNo=" + pageResult.getPageable().getPageNumber()
                                + "&pageSize=" + pageResult.getPageable().getPageSize() + "&sortBy=" + sortBy[0]
                                + "&sortDirection=" + sortDirection[0];

                Map<String, Object> json = new LinkedHashMap<>();

                json.put("users", CollectionModel.of(usersAccountInfo2, Link.of(selfLink)));

                for (Sort.Order order : pageResult.nextPageable().getSort()) {
                        if (pageResult.nextPageable().isPaged())
                                json.put("next_page_url", Link.of(selfLink));
                }

                for (Sort.Order order : pageResult.previousPageable().getSort()) {
                        if (pageResult.previousPageable().isPaged())
                                json.put("previous_page_url", Link.of(selfLink));
                }

                json.put("other-info", pageResult.getPageable());

                return Collections.unmodifiableMap(json);

        }

        private UserDTO userEntityToDTO(User user) {

                return new UserDTO.Builder()
                                .id(user.getId())
                                .username(user.getUsername())
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .address(user.getAddress())
                                .dateOfBirth(user.getDateOfBirth())
                                .email(user.getEmail())
                                .phoneNo(user.getPhoneNo())
                                .roles(user.getRoles())
                                .createdAt(user.getCreatedAt())
                                .modifiedAt(user.getModifiedAt())
                                .build();

        }

        /**
         * transfers
         *
         * @param userDTO    userDTO transfers user account data {@link UserDTO}.
         *                   It contains the fields that need to be updated and their
         *                   values.
         * @param userRecord current stored user account data
         * @return the update User object
         */
        private User userDTOToUserEntity(UserDTO userDTO, User userRecord) {

                final LocalDateTime currentDate = DateGenerator.getLocalDate();

                final Integer id = userRecord.getId();
                final String username = (userDTO.getUsername() != null) ? userDTO.getUsername()
                                : userRecord.getUsername();
                final String firstName = (userDTO.getFirstName() != null) ? userDTO.getFirstName()
                                : userRecord.getFirstName();
                final String lastName = (userDTO.getLastName() != null) ? userDTO.getLastName()
                                : userRecord.getLastName();
                final String password = (userDTO.getPassword() != null) ? userDTO.getPassword()
                                : userRecord.getPassword();
                final String address = (userDTO.getAddress() != null) ? userDTO.getAddress() : userRecord.getAddress();
                final String dateOfBirth = (userDTO.getDateOfBirth() != null) ? userDTO.getDateOfBirth()
                                : userRecord.getDateOfBirth();
                final String email = (userDTO.getEmail() != null) ? userDTO.getEmail() : userRecord.getEmail();
                final String phoneNo = (userDTO.getPhoneNo() != null) ? userDTO.getPhoneNo() : userRecord.getPhoneNo();
                final LocalDateTime createAt = (userRecord.getCreatedAt() != null) ? userRecord.getCreatedAt()
                                : currentDate;
                final LocalDateTime modifiedAt = currentDate;
                final List<Role> roles = userRecord.getRoles();

                return new User.Builder(username, firstName, lastName, password, address, dateOfBirth,
                                email, phoneNo, roles, createAt, modifiedAt).id(id).build();
        }

        // To do:
        /*
         * @Override
         * public Iterable<User> getUserByYear() {
         * 
         * return null;
         * }
         * 
         * @Override
         * public Iterable<User> getUserByMonth() {
         * return null;
         * }
         * 
         * @Override
         * public Iterable<User> getUserByWeek() {
         * return null;
         * }
         * 
         * @Override
         * public Iterable<User> getUserByDay() {
         * return null;
         * }
         */

        private Sort.Direction getSortDirection(String direction) {

                if (direction.equalsIgnoreCase("asc")) {
                        return Sort.Direction.ASC;
                }

                return Sort.Direction.DESC;
        }

}
