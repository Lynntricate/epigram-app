package com.lynn.epigramapp.exception;

/**
 * This enum is used to specify in a particular ErrorResponse what the exact problem was.
 *
 * This allows the API to distinguish different causes that may have the same HTTP status
 * code in ErrorResponse objects
 */
public enum ErrorCode {
    INVALID_USERNAME,
    INVALID_PASSWORD,
    INCORRECT_PASSWORD,
    USERNAME_ALREADY_EXISTS,
    USERNAME_NOT_FOUND,
    NO_EPIGRAM_AUTHOR,
    INTERNAL_ERROR
}