package com.flowguard.deploymentservice.api.dto.response;

import com.flowguard.deploymentservice.domain.Environment;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class DeploymentResponse {

    private UUID id;
    private UUID serviceId;
    private Environment environment;
    private String version;
    private Set<UUID> changeIds;
    private Instant deployedAt;
    private String deployedBy;

    public DeploymentResponse(
            UUID id,
            UUID serviceId,
            Environment environment,
            String version,
            Set<UUID> changeIds,
            Instant deployedAt,
            String deployedBy) {
        this.id = id;
        this.serviceId = serviceId;
        this.version = version;
        this.environment = environment;
        this.changeIds = changeIds;
        this.deployedAt = deployedAt;
        this.deployedBy = deployedBy;
    }

    public UUID getId() {return id;}
    public UUID getServiceId() {return serviceId;}
    public Environment getEnvironment() {return environment;}
    public String getVersion() {return version;}
    public Set<UUID> getChangeIds() {return changeIds;}
    public Instant getDeployedAt() {return deployedAt;}
    public String getDeployedBy() {return deployedBy;}

}
