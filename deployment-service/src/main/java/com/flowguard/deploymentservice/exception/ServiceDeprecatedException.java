package com.flowguard.deploymentservice.exception;

public class ServiceDeprecatedException extends RuntimeException {
    public ServiceDeprecatedException(String message) {
        super(message);
    }
}
