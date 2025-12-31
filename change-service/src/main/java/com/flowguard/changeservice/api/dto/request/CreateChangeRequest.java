package com.flowguard.changeservice.api.dto.request;

import com.flowguard.changeservice.domain.ChangeType;
import com.flowguard.changeservice.domain.ReferenceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class CreateChangeRequest {

    @NotNull
    private UUID serviceId;

    @NotNull
    private ChangeType changeType;

    @NotBlank
    @Size(min = 10, max = 255)
    private String summary;

    @Size(max = 2000)
    private String description;

    @NotNull
    private ReferenceType referenceType;

    @NotBlank
    @Size(max = 100)
    private String referenceId;

    @NotBlank
    @Size(min = 3, max = 100)
    private String author;

    public CreateChangeRequest(){}

    public CreateChangeRequest(
            UUID serviceId,
            ChangeType changeType,
            String summary,
            String description,
            ReferenceType referenceType,
            String referenceId,
            String author) {
        this.serviceId = serviceId;
        this.changeType = changeType;
        this.summary = summary;
        this.description = description;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.author = author;
    }

    public UUID getServiceId() {return serviceId;}
    public ChangeType getChangeType() {return changeType;}
    public String getSummary() {return summary;}
    public String getDescription() {return description;}
    public ReferenceType getReferenceType() {return referenceType;}
    public String getReferenceId() {return referenceId;}
    public String getAuthor() {return author;}

    public void setServiceId(UUID serviceId) {this.serviceId = serviceId;}
    public void setChangeType(ChangeType changeType) {this.changeType = changeType;}
    public void setSummary(String summary) {this.summary = summary;}
    public void setDescription(String description) {this.description = description;}
    public void setReferenceType(ReferenceType referenceType) {this.referenceType = referenceType;}
    public void setReferenceId(String referenceId) {this.referenceId = referenceId;}
    public void setAuthor(String author) {this.author = author;}

}
