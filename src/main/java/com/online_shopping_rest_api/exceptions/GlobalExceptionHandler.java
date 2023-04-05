package com.online_shopping_rest_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * It handles the custom exception that gets thrown by the client.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle bad request exception response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler(value = { BadRequestException.class })
    public ResponseEntity<?> handleBadRequestException(BadRequestException ex) {

        final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(),
                httpStatus, ZonedDateTime.now(ZoneId.of("Z")));

        return new ResponseEntity<>(errorDetails, httpStatus);
    }

    /**
     * Illegal argument exception response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler(value = { IllegalArgumentException.class })
    public ResponseEntity<?> IllegalArgumentException(IllegalArgumentException ex) {

        final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(),
                httpStatus, ZonedDateTime.now(ZoneId.of("Z")));

        return new ResponseEntity<>(errorDetails, httpStatus);
    }

    /**
     * Handle account not found request exception response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler(value = { ResourceNotFoundException.class })
    public ResponseEntity<?> handleAccountNotFoundRequestException(ResourceNotFoundException ex) {

        final HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        final ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(),
                httpStatus, ZonedDateTime.now(ZoneId.of("Z")));

        return new ResponseEntity<>(errorDetails, httpStatus);
    }

    /**
     * Handle missing user role exception response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler(value = { MissingUserRoleException.class })
    public ResponseEntity<?> handleMissingUserRoleException(MissingUserRoleException ex) {

        final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        final ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(),
                httpStatus, ZonedDateTime.now(ZoneId.of("Z")));

        return new ResponseEntity<>(errorDetails, httpStatus);
    }

    @ExceptionHandler(value = { UnauthorizedException.class })
    public ResponseEntity<ErrorDetails> unauthorizedException(UnauthorizedException ex) {

        final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        final ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(),
                httpStatus, ZonedDateTime.now(ZoneId.of("Z")));

        return new ResponseEntity<ErrorDetails>(errorDetails, httpStatus);
    }

}
