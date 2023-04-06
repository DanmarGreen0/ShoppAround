package com.online_shopping_rest_api.exceptions;


import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

/**
 * It holds the thrown exception details
 *
 * @param message exception message
 * @param httpStatus status code
 * @param zonedDateTime date and time
 */
public record ErrorDetails(String message, HttpStatus httpStatus, ZonedDateTime zonedDateTime) {
}
