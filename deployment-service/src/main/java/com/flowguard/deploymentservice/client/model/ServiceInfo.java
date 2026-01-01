package com.flowguard.deploymentservice.client.model;

import java.util.UUID;

public class ServiceInfo {

    private UUID id;
    private ServiceStatus status;
    private ServiceCriticality criticality;

    public UUID getServiceId() {return id;}
    public ServiceStatus getStatus() {return status;}
    public ServiceCriticality getCriticality() {return criticality;}

}
