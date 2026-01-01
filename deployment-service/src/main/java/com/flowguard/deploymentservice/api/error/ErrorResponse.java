package com.flowguard.deploymentservice.api.error;

import java.time.Instant;

public class ErrorResponse {

    private final String errorCode;
    private final String message;
    private final Instant timestamp;
    private final String traceId;

    public ErrorResponse(
            String errorCode,
            String message,
            Instant timestamp,
            String traceId) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = timestamp;
        this.traceId = traceId;
    }

    public String getErrorCode() {return errorCode;}
    public String getMessage() {return message;}
    public Instant getTimestamp() {return timestamp;}
    public String getTraceId() {return traceId;}

}
