package com.lynn.epigramapp.exception;

public class InvalidUsernameException extends RuntimeException {
    public InvalidUsernameException (String reason) {
        super(reason);
    }
}
