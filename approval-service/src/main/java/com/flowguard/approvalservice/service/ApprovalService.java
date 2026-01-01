package com.flowguard.approvalservice.service;

import com.flowguard.approvalservice.api.dto.request.CreateApprovalRequest;
import com.flowguard.approvalservice.api.dto.response.ApprovalResponse;

import java.util.UUID;

public interface ApprovalService {

    ApprovalResponse approve(CreateApprovalRequest request);

    ApprovalResponse getByDeploymentId(UUID deploymentId);

}
