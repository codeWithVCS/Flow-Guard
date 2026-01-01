package com.flowguard.deploymentservice.api.dto.request;

import com.flowguard.deploymentservice.domain.Environment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.UUID;

public class CreateDeploymentRequest {

    @NotNull
    private UUID serviceId;

    @NotNull
    private Environment environment;

    @NotBlank
    @Size(max = 50)
    private String version;

    @NotEmpty
    private Set<UUID> changeIds;

    @NotBlank
    @Size(min = 3, max = 100)
    private String deployedBy;

    public CreateDeploymentRequest() {}

    public CreateDeploymentRequest(
            UUID serviceId,
            Environment environment,
            String version,
            Set<UUID> changeIds,
            String deployedBy) {
        this.serviceId = serviceId;
        this.environment = environment;
        this.version = version;
        this.changeIds = changeIds;
        this.deployedBy = deployedBy;
    }

    public UUID getServiceId() {return serviceId;}
    public Environment getEnvironment() {return environment;}
    public String getVersion() {return version;}
    public Set<UUID> getChangeIds() {return changeIds;}
    public String getDeployedBy() {return deployedBy;}

}
