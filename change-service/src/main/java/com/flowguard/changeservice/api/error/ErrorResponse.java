package com.flowguard.changeservice.api.error;

import java.time.Instant;

public class ErrorResponse {

    private final String errorCode;
    private final  String message;
    private final Instant timestamp;
    private final String traceId;

    public ErrorResponse(
            String errorCode,
            String message,
            Instant timestamp,
            String traceId
    ){
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = timestamp;
        this.traceId = traceId;
    }

    public String getErrorCode(){return this.errorCode;}
    public String getMessage(){return this.message;}
    public Instant getTimestamp(){return this.timestamp;}
    public String getTraceId(){return this.traceId;}

}
