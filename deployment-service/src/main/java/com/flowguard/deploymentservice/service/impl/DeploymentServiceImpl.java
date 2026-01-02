package com.flowguard.deploymentservice.service.impl;

import com.flowguard.deploymentservice.api.dto.request.CreateDeploymentRequest;
import com.flowguard.deploymentservice.api.dto.response.DeploymentResponse;
import com.flowguard.deploymentservice.api.dto.response.DeploymentSummaryResponse;
import com.flowguard.deploymentservice.client.ChangeServiceClient;
import com.flowguard.deploymentservice.client.ServiceRegistryClient;
import com.flowguard.deploymentservice.client.model.ChangeInfo;
import com.flowguard.deploymentservice.client.model.ServiceCriticality;
import com.flowguard.deploymentservice.client.model.ServiceInfo;
import com.flowguard.deploymentservice.client.model.ServiceStatus;
import com.flowguard.deploymentservice.domain.Deployment;
import com.flowguard.deploymentservice.domain.Environment;
import com.flowguard.deploymentservice.event.model.DeploymentCreatedEventV1;
import com.flowguard.deploymentservice.event.model.DeploymentEventType;
import com.flowguard.deploymentservice.event.producer.DeploymentCreatedEventProducer;
import com.flowguard.deploymentservice.exception.ApprovalRequiredException;
import com.flowguard.deploymentservice.exception.DeploymentAlreadyExistsException;
import com.flowguard.deploymentservice.exception.DeploymentNotFoundException;
import com.flowguard.deploymentservice.exception.InvalidChangeReferenceException;
import com.flowguard.deploymentservice.exception.InvalidServiceReferenceException;
import com.flowguard.deploymentservice.exception.ServiceDeprecatedException;
import com.flowguard.deploymentservice.repository.DeploymentRepository;
import com.flowguard.deploymentservice.service.DeploymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class DeploymentServiceImpl implements DeploymentService {

    private final DeploymentRepository deploymentRepository;
    private final ServiceRegistryClient serviceRegistryClient;
    private final ChangeServiceClient changeServiceClient;
    private final DeploymentCreatedEventProducer eventProducer;

    public DeploymentServiceImpl(
            DeploymentRepository deploymentRepository,
            ServiceRegistryClient serviceRegistryClient,
            ChangeServiceClient changeServiceClient,
            DeploymentCreatedEventProducer eventProducer
    ) {
        this.deploymentRepository = deploymentRepository;
        this.serviceRegistryClient = serviceRegistryClient;
        this.changeServiceClient = changeServiceClient;
        this.eventProducer = eventProducer;
    }

    @Override
    public DeploymentResponse deploy(CreateDeploymentRequest request) {

        ServiceInfo serviceInfo = serviceRegistryClient
                .getService(request.getServiceId())
                .orElseThrow(() ->
                        new InvalidServiceReferenceException("Service not found"));

        if (serviceInfo.getStatus() == ServiceStatus.DEPRECATED) {
            throw new ServiceDeprecatedException("Service is deprecated");
        }

        if (request.getEnvironment() == Environment.PROD
                && serviceInfo.getCriticality() == ServiceCriticality.MISSION_CRITICAL) {
            throw new ApprovalRequiredException(
                    "Approval required for PROD deployment of mission critical service");
        }

        if (deploymentRepository.existsByServiceIdAndEnvironmentAndVersion(
                request.getServiceId(),
                request.getEnvironment(),
                request.getVersion())) {
            throw new DeploymentAlreadyExistsException("Deployment already exists for this version");
        }

        validateChanges(request.getServiceId(), request.getChangeIds());

        Deployment deployment = new Deployment(
                request.getServiceId(),
                request.getEnvironment(),
                request.getVersion(),
                request.getChangeIds(),
                request.getDeployedBy()
        );

        Deployment savedDeployment = deploymentRepository.save(deployment);

        DeploymentCreatedEventV1 event = new DeploymentCreatedEventV1(
                UUID.randomUUID(),
                DeploymentEventType.DEPLOYMENT_CREATED,
                "v1",
                Instant.now(),
                savedDeployment.getId(),
                savedDeployment.getServiceId(),
                savedDeployment.getEnvironment().name(),
                savedDeployment.getVersion()
        );

        eventProducer.publish(event);

        return mapToResponse(savedDeployment);
    }

    @Override
    @Transactional(readOnly = true)
    public DeploymentResponse getLatest(UUID serviceId, Environment environment) {

        Deployment deployment = deploymentRepository
                .findTopByServiceIdAndEnvironmentOrderByDeployedAtDesc(serviceId, environment)
                .orElseThrow(() ->
                        new DeploymentNotFoundException("No deployment found"));

        return mapToResponse(deployment);
    }

    @Override
    @Transactional(readOnly = true)
    public DeploymentResponse getById(UUID deploymentId) {

        Deployment deployment = deploymentRepository.findById(deploymentId)
                .orElseThrow(() ->
                        new DeploymentNotFoundException("Deployment not found"));

        return mapToResponse(deployment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeploymentSummaryResponse> list(UUID serviceId, Environment environment) {

        List<Deployment> deployments =
                environment == null
                        ? deploymentRepository.findByServiceIdOrderByDeployedAtDesc(serviceId)
                        : deploymentRepository.findByServiceIdAndEnvironmentOrderByDeployedAtDesc(
                        serviceId, environment);

        List<DeploymentSummaryResponse> responses = new ArrayList<>();
        for (Deployment deployment : deployments) {
            responses.add(
                    new DeploymentSummaryResponse(
                            deployment.getId(),
                            deployment.getEnvironment(),
                            deployment.getVersion(),
                            deployment.getDeployedAt()
                    )
            );
        }
        return responses;
    }

    private void validateChanges(UUID serviceId, Set<UUID> changeIds) {
        for (UUID changeId : changeIds) {
            ChangeInfo change = changeServiceClient
                    .getChange(changeId)
                    .orElseThrow(() ->
                            new InvalidChangeReferenceException("Change not found: " + changeId));

            if (!change.getServiceId().equals(serviceId)) {
                throw new InvalidChangeReferenceException(
                        "Change does not belong to the service: " + changeId);
            }
        }
    }

    private DeploymentResponse mapToResponse(Deployment deployment) {
        return new DeploymentResponse(
                deployment.getId(),
                deployment.getServiceId(),
                deployment.getEnvironment(),
                deployment.getVersion(),
                Set.copyOf(deployment.getChangeIds()),
                deployment.getDeployedAt(),
                deployment.getDeployedBy()
        );
    }
}
