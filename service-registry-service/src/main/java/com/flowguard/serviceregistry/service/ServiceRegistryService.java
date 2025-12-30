package com.flowguard.serviceregistry.service;

import com.flowguard.serviceregistry.api.dto.request.CreateServiceRequest;
import com.flowguard.serviceregistry.api.dto.response.ServiceResponse;
import com.flowguard.serviceregistry.api.dto.response.ServiceSummaryResponse;
import com.flowguard.serviceregistry.domain.ServiceStatus;

import java.util.List;
import java.util.UUID;

public interface ServiceRegistryService {
    ServiceResponse register(CreateServiceRequest request);
    ServiceResponse getById(UUID id);
    ServiceResponse getByName(String serviceName);
    List<ServiceSummaryResponse> listByStatus(ServiceStatus status);
    void deprecate(UUID id);
}

