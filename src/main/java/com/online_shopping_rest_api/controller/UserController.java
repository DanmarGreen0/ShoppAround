package com.online_shopping_rest_api.controller;

import com.online_shopping_rest_api.dtos.UserDTO;
import com.online_shopping_rest_api.configs.JwtUtils;
import com.online_shopping_rest_api.dtos.LoginRequest;
import com.online_shopping_rest_api.exceptions.BadRequestException;
import com.online_shopping_rest_api.models.User;
import com.online_shopping_rest_api.services.UserServiceImpl;
import com.online_shopping_rest_api.utils.MapUserEntity;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
// @CrossOrigin("http://localhost:8080")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private MapUserEntity mapUserEntity;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping(path = "/perform_login")
    // @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<Object> perform_login(@RequestBody LoginRequest loginRequest) {
        System.out.println("here------>>>>" + loginRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        // ApplicationUserPrincipal userDetails = (ApplicationUserPrincipal)
        // authentication.getPrincipal();
        // List<String> roles = userDetails.getAuthorities().stream()
        // .map(item -> item.getAuthority())
        // .collect(Collectors.toList());

        HashMap<String, Object> responseBody = new HashMap<>();
        responseBody.put("accountInfo", userService.getUserAccountByUsername(loginRequest.getUsername()));
        responseBody.put("jwt", jwt);

        return new ResponseEntity<>(responseBody, HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/users", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_MASTER_ADMIN')")
    public ResponseEntity<Object> getUsersAccounts(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "2") Integer pageSize,
            @RequestParam(defaultValue = "username") String[] sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Map<String, Object> pageResult = userService.getUsersAccounts(pageNo, pageSize, sortBy, sortDirection);

        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    @GetMapping(value = { "/user", "/user/{id}" })
    // @PreAuthorize("hasAnyRole('ROLE_MASTER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<Object> getUserAccountBy(@PathVariable Optional<Integer> id,
            @RequestParam Optional<String> username) {

        if (username.isPresent()) {

            return new ResponseEntity<>(
                    userService.getUserAccountByUsername(username.get()),
                    HttpStatus.OK);
        } else if (id.isEmpty()) {
            throw new BadRequestException("The param id value must be equal or greater than zero." +
                    " Another option is using a query param to get a user by his/her username. ");
        }

        return new ResponseEntity<>(
                mapUserEntity.toJson(userService.getUserAccountById(id.get())),
                HttpStatus.OK);
    }

    @PostMapping(path = "/user", produces = "application/json")
    // @PreAuthorize("hasRole('ROLE_MASTER_ADMIN')")
    public ResponseEntity<Object> addAdminUser(@RequestBody UserDTO userDTO) {

        final Object userJson = mapUserEntity.toJson(userService.createAdminUser(userDTO));

        return new ResponseEntity<>(userJson, HttpStatus.CREATED);
    }

    @PatchMapping(path = "/user/{id}", produces = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_MASTER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<Object> updateUser(Authentication authentication, @RequestBody UserDTO userDTO,
            @PathVariable int id) {

        if (id < 0)
            throw new BadRequestException("The value of the param id must be equal or greater than zero.");

        final Object userJson = mapUserEntity.toJson(userService.updateUser(authentication, userDTO, id));

        return new ResponseEntity<>(userJson, HttpStatus.OK);
    }

    @DeleteMapping(path = "/user/{id}", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_MASTER_ADMIN')")
    public ResponseEntity<String> deleteUser(Authentication authentication, @PathVariable int id) {

        String delAccId = userService.deleteUser(authentication, id);

        return new ResponseEntity<>(delAccId, HttpStatus.OK);
    }

    // add self links to the productDTO collection object and paging details
    private static Map<String, Object> pageableToDTOList(Page<User> pageResult) {

        List<Map<String, Object>> userDTOList = new ArrayList<>();

        // add self-link that supports each product item of the product collection
        for (int i = 0; i < pageResult.getContent().size(); i++) {

            // userDTOList.add(
            // new UserDTO.Builder(
            // pageResult.getContent().get(i).getUsername(),
            // pageResult.getContent().get(i).getFirstName(),
            // pageResult.getContent().get(i).getLastName(),
            // pageResult.getContent().get(i).getAddress(),
            // pageResult.getContent().get(i).getDateOfBirth(),
            // pageResult.getContent().get(i).getEmail(),
            // pageResult.getContent().get(i).getPhoneNo(),
            // pageResult.getContent().get(i).getCreatedAt(),
            // pageResult.getContent().get(i).getModifiedAt())
            // .id(
            // pageResult.getContent().get(i).getId())
            // .roles(
            // pageResult.getContent().get(i).getRoles())
            // .build());

            Link selfLink = linkTo(UserController.class)
                    .slash("user" + "/" + pageResult.getContent().get(i).getId())
                    .withSelfRel();

            // userDTOList.get(i).add(Collections.singleton(selfLink));
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
        Link OrderDetailsLink = linkTo(
                methodOn(UserController.class).getUsersAccounts(pageResult.getPageable().getPageNumber(),
                        pageResult.getPageable().getPageSize(), sortBy, sortDirection[0]))
                .withSelfRel();

        Map<String, Object> jsonObj = new LinkedHashMap<>();
        jsonObj.put("data", CollectionModel.of(userDTOList, OrderDetailsLink));

        for (Sort.Order order : pageResult.nextPageable().getSort()) {
            if (pageResult.nextPageable().isPaged())
                jsonObj.put("next_page_url", linkTo(methodOn(UserController.class)
                        .getUsersAccounts(
                                pageResult.nextPageable().getPageNumber(),
                                pageResult.nextPageable().getPageSize(),
                                sortBy, sortDirection[0]))
                        .withSelfRel());
        }

        for (Sort.Order order : pageResult.previousPageable().getSort()) {
            if (pageResult.previousPageable().isPaged())
                jsonObj.put("previous_page_url",
                        linkTo(methodOn(UserController.class)
                                .getUsersAccounts(
                                        pageResult.previousPageable().getPageNumber(),
                                        pageResult.previousPageable().getPageSize(),
                                        sortBy,
                                        sortDirection[0]))
                                .withSelfRel());
        }

        jsonObj.put("other-info", pageResult.getPageable());

        return Collections.unmodifiableMap(jsonObj);
    }

}
