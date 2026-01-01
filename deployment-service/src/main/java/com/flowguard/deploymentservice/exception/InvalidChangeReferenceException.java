package com.flowguard.deploymentservice.exception;

public class InvalidChangeReferenceException extends RuntimeException {
    public InvalidChangeReferenceException(String message) {
        super(message);
    }
}
