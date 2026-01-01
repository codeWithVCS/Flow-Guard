package com.flowguard.deploymentservice.api.error;

import com.flowguard.deploymentservice.exception.*;
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
            HttpStatus status, String code, String message) {
        return ResponseEntity.status(status).body(
                new ErrorResponse(
                        code,
                        message,
                        Instant.now(),
                        MDC.get("traceId")
                )
        );
    }

    @ExceptionHandler(DeploymentAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleDeploymentExists(DeploymentAlreadyExistsException ex) {
        return build(HttpStatus.CONFLICT, "DEPLOYMENT_ALREADY_EXISTS", ex.getMessage());
    }

    @ExceptionHandler(DeploymentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(DeploymentNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, "DEPLOYMENT_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(ApprovalRequiredException.class)
    public ResponseEntity<ErrorResponse> handleApprovalRequired(ApprovalRequiredException ex) {
        return build(HttpStatus.FORBIDDEN, "APPROVAL_REQUIRED", ex.getMessage());
    }

    @ExceptionHandler(ServiceDeprecatedException.class)
    public ResponseEntity<ErrorResponse> handleServiceDeprecated(ServiceDeprecatedException ex) {
        return build(HttpStatus.BAD_REQUEST, "SERVICE_DEPRECATED", ex.getMessage());
    }

    @ExceptionHandler(InvalidServiceReferenceException.class)
    public ResponseEntity<ErrorResponse> handleInvalidService(InvalidServiceReferenceException ex) {
        return build(HttpStatus.BAD_REQUEST, "INVALID_SERVICE_REFERENCE", ex.getMessage());
    }

    @ExceptionHandler(InvalidChangeReferenceException.class)
    public ResponseEntity<ErrorResponse> handleInvalidChange(InvalidChangeReferenceException ex) {
        return build(HttpStatus.BAD_REQUEST, "INVALID_CHANGE_REFERENCE", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", ex.getMessage());
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
