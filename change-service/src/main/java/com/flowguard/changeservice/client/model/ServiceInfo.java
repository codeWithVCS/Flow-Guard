package com.flowguard.changeservice.client.model;

import java.util.UUID;

public class ServiceInfo {

    private UUID id;
    private ServiceStatus status;

    public ServiceInfo(){}

    public UUID getId() {return id;}
    public void setId(UUID id) {this.id = id;}
    public ServiceStatus getStatus() {return status;}
    public void setStatus(ServiceStatus status) {this.status = status;}

}
