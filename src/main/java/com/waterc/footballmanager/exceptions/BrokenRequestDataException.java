package com.waterc.footballmanager.exceptions;

public class BrokenRequestDataException extends RuntimeException {
    public BrokenRequestDataException() {
    }

    public BrokenRequestDataException(String message) {
        super(message);
    }

    public BrokenRequestDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
