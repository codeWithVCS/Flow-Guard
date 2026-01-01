package com.flowguard.changeservice.service.impl;

import com.flowguard.changeservice.api.dto.request.CreateChangeRequest;
import com.flowguard.changeservice.api.dto.response.ChangeResponse;
import com.flowguard.changeservice.api.dto.response.ChangeSummaryResponse;
import com.flowguard.changeservice.client.ServiceRegistryClient;
import com.flowguard.changeservice.client.model.ServiceInfo;
import com.flowguard.changeservice.client.model.ServiceStatus;
import com.flowguard.changeservice.domain.Change;
import com.flowguard.changeservice.exception.ChangeAlreadyExistsException;
import com.flowguard.changeservice.exception.ChangeNotFoundException;
import com.flowguard.changeservice.exception.InvalidServiceReferenceException;
import com.flowguard.changeservice.exception.ServiceDeprecatedException;
import com.flowguard.changeservice.repository.ChangeRepository;
import com.flowguard.changeservice.service.ChangeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ChangeServiceImpl implements ChangeService {

    private final ChangeRepository changeRepository;
    private final ServiceRegistryClient serviceRegistryClient;

    public ChangeServiceImpl(ChangeRepository changeRepository, ServiceRegistryClient serviceRegistryClient) {
        this.changeRepository = changeRepository;
        this.serviceRegistryClient = serviceRegistryClient;
    }

    @Override
    public ChangeResponse create(CreateChangeRequest request) {
        ServiceInfo service = serviceRegistryClient.getService(request.getServiceId())
                .orElseThrow(() -> new InvalidServiceReferenceException("Service not found"));
        if(service.getStatus() == ServiceStatus.DEPRECATED){
            throw new ServiceDeprecatedException("Service is deprecated");
        }
        if(changeRepository.existsByServiceIdAndReferenceTypeAndReferenceId(
                request.getServiceId(),
                request.getReferenceType(),
                request.getReferenceId()
        )){
            throw new ChangeAlreadyExistsException("Change already exists");
        }
        Change change = new Change(
                request.getServiceId(),
                request.getChangeType(),
                request.getSummary(),
                request.getReferenceType(),
                request.getReferenceId(),
                request.getDescription(),
                request.getAuthor()
        );
        Change savedChange = changeRepository.save(change);
        ChangeResponse response = new ChangeResponse(
                savedChange.getId(),
                savedChange.getServiceId(),
                savedChange.getChangeType(),
                savedChange.getSummary(),
                savedChange.getDescription(),
                savedChange.getReferenceType(),
                savedChange.getReferenceId(),
                savedChange.getAuthor(),
                savedChange.getCreatedAt()
        );
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ChangeResponse getById(UUID id) {
        Change change = changeRepository.findById(id)
                .orElseThrow(() -> new ChangeNotFoundException("Change not found"));
        ChangeResponse response = new ChangeResponse(
                change.getId(),
                change.getServiceId(),
                change.getChangeType(),
                change.getSummary(),
                change.getDescription(),
                change.getReferenceType(),
                change.getReferenceId(),
                change.getAuthor(),
                change.getCreatedAt()
        );
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChangeSummaryResponse> getByServiceId(UUID serviceId) {
        ServiceInfo serviceInfo = serviceRegistryClient.getService(serviceId)
                .orElseThrow(() -> new InvalidServiceReferenceException("Service not found"));
        List<Change> changes = changeRepository.findByServiceIdOrderByCreatedAtDesc(serviceId);
        List<ChangeSummaryResponse> responses = new ArrayList<>();
        for (Change change : changes) {
            responses.add(
                    new ChangeSummaryResponse(
                            change.getId(),
                            change.getChangeType(),
                            change.getSummary(),
                            change.getReferenceType(),
                            change.getReferenceId(),
                            change.getAuthor(),
                            change.getCreatedAt()
                    )
            );
        }
        return responses;
    }
}
