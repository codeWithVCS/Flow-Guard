package com.flowguard.deploymentservice.service;

import com.flowguard.deploymentservice.api.dto.request.CreateDeploymentRequest;
import com.flowguard.deploymentservice.api.dto.response.DeploymentResponse;
import com.flowguard.deploymentservice.api.dto.response.DeploymentSummaryResponse;
import com.flowguard.deploymentservice.domain.Environment;

import java.util.List;
import java.util.UUID;

public interface DeploymentService {

    DeploymentResponse deploy(CreateDeploymentRequest request);

    DeploymentResponse getLatest(UUID serviceId, Environment environment);

    List<DeploymentSummaryResponse> list(UUID serviceId, Environment environment);

}
