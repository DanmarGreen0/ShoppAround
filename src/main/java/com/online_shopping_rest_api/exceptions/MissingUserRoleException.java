package com.online_shopping_rest_api.exceptions;

/**
 * custom exception: it's thrown when a role that's supposed to be present in the Role database is not found in the database.
 */
public class MissingUserRoleException extends RuntimeException{

    public MissingUserRoleException(String message) {
        super(message);
    }

    public MissingUserRoleException(String message, Throwable cause) {
        super(message, cause);
    }
}