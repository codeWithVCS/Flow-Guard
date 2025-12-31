package com.flowguard.changeservice.exception;

public class ServiceDeprecatedException extends RuntimeException {
    public ServiceDeprecatedException(String message) {
        super(message);
    }
}
