package com.flowguard.approvalservice.exception;

public class ApprovalNotFoundException extends RuntimeException {
    public ApprovalNotFoundException(String message) {
        super(message);
    }
}
