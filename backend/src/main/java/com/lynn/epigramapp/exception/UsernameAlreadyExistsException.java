package com.lynn.epigramapp.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super("User " + username + " already exists");
    }
}
