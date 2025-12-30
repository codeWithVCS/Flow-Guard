package com.flowguard.serviceregistry.api.error;

import com.flowguard.serviceregistry.exception.InvalidServiceStateException;
import com.flowguard.serviceregistry.exception.ServiceAlreadyExistsException;
import com.flowguard.serviceregistry.exception.ServiceNotFoundException;
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

    @ExceptionHandler(ServiceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleServiceAlreadyExists(ServiceAlreadyExistsException ex) {
        return build(HttpStatus.CONFLICT, "SERVICE_ALREADY_EXISTS", ex.getMessage());
    }

    @ExceptionHandler(ServiceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleServiceNotFound(ServiceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, "SERVICE_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(InvalidServiceStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidState(InvalidServiceStateException ex) {
        return build(HttpStatus.BAD_REQUEST, "INVALID_SERVICE_STATE", ex.getMessage());
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
        ex.printStackTrace(); // logged for 5xx only
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "Unexpected error occurred");
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String code, String message) {
        return ResponseEntity.status(status).body(
                new ErrorResponse(
                        code,
                        message,
                        Instant.now(),
                        MDC.get("traceId")
                )
        );
    }
}
