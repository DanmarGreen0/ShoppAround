package com.online_shopping_rest_api.models;

import com.online_shopping_rest_api.configs.PasswordEncoder;
import com.online_shopping_rest_api.exceptions.IllegalArgumentException;
import com.online_shopping_rest_api.utils.InputValidator;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


/**
 * The User class is responsible for encapsulating and persisting user account data to and from the JPA database.
 * It uses a builder pattern because of the handful of instance variables which can cause ambiguity when writing client code.
 * The builder pattern provides client code readability, scalability, optional parameter initialization, and immutable class.
 *
 * @Entity indicates that this class is an entity associated with a table within the JPA database
 * @Table specifies the database table's name in which the persisting data should map
 * @NoArgsConstructor indicates that this class generates a Lombok no-argument constructor method
 * @Getter indicates that this class generates Lombok getter methods
 * @Id specifies the primary key for the Entity
 * @GeneratedValue provides for the specification of generation strategies for the values of primary keys.
 * @Column configure the mapping from an attribute to the JPA database column
 * @ManyToMany create a many-to-many relationship between two entity
 * @JoinTable Specifies the mapping of associations. It is applied to the owning side of an association
 * @JoinColumn Specifies a column for joining an entity association or element collection.
 */
@Entity
@Table(name = "Users")
@NoArgsConstructor
@Getter
public class User implements Serializable {  //read-up on serialization
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="Id", nullable = false)
    private Integer id;
    @Column(name="Username", nullable = false, unique = true)
    private String username;
    @Column(name="First_Name", nullable = false)
    private String firstName;
    @Column(name="Last_Name", nullable = false)
    private String lastName;
    @Column(name="Password", nullable = false)
    private String password;
    @Column(name="Address", nullable = false, unique = true)
    private String address;
    @Column(name="Date_Of_Birth", nullable = false)
    private String dateOfBirth;
    @Column(name="Email", nullable = false, unique = true)
    private String email;
    @Column(name="Phone_No", nullable = false, unique = true)
    private String phoneNo;
    @Column(name="Created_At", nullable = false)
    private LocalDateTime createdAt;
    @Column(name="Modified_At", nullable = false)
    private LocalDateTime modifiedAt;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "Has_Roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;


    public static class Builder{
        private Integer id  = 0;
        private final String username;
        private final String firstName;
        private final String lastName;
        private final String password;
        private final String address;
        private final String dateOfBirth;
        private final String email;
        private final String phoneNo;
        private final List<Role> roles;
        private final LocalDateTime createdAt;
        private final LocalDateTime modifiedAt;

        /**
         * Returns a builder object to the outer class.
         * <p>
         * This class instance variable mirror the instance variables of the outer class.
         *
         * @throws IllegalArgumentException if param username, firstname, lastname, password or email equal null
         * @throws IllegalArgumentException if param username, firstname, lastname, password or email doesn't match
         *                                  their valid criteria {@link InputValidator#checkName(String)},
         *                                  {@link InputValidator#checkPassword(String)} (String)},
         *                                  {@link InputValidator#checkEmail(String)} (String)}
         * @param username the user alias
         * @param firstName the user first name
         * @param lastName the user last name
         * @param password the user account password
         * @param address the user address
         * @param dateOfBirth the date of birth
         * @param email the user email
         * @param phoneNo the user phone number
         * @param createdAt the creation date of the user's account
         * @param modifiedAt The date on which the user last modified his account
         */
        public Builder(String username, String firstName, String lastName, String password,
                       String address, String dateOfBirth, String email, String phoneNo,
                        List<Role> roles, LocalDateTime createdAt, LocalDateTime modifiedAt){

            if(username == null || !InputValidator.checkName(username)){
                throw new IllegalArgumentException("Invalid username. Enter username.");
            }

            if(firstName == null || !InputValidator.checkName(firstName)){
                throw new IllegalArgumentException("Invalid first name. Enter first name.");
            }

            if(lastName == null || !InputValidator.checkName(lastName)){
                throw new IllegalArgumentException("Invalid last name. Enter last name.");
            }

            if( email == null || !InputValidator.checkEmail(email)){
                throw new IllegalArgumentException("Invalid email. Enter email.");
            }

            if(password == null){
                throw new IllegalArgumentException("Invalid password. Enter password.");
            }

            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.password = password;
            this.address = address;
            this.dateOfBirth = dateOfBirth;
            this.email = email;
            this.phoneNo = phoneNo;
            this.createdAt = createdAt;
            this.modifiedAt = modifiedAt;
            this.roles = roles;
        }

        /**
         * @return the current object of the class
         *
         * @param id specifies the primary key of the User entity
         */
        public Builder id(Integer id){
            this.id = id;
            return this;
        }

        /**
         * @return an  instantiation of the User class with the current class object as its parameter
         * @see #User(Builder builder)
         */
        public User build(){
            return new User(this);
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
     * @param builder a builder object which store state of the class {@link Builder}
     */
    private User(Builder builder) {

        final PasswordEncoder decodedPassword = new PasswordEncoder();

        id = builder.id;
        username = builder.username;
        firstName = builder.firstName;
        lastName = builder.lastName;
        password = decodedPassword.getEncodedPassword(builder.password);
        address = builder.address;
        dateOfBirth = builder.dateOfBirth;
        email = builder.email;
        phoneNo = builder.phoneNo;
        createdAt = builder.createdAt;
        modifiedAt = builder.modifiedAt;
        roles = builder.roles;
    }

    @Override
    public String toString() {

        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", email='" + email + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }

}
