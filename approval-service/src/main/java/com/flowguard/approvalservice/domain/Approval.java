package com.flowguard.approvalservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "approvals")
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "approval_id", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "deployment_id", unique = true, nullable = false)
    private UUID deploymentId;

    @NotBlank
    @Size(min = 3, max = 100)
    @Column(name = "approved_by", nullable = false)
    private String approvedBy;

    @Size(max = 500)
    @Column(name = "comment", nullable = true)
    private String comment;

    @NotNull
    @Column(name = "approved_at", nullable = false, updatable = false)
    private Instant approvedAt;

    protected  Approval() {}

    public Approval(
            UUID deploymentId,
            String approvedBy,
            String comment) {
        this.deploymentId = deploymentId;
        this.approvedBy = approvedBy;
        this.comment = comment;
    }

    @PrePersist
    public void prePersist() {
        this.approvedAt = Instant.now();
    }

    public UUID getId() {return id;}
    public UUID getDeploymentId() {return deploymentId;}
    public String getApprovedBy() {return approvedBy;}
    public String getComment() {return comment;}
    public Instant getApprovedAt() {return approvedAt;}

}
