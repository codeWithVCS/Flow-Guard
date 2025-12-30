package com.flowguard.serviceregistry.api.dto.response;

import com.flowguard.serviceregistry.domain.Criticality;
import com.flowguard.serviceregistry.domain.ServiceStatus;

import java.time.Instant;
import java.util.UUID;

public class ServiceResponse {

    private UUID id;
    private String serviceName;
    private String ownerTeam;
    private Criticality criticality;
    private ServiceStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    public ServiceResponse() {}

    public ServiceResponse(
            UUID id,
            String serviceName,
            String ownerTeam,
            Criticality criticality,
            ServiceStatus status,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.serviceName = serviceName;
        this.ownerTeam = ownerTeam;
        this.criticality = criticality;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {return id;}
    public void setId(UUID id) {this.id = id;}
    public String getServiceName() {return serviceName;}
    public void setServiceName(String serviceName) {this.serviceName = serviceName;}
    public String getOwnerTeam() {return ownerTeam;}
    public void setOwnerTeam(String ownerTeam) {this.ownerTeam = ownerTeam;}
    public Criticality getCriticality() {return criticality;}
    public void setCriticality(Criticality criticality) {this.criticality = criticality;}
    public ServiceStatus getStatus() {return status;}
    public void setStatus(ServiceStatus status) {this.status = status;}
    public Instant getCreatedAt() {return createdAt;}
    public void setCreatedAt(Instant createdAt) {this.createdAt = createdAt;}
    public Instant getUpdatedAt() {return updatedAt;}
    public void setUpdatedAt(Instant updatedAt) {this.updatedAt = updatedAt;}

}
