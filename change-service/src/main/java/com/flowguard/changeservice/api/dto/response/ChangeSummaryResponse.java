package com.flowguard.changeservice.api.dto.response;

import com.flowguard.changeservice.domain.ChangeType;
import com.flowguard.changeservice.domain.ReferenceType;

import java.time.Instant;
import java.util.UUID;

public class ChangeSummaryResponse {

    private UUID id;
    private ChangeType changeType;
    private String summary;
    private ReferenceType referenceType;
    private String referenceId;
    private String author;
    private Instant createdAt;

    public ChangeSummaryResponse() {}

    public ChangeSummaryResponse(
            UUID id,
            ChangeType changeType,
            String summary,
            ReferenceType referenceType,
            String referenceId, String author,
            Instant createdAt) {
        this.id = id;
        this.changeType = changeType;
        this.summary = summary;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.author = author;
        this.createdAt = createdAt;
    }

    public UUID getId() {return id;}
    public ChangeType getChangeType() {return changeType;}
    public String getSummary() {return summary;}
    public ReferenceType getReferenceType() {return referenceType;}
    public String getReferenceId() {return referenceId;}
    public String getAuthor() {return author;}
    public Instant getCreatedAt() {return createdAt;}

    public void setId(UUID id) {this.id = id;}
    public void setChangeType(ChangeType changeType) {this.changeType = changeType;}
    public void setSummary(String summary) {this.summary = summary;}
    public void setReferenceType(ReferenceType referenceType) {this.referenceType = referenceType;}
    public void setReferenceId(String referenceId) {this.referenceId = referenceId;}
    public void setAuthor(String author) {this.author = author;}
    public void setCreatedAt(Instant createdAt) {this.createdAt = createdAt;}

}
