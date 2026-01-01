package com.flowguard.approvalservice.exception;

public class InvalidDeploymentReferenceException extends RuntimeException {
    public InvalidDeploymentReferenceException(String message) {
        super(message);
    }
}
