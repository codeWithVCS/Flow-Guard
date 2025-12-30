package com.flowguard.serviceregistry.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "services")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "service_id")
    private UUID id;

    @NotBlank
    @Column(name = "service_name", unique = true, updatable = false)
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-z][a-z0-9-]+$")
    private String serviceName;

    @NotBlank
    @Column(name = "owner_team")
    @Size(min = 3, max = 50)
    private String ownerTeam;

    @Enumerated(EnumType.STRING)
    @Column(name = "criticality")
    @NotNull
    private Criticality criticality;

    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ServiceStatus status;

    @NotNull
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", updatable = true)
    private Instant updatedAt;

    protected Service() {}

    public Service(String serviceName, String ownerTeam, Criticality criticality) {
        this.serviceName = serviceName;
        this.ownerTeam = ownerTeam;
        this.criticality = criticality;
        this.status = ServiceStatus.ACTIVE;
    }

    public UUID getId() {return id;}
    public void setId(UUID id) {this.id = id;}
    public String getServiceName() {return serviceName;}
    public String getOwnerTeam() {return ownerTeam;}
    public void setOwnerTeam(String ownerTeam) {this.ownerTeam = ownerTeam;}
    public Criticality getCriticality() {return criticality;}
    public void setCriticality(Criticality criticality) {this.criticality = criticality;}
    public ServiceStatus getStatus() {return status;}
    public Instant getCreatedAt() {return createdAt;}
    public void setCreatedAt(Instant createdAt) {this.createdAt = createdAt;}
    public Instant getUpdatedAt() {return updatedAt;}
    public void setUpdatedAt(Instant updatedAt) {this.updatedAt = updatedAt;}

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public void deprecate() {
        if (this.status == ServiceStatus.DEPRECATED) {
            throw new IllegalStateException("Service is already deprecated");
        }
        this.status = ServiceStatus.DEPRECATED;
    }



}
