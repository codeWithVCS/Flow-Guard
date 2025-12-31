package com.flowguard.changeservice.exception;

public class ChangeAlreadyExistsException extends RuntimeException {
    public ChangeAlreadyExistsException(String message) {
        super(message);
    }
}
