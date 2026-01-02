package com.flowguard.approvalservice.service.impl;

import com.flowguard.approvalservice.api.dto.request.CreateApprovalRequest;
import com.flowguard.approvalservice.api.dto.response.ApprovalResponse;
import com.flowguard.approvalservice.client.DeploymentServiceClient;
import com.flowguard.approvalservice.client.model.DeploymentInfo;
import com.flowguard.approvalservice.client.model.Environment;
import com.flowguard.approvalservice.domain.Approval;
import com.flowguard.approvalservice.event.model.DeploymentApprovedEventV1;
import com.flowguard.approvalservice.event.producer.DeploymentApprovedEventProducer;
import com.flowguard.approvalservice.exception.ApprovalAlreadyExistsException;
import com.flowguard.approvalservice.exception.ApprovalNotFoundException;
import com.flowguard.approvalservice.exception.ApprovalNotRequiredException;
import com.flowguard.approvalservice.exception.InvalidDeploymentReferenceException;
import com.flowguard.approvalservice.repository.ApprovalRepository;
import com.flowguard.approvalservice.service.ApprovalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final DeploymentServiceClient deploymentServiceClient;
    private final DeploymentApprovedEventProducer deploymentApprovedEventProducer;

    public ApprovalServiceImpl(
            ApprovalRepository approvalRepository,
            DeploymentServiceClient deploymentServiceClient,
            DeploymentApprovedEventProducer deploymentApprovedEventProducer
    ) {
        this.approvalRepository = approvalRepository;
        this.deploymentServiceClient = deploymentServiceClient;
        this.deploymentApprovedEventProducer = deploymentApprovedEventProducer;
    }

    @Override
    public ApprovalResponse approve(CreateApprovalRequest request) {

        DeploymentInfo deployment = deploymentServiceClient
                .getDeployment(request.getDeploymentId())
                .orElseThrow(() ->
                        new InvalidDeploymentReferenceException("Deployment not found"));

        if (deployment.getEnvironment() != Environment.PROD) {
            throw new ApprovalNotRequiredException(
                    "Approval is not required for this environment");
        }

        if (approvalRepository.existsByDeploymentId(deployment.getDeploymentId())) {
            throw new ApprovalAlreadyExistsException("Approval already exists");
        }

        Approval approval = new Approval(
                request.getDeploymentId(),
                request.getApprovedBy(),
                request.getComment()
        );

        Approval savedApproval = approvalRepository.save(approval);

        DeploymentApprovedEventV1 event = new DeploymentApprovedEventV1(
                UUID.randomUUID(),
                "DEPLOYMENT_APPROVED",
                "v1",
                Instant.now(),
                savedApproval.getDeploymentId(),
                deployment.getServiceId(),
                deployment.getEnvironment().name(),
                savedApproval.getApprovedBy(),
                savedApproval.getApprovedAt()
        );

        deploymentApprovedEventProducer.publish(event);

        return new ApprovalResponse(
                savedApproval.getId(),
                savedApproval.getDeploymentId(),
                savedApproval.getApprovedBy(),
                savedApproval.getComment(),
                savedApproval.getApprovedAt()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ApprovalResponse getByDeploymentId(UUID deploymentId) {

        Approval approval = approvalRepository.findByDeploymentId(deploymentId)
                .orElseThrow(() ->
                        new ApprovalNotFoundException("Approval not found"));

        return new ApprovalResponse(
                approval.getId(),
                approval.getDeploymentId(),
                approval.getApprovedBy(),
                approval.getComment(),
                approval.getApprovedAt()
        );
    }
}
