package com.flowguard.deploymentservice.exception;

public class InvalidServiceReferenceException extends RuntimeException {
    public InvalidServiceReferenceException(String message) {
        super(message);
    }
}
