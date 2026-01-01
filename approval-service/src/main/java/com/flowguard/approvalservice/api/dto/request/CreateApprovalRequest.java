package com.flowguard.approvalservice.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class CreateApprovalRequest {

    @NotNull
    private UUID deploymentId;

    @NotBlank
    @Size(min = 3, max = 100)
    private String approvedBy;

    @Size(max = 500)
    private String comment;

    public CreateApprovalRequest() {}

    public CreateApprovalRequest(UUID deploymentId, String approvedBy, String comment) {
        this.deploymentId = deploymentId;
        this.approvedBy = approvedBy;
        this.comment = comment;
    }

    public UUID getDeploymentId() {return deploymentId;}
    public String getApprovedBy() {return approvedBy;}
    public String getComment() {return comment;}

}
