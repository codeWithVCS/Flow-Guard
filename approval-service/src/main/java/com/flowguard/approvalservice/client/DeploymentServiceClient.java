package com.flowguard.approvalservice.client;

import com.flowguard.approvalservice.client.model.DeploymentInfo;

import java.util.Optional;
import java.util.UUID;

public interface DeploymentServiceClient {
    Optional<DeploymentInfo> getDeployment(UUID deploymentId);
}
