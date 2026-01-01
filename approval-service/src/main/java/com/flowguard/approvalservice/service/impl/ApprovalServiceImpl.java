package com.flowguard.approvalservice.service.impl;

import com.flowguard.approvalservice.api.dto.request.CreateApprovalRequest;
import com.flowguard.approvalservice.api.dto.response.ApprovalResponse;
import com.flowguard.approvalservice.client.DeploymentServiceClient;
import com.flowguard.approvalservice.client.model.DeploymentInfo;
import com.flowguard.approvalservice.client.model.Environment;
import com.flowguard.approvalservice.domain.Approval;
import com.flowguard.approvalservice.exception.ApprovalAlreadyExistsException;
import com.flowguard.approvalservice.exception.ApprovalNotFoundException;
import com.flowguard.approvalservice.exception.ApprovalNotRequiredException;
import com.flowguard.approvalservice.exception.InvalidDeploymentReferenceException;
import com.flowguard.approvalservice.repository.ApprovalRepository;
import com.flowguard.approvalservice.service.ApprovalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final DeploymentServiceClient deploymentServiceClient;

    public ApprovalServiceImpl(
            ApprovalRepository approvalRepository,
            DeploymentServiceClient deploymentServiceClient) {
        this.approvalRepository = approvalRepository;
        this.deploymentServiceClient = deploymentServiceClient;
    }

    @Override
    @Transactional
    public ApprovalResponse approve(CreateApprovalRequest request) {
        DeploymentInfo deployment = deploymentServiceClient.getDeployment(request.getDeploymentId())
                .orElseThrow(() -> new InvalidDeploymentReferenceException("Deployment not found"));
        if(deployment.getEnvironment() != Environment.PROD){
            throw new ApprovalNotRequiredException("Approval is not required for this environment");
        }
        if(approvalRepository.existsByDeploymentId(deployment.getDeploymentId())) {
            throw new ApprovalAlreadyExistsException("Approval already exists");
        }
        Approval approval = new Approval(
                request.getDeploymentId(),
                request.getApprovedBy(),
                request.getComment()
        );
        Approval savedApproval = approvalRepository.save(approval);
        ApprovalResponse response = new ApprovalResponse(
                savedApproval.getId(),
                savedApproval.getDeploymentId(),
                savedApproval.getApprovedBy(),
                savedApproval.getComment(),
                savedApproval.getApprovedAt()
        );
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ApprovalResponse getByDeploymentId(UUID deploymentId) {
        Approval approval = approvalRepository.findByDeploymentId(deploymentId)
                .orElseThrow(() -> new ApprovalNotFoundException("Approval not found"));
        ApprovalResponse response = new ApprovalResponse(
                approval.getId(),
                approval.getDeploymentId(),
                approval.getApprovedBy(),
                approval.getComment(),
                approval.getApprovedAt()
        );
        return response;
    }
}
