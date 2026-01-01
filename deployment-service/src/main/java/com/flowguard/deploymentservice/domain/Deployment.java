package com.flowguard.deploymentservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "deployments")
public class Deployment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "deployment_id", nullable = false, updatable = false)
    private UUID id;

    @NotNull
    @Column(name = "service_id", nullable = false)
    private UUID serviceId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "environment", nullable = false)
    private Environment environment;

    @NotBlank
    @Size(max = 50)
    @Column(name = "version", nullable = false, length = 50)
    private String version;

    @ElementCollection
    @CollectionTable(
            name = "deployment_changes",
            joinColumns = @JoinColumn(name = "deployment_id")
    )
    @Column(name = "change_id", nullable = false)
    @NotEmpty
    private Set<UUID> changeIds;

    @NotNull
    @Column(name = "deployed_at", nullable = false, updatable = false)
    private Instant deployedAt;

    @NotBlank
    @Size(min = 3, max = 100)
    @Column(name = "deployed_by", nullable = false, length = 100)
    private String deployedBy;

    protected Deployment() {}

    public Deployment(
            UUID serviceId,
            Environment environment,
            String version,
            Set<UUID> changeIds,
            String deployedBy
    ) {
        this.serviceId = serviceId;
        this.environment = environment;
        this.version = version;
        this.changeIds = changeIds;
        this.deployedBy = deployedBy;
    }

    public UUID getId() {return id;}
    public UUID getServiceId() {return serviceId;}
    public Environment getEnvironment() {return environment;}
    public String getVersion() {return version;}
    public Set<UUID> getChangeIds() {return changeIds;}
    public Instant getDeployedAt() {return deployedAt;}
    public String getDeployedBy() {return deployedBy;}

    @PrePersist
    public void prePersist() {
        this.deployedAt = Instant.now();
    }
}
