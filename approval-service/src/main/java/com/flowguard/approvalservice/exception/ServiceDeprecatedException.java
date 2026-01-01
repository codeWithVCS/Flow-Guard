package com.flowguard.approvalservice.exception;

public class ServiceDeprecatedException extends RuntimeException {
    public ServiceDeprecatedException(String message) {
        super(message);
    }
}
