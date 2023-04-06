package com.online_shopping_rest_api.models;

import com.online_shopping_rest_api.utils.DateGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


/**
 * This entity class is responsible for persisting user roles to the JPA database.
 * It uses a Builder pattern to enforce immutability to its object.
 * The application uses these roles to determine which users are allowed to do a specific database operation.
 */
@Entity
@Table(name="Roles")
@NoArgsConstructor
@Getter
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String role;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
    private List<User> users;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    public static class Builder{

        private Integer id;
        private final String role;
        private List<User> users;

        public Builder (String role){
            this.role = role;
        }

        public Builder id(Integer id){
            this.id = id;
            return this;
        }

        public Builder users(List<User> users){
            this.users = users;
            return this;
        }

        public Role build(){
            return new Role(this);
        }

    }

    private Role(Builder builder){
        final LocalDateTime date = new DateGenerator().getLocalDate();

        this.id = builder.id;
        this.role = builder.role;
        this.users = builder.users;
        this.createdAt = date;
        this.modifiedAt = date;
    }

}
