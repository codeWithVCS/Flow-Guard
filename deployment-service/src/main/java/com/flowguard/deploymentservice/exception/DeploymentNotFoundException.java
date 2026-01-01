package com.flowguard.deploymentservice.exception;

public class DeploymentNotFoundException extends RuntimeException {
    public DeploymentNotFoundException(String message) {
        super(message);
    }
}
