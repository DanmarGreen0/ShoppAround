package com.online_shopping_rest_api.exceptions;

/**
 * custom illegal argument exception
 */
public class IllegalArgumentException extends RuntimeException{

    public IllegalArgumentException(String message) {
        super(message);
    }

    public IllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}