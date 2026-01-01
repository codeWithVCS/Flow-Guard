package com.flowguard.approvalservice.api.dto.response;

import java.time.Instant;
import java.util.UUID;

public class ApprovalResponse {

    private UUID id;
    private UUID deploymentId;
    private String approvedBy;
    private String comment;
    private Instant approvedAt;

    public ApprovalResponse(){}

    public ApprovalResponse(UUID id, UUID deploymentId, String approvedBy, String comment, Instant approvedAt) {
        this.id = id;
        this.deploymentId = deploymentId;
        this.approvedBy = approvedBy;
        this.comment = comment;
        this.approvedAt = approvedAt;
    }

    public UUID getId() {return id;}
    public UUID getDeploymentId() {return deploymentId;}
    public String getApprovedBy() {return approvedBy;}
    public String getComment() {return comment;}
    public Instant getApprovedAt() {return approvedAt;}

}
