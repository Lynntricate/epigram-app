package com.lynn.epigramapp.exception;

public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException(String username) {
        super("Invalid credentials for user with username: " + username);

    }
}
