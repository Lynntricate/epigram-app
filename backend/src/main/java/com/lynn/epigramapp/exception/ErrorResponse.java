package com.lynn.epigramapp.exception;

import java.time.LocalDateTime;

public class ErrorResponse {

    private final int status;
    private final String message;
    private final ErrorCode errorCode;
    private final LocalDateTime timestamp;

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