package com.flowguard.serviceregistry.api.dto.request;

import com.flowguard.serviceregistry.domain.Criticality;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateServiceRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-z][a-z0-9-]+$")
    private String serviceName;

    @NotBlank
    @Size(min = 3, max = 50)
    private String ownerTeam;

    @NotNull
    private Criticality criticality;

    public CreateServiceRequest() {}

    public CreateServiceRequest(String serviceName, String ownerTeam, Criticality criticality) {
        this.serviceName = serviceName;
        this.ownerTeam = ownerTeam;
        this.criticality = criticality;
    }

    public String getServiceName() {return serviceName;}
    public void setServiceName(String serviceName) {this.serviceName = serviceName;}
    public String getOwnerTeam() {return ownerTeam;}
    public void setOwnerTeam(String ownerTeam) {this.ownerTeam = ownerTeam;}
    public Criticality getCriticality() {return criticality;}
    public void setCriticality(Criticality criticality) {this.criticality = criticality;}
}
