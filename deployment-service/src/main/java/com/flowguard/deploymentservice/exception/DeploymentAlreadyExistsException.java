package com.flowguard.deploymentservice.exception;

public class DeploymentAlreadyExistsException extends RuntimeException {
    public DeploymentAlreadyExistsException(String message) {
        super(message);
    }
}
