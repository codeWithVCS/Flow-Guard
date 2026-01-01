package com.flowguard.deploymentservice.client;

import com.flowguard.deploymentservice.client.model.ChangeInfo;

import java.util.Optional;
import java.util.UUID;

public interface ChangeServiceClient {

    Optional<ChangeInfo> getChange(UUID changeId);

}
