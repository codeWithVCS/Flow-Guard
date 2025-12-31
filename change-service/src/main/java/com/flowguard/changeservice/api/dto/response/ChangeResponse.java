package com.flowguard.changeservice.api.dto.response;

import com.flowguard.changeservice.domain.ChangeType;
import com.flowguard.changeservice.domain.ReferenceType;

import java.time.Instant;
import java.util.UUID;

public class ChangeResponse {

    private UUID id;
    private UUID serviceId;
    private ChangeType changeType;
    private String summary;
    private String description;
    private ReferenceType referenceType;
    private String referenceId;
    private String author;
    private Instant createdAt;

    public ChangeResponse() {}

    public ChangeResponse(
            UUID id,
            UUID serviceId,
            ChangeType changeType,
            String summary,
            String description,
            ReferenceType referenceType,
            String referenceId,
            String author,
            Instant createdAt) {
        this.id = id;
        this.serviceId = serviceId;
        this.changeType = changeType;
        this.summary = summary;
        this.description = description;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.author = author;
        this.createdAt = createdAt;
    }

    public UUID getId() {return id;}
    public UUID getServiceId() {return serviceId;}
    public ChangeType getChangeType() {return changeType;}
    public String getSummary() {return summary;}
    public String getDescription() {return description;}
    public ReferenceType getReferenceType() {return referenceType;}
    public String getReferenceId() {return referenceId;}
    public String getAuthor() {return author;}
    public Instant getCreatedAt() {return createdAt;}

    public void setId(UUID id) {this.id = id;}
    public void setServiceId(UUID serviceId) {this.serviceId = serviceId;}
    public void setChangeType(ChangeType changeType) {this.changeType = changeType;}
    public void setSummary(String summary) {this.summary = summary;}
    public void setDescription(String description) {this.description = description;}
    public void setReferenceType(ReferenceType referenceType) {this.referenceType = referenceType;}
    public void setReferenceId(String referenceId) {this.referenceId = referenceId;}
    public void setAuthor(String author) {this.author = author;}
    public void setCreatedAt(Instant createdAt) {this.createdAt = createdAt;}

}
