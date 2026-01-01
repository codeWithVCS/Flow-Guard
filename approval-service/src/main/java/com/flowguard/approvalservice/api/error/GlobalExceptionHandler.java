package com.flowguard.approvalservice.api.error;

import com.flowguard.approvalservice.exception.ApprovalAlreadyExistsException;
import com.flowguard.approvalservice.exception.ApprovalNotFoundException;
import com.flowguard.approvalservice.exception.ApprovalNotRequiredException;
import com.flowguard.approvalservice.exception.InvalidDeploymentReferenceException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> build(
            HttpStatus status,
            String errorCode,
            String message
    ) {
        return ResponseEntity.status(status).body(
                new ErrorResponse(
                        errorCode,
                        message,
                        Instant.now(),
                        MDC.get("traceId")
                )
        );
    }

    @ExceptionHandler(ApprovalAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleApprovalAlreadyExists(
            ApprovalAlreadyExistsException ex
    ) {
        return build(
                HttpStatus.CONFLICT,
                "APPROVAL_ALREADY_EXISTS",
                ex.getMessage()
        );
    }

    @ExceptionHandler(ApprovalNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleApprovalNotFound(
            ApprovalNotFoundException ex
    ) {
        return build(
                HttpStatus.NOT_FOUND,
                "APPROVAL_NOT_FOUND",
                ex.getMessage()
        );
    }

    @ExceptionHandler(InvalidDeploymentReferenceException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDeploymentReference(
            InvalidDeploymentReferenceException ex
    ) {
        return build(
                HttpStatus.BAD_REQUEST,
                "INVALID_DEPLOYMENT_REFERENCE",
                ex.getMessage()
        );
    }

    @ExceptionHandler(ApprovalNotRequiredException.class)
    public ResponseEntity<ErrorResponse> handleApprovalNotRequired(
            ApprovalNotRequiredException ex
    ) {
        return build(
                HttpStatus.BAD_REQUEST,
                "APPROVAL_NOT_REQUIRED",
                ex.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex
    ) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return build(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                message
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex
    ) {
        return build(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                ex.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        ex.printStackTrace();
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "Unexpected error occurred"
        );
    }
}
