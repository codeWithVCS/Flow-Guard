package com.flowguard.deploymentservice.client;

import com.flowguard.deploymentservice.client.model.ServiceInfo;

import java.util.Optional;
import java.util.UUID;

public interface ServiceRegistryClient {

    Optional<ServiceInfo> getService(UUID serviceId);

}
