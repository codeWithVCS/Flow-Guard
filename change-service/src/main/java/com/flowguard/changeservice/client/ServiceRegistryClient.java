package com.flowguard.changeservice.client;

import com.flowguard.changeservice.client.model.ServiceInfo;

import java.util.Optional;
import java.util.UUID;

public interface ServiceRegistryClient {

    Optional<ServiceInfo> getService(UUID serviceId);

}
