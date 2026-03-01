package com.lynn.epigramapp.exception;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException (String reason) {
        super(reason);
    }

}
