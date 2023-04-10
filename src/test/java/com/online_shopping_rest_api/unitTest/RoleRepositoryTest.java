package com.online_shopping_rest_api.unitTest;

import com.online_shopping_rest_api.models.Role;
import com.online_shopping_rest_api.repositories.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RoleRepositoryTest {
    @Autowired
    RoleRepository roleRepository;

    //creates a role entry, then insert it into the database inside the Product table
    @Test
    public void addRole(){

        final String userRole = "TestRole";
        final Role role = new Role.Builder(userRole).build();
        final Role savedRole = roleRepository.save(role);
        final Role retrievedRole = roleRepository.findById(savedRole.getId()).get();

        assertThat(userRole).isEqualTo(retrievedRole.getRole());

    }
}
