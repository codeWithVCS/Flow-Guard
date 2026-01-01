package com.flowguard.approvalservice.exception;

public class ApprovalAlreadyExistsException extends RuntimeException {
    public ApprovalAlreadyExistsException(String message) {
        super(message);
    }
}
