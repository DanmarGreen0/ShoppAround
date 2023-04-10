package com.online_shopping_rest_api.configs;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * encrypt and decrypt user password
 */
public class PasswordEncoder {
    /**
     * Bcrypt password encoder which encode and decode a plain text password string
     *
     * @return the b crypt password encoder
     */
    public BCryptPasswordEncoder bcryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Get encoded password string.
     *
     * @param password the password
     * @return the string
     */
    public String getEncodedPassword(String password){
        return bcryptPasswordEncoder().encode(password);
   }

}
