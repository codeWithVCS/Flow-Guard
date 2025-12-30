package com.flowguard.serviceregistry.exception;

public class InvalidServiceStateException extends RuntimeException {
    public InvalidServiceStateException(String message) {
        super(message);
    }
}
