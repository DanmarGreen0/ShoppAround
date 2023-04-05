package com.online_shopping_rest_api.exceptions;

/**
 * custom bad request exception
 */
public class BadRequestException extends RuntimeException{

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}