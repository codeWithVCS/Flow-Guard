package com.flowguard.deploymentservice.api.dto.response;

import com.flowguard.deploymentservice.domain.Environment;

import java.time.Instant;
import java.util.UUID;

public class DeploymentSummaryResponse {

    private UUID id;
    private Environment environment;
    private String version;
    private Instant deployedAt;

    public DeploymentSummaryResponse(
            UUID id,
            Environment environment,
            String version,
            Instant deployedAt) {
        this.id = id;
        this.environment = environment;
        this.version = version;
        this.deployedAt = deployedAt;
    }

    public UUID getId() {return id;}
    public Environment getEnvironment() {return environment;}
    public String getVersion() {return version;}
    public Instant getDeployedAt() {return deployedAt;}

}
