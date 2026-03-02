package com.lynn.epigramapp.exception;

import java.time.LocalDateTime;

/**
 * Class for all error responses returned from the API
 */
public class ErrorResponse {

    private final int status;               // HTTP status code
    private final String message;           // Error message
    private final ErrorCode errorCode;      // Error code enum that specifies exact cause
    private final LocalDateTime timestamp;  // Time of error

    public ErrorResponse(int status, String message, ErrorCode errorCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public ErrorCode getErrorCode() {return errorCode; }
    public LocalDateTime getTimestamp() { return timestamp; }
}