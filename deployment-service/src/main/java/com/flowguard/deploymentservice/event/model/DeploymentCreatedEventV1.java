package com.flowguard.deploymentservice.event.model;

import java.time.Instant;
import java.util.UUID;

public final class DeploymentCreatedEventV1 {

    private final UUID eventId;
    private final DeploymentEventType eventType;
    private final String eventVersion;
    private final Instant occurredAt;

    private final UUID deploymentId;
    private final UUID serviceId;
    private final String environment;
    private final String version;

    public DeploymentCreatedEventV1(
            UUID eventId,
            DeploymentEventType eventType,
            String eventVersion,
            Instant occurredAt,
            UUID deploymentId,
            UUID serviceId,
            String environment,
            String version
    ) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.eventVersion = eventVersion;
        this.occurredAt = occurredAt;
        this.deploymentId = deploymentId;
        this.serviceId = serviceId;
        this.environment = environment;
        this.version = version;
    }

    public UUID getEventId() {return eventId;}
    public DeploymentEventType getEventType() {return eventType;}
    public String getEventVersion() {return eventVersion;}
    public Instant getOccurredAt() {return occurredAt;}
    public UUID getDeploymentId() {return deploymentId;}
    public UUID getServiceId() {return serviceId;}
    public String getEnvironment() {return environment;}
    public String getVersion() {return version;}

}
