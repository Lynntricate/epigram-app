package com.lynn.epigramapp.exception;

public class MissingAuthorException extends RuntimeException{
    public MissingAuthorException() {
        super("An epigram must have an author");
    }
}
