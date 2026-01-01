package com.flowguard.changeservice.api.error;

import com.flowguard.changeservice.exception.ChangeAlreadyExistsException;
import com.flowguard.changeservice.exception.ChangeNotFoundException;
import com.flowguard.changeservice.exception.InvalidServiceReferenceException;
import com.flowguard.changeservice.exception.ServiceDeprecatedException;
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

    @ExceptionHandler(ChangeAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleChangeAlreadyExistsException(ChangeAlreadyExistsException ex){
        return build(HttpStatus.CONFLICT, "CHANGE_ALREADY_EXISTS", ex.getMessage());
    }

    @ExceptionHandler(ChangeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleChangeNotFoundException(ChangeNotFoundException ex){
        return build(HttpStatus.NOT_FOUND, "CHANGE_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(InvalidServiceReferenceException.class)
    public ResponseEntity<ErrorResponse> handleInvalidServiceReferenceException(InvalidServiceReferenceException ex){
        return build(HttpStatus.BAD_REQUEST, "INVALID_SERVICE_REFERENCE", ex.getMessage());
    }

    @ExceptionHandler(ServiceDeprecatedException.class)
    public ResponseEntity<ErrorResponse> handleServiceDeprecatedException(ServiceDeprecatedException ex){
        return build(HttpStatus.BAD_REQUEST, "SERVICE_DEPRECATED", ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex){
        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", ex.getMessage());
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex){
        ex.printStackTrace();
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "Unexpected error occurred"
        );
    }

}
