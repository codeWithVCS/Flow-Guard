package com.flowguard.changeservice.exception;

public class ChangeNotFoundException extends RuntimeException {
    public ChangeNotFoundException(String message) {
        super(message);
    }
}
