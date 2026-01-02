package com.flowguard.approvalservice.event.model;

import java.time.Instant;
import java.util.UUID;

public class DeploymentApprovedEventV1 {

    private UUID eventId;
    private String eventType;
    private String eventVersion;
    private Instant occurredAt;
    private UUID deploymentId;
    private UUID serviceId;
    private String environment;
    private String approvedBy;
    private Instant approvedAt;

    public DeploymentApprovedEventV1() {
    }

    public DeploymentApprovedEventV1(
            UUID eventId,
            String eventType,
            String eventVersion,
            Instant occurredAt,
            UUID deploymentId,
            UUID serviceId,
            String environment,
            String approvedBy,
            Instant approvedAt
    ) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.eventVersion = eventVersion;
        this.occurredAt = occurredAt;
        this.deploymentId = deploymentId;
        this.serviceId = serviceId;
        this.environment = environment;
        this.approvedBy = approvedBy;
        this.approvedAt = approvedAt;
    }

    public UUID getEventId() {return eventId;}
    public String getEventType() {return eventType;}
    public String getEventVersion() {return eventVersion;}
    public Instant getOccurredAt() {return occurredAt;}
    public UUID getDeploymentId() {return deploymentId;}
    public UUID getServiceId() {return serviceId;}
    public String getEnvironment() {return environment;}
    public String getApprovedBy() {return approvedBy;}
    public Instant getApprovedAt() {return approvedAt;}

}
