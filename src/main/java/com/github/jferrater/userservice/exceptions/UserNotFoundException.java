package com.github.jferrater.userservice.exceptions;

/**
 * @author joffryferrater
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
