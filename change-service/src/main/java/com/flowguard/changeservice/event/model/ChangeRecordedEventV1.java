package com.flowguard.changeservice.event.model;

import java.time.Instant;
import java.util.UUID;

public final class ChangeRecordedEventV1 {

    private final UUID eventId;
    private final Instant occurredAt;
    private final UUID changeId;
    private final UUID serviceId;
    private final String changeType;
    private final String referenceType;
    private final String referenceId;
    private final String author;

    public ChangeRecordedEventV1(
            UUID eventId,
            Instant occurredAt,
            UUID changeId,
            UUID serviceId,
            String changeType,
            String referenceType,
            String referenceId,
            String author
    ) {
        this.eventId = eventId;
        this.occurredAt = occurredAt;
        this.changeId = changeId;
        this.serviceId = serviceId;
        this.changeType = changeType;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.author = author;
    }

    public UUID getEventId() {return eventId;}
    public Instant getOccurredAt() {return occurredAt;}
    public UUID getChangeId() {return changeId;}
    public UUID getServiceId() {return serviceId;}
    public String getChangeType() {return changeType;}
    public String getReferenceType() {return referenceType;}
    public String getReferenceId() {return referenceId;}
    public String getAuthor() {return author;}

}
