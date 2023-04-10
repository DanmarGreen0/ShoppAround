package com.online_shopping_rest_api.unitTest;

import com.online_shopping_rest_api.models.Role;
import com.online_shopping_rest_api.models.User;
import com.online_shopping_rest_api.exceptions.IllegalArgumentException;
import com.online_shopping_rest_api.exceptions.ResourceNotFoundException;
import com.online_shopping_rest_api.repositories.RoleRepository;
import com.online_shopping_rest_api.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public RoleRepository roleRepository;

    public List<Role> roles = new ArrayList<>();

    @PostConstruct
    public void printStart() {

        Role adminRole = roleRepository.findByRole("ROLE_ADMIN")
                .orElseThrow(() -> new ResourceNotFoundException("Admin account couldn't be create " +
                        "because the role admin isn't supported or found in database."));

        Role userRole = roleRepository.findByRole("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("User account couldn't be create " +
                        "because the role user isn't supported or found in database."));

        roles.add(adminRole);
        roles.add(userRole);

        System.out.println(
                "\n<--------------------------------------TestUserRepositoryTest-------------------------------------->\n");
    }

    @Test
    @DisplayName("Create User Entry Test")
    public void createUserEntryTest() {

        try {
            final String username = "Zero";
            final String firstName = "Jeff";
            final String lastName = "Robinson";
            final String password = "passwordZ";
            final String address = "20 Bridge Dr, Cedar Rapids, IA 52402";
            final String dateOfBirth = "1997-5-27";
            final String email = "JeffRobinson@gmail.com";
            final String phoneNo = "(202) 224–3121";
            final List<Role> roles = this.roles;

            final List<User> userList = userRepository.findAll();

            if (userList.isEmpty())
                return;

            for (User user : userList) {
                if (user.getUsername().equals("Zero")) {

                    printEntries(userList);

                    assertThat(user.getUsername()).isEqualTo(username);
                    assertThat(user.getFirstName()).isEqualTo(firstName);
                    assertThat(user.getLastName()).isEqualTo(lastName);
                    assertThat(BCrypt.checkpw(password, user.getPassword())).isTrue();
                    assertThat(user.getDateOfBirth()).isEqualTo(dateOfBirth);
                    assertThat(user.getEmail()).isEqualTo(email);
                    assertThat(user.getAddress()).isEqualTo(address);
                    assertThat(user.getPhoneNo()).isEqualTo(phoneNo);

                }
            }
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("");
        }

    }

    public void printEntries(List<User> users) {

        if (users.size() == 0)
            return;

        users.forEach(user -> {
            System.out.println("id" + ": " + user.getId());
            System.out.println("username" + ": " + user.getUsername());
            System.out.println("firstName" + ": " + user.getFirstName());
            System.out.println("lastName" + ": " + user.getLastName());
            System.out.println("password" + ": " + user.getPassword());
            System.out.println("address" + ": " + user.getAddress());
            System.out.println("dateOfBirth" + ": " + user.getDateOfBirth());
            System.out.println("email" + ": " + user.getEmail());
            System.out.println("phoneNo" + ": " + user.getPhoneNo());
            System.out.println("createdAt" + ": " + user.getCreatedAt());
            System.out.println("modifiedAt" + ": " + user.getModifiedAt());
            System.out.print("\n");
        });

        System.out.println(
                "<-------------------------------------------End---------------------------------------------------->\n");

    }
}
