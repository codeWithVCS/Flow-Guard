package com.flowguard.approvalservice.client.model;

import java.util.UUID;

public class DeploymentInfo {

    private UUID deploymentId;
    private UUID serviceId;
    private Environment environment;
    private String version;

    public DeploymentInfo() {}

    public UUID getDeploymentId() {return deploymentId;}
    public void setDeploymentId(UUID deploymentId) {this.deploymentId = deploymentId;}
    public UUID getServiceId() {return serviceId;}
    public void setServiceId(UUID serviceId) {this.serviceId = serviceId;}
    public Environment getEnvironment() {return environment;}
    public void setEnvironment(Environment environment) {this.environment = environment;}
    public String getVersion() {return version;}
    public void setVersion(String version) {this.version = version;}

}
