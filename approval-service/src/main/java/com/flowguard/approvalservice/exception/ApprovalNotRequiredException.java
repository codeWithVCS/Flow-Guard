package com.flowguard.approvalservice.exception;

public class ApprovalNotRequiredException extends RuntimeException {
    public ApprovalNotRequiredException(String message) {
        super(message);
    }
}
