package com.flowguard.changeservice.service.impl;

import com.flowguard.changeservice.api.dto.request.CreateChangeRequest;
import com.flowguard.changeservice.api.dto.response.ChangeResponse;
import com.flowguard.changeservice.api.dto.response.ChangeSummaryResponse;
import com.flowguard.changeservice.client.ServiceRegistryClient;
import com.flowguard.changeservice.client.model.ServiceInfo;
import com.flowguard.changeservice.client.model.ServiceStatus;
import com.flowguard.changeservice.domain.Change;
import com.flowguard.changeservice.event.model.ChangeRecordedEventV1;
import com.flowguard.changeservice.event.producer.ChangeRecordedEventProducer;
import com.flowguard.changeservice.exception.ChangeAlreadyExistsException;
import com.flowguard.changeservice.exception.ChangeNotFoundException;
import com.flowguard.changeservice.exception.InvalidServiceReferenceException;
import com.flowguard.changeservice.exception.ServiceDeprecatedException;
import com.flowguard.changeservice.repository.ChangeRepository;
import com.flowguard.changeservice.service.ChangeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ChangeServiceImpl implements ChangeService {

    private final ChangeRepository changeRepository;
    private final ServiceRegistryClient serviceRegistryClient;
    private final ChangeRecordedEventProducer eventProducer;

    public ChangeServiceImpl(
            ChangeRepository changeRepository,
            ServiceRegistryClient serviceRegistryClient,
            ChangeRecordedEventProducer eventProducer
    ) {
        this.changeRepository = changeRepository;
        this.serviceRegistryClient = serviceRegistryClient;
        this.eventProducer = eventProducer;
    }

    @Override
    public ChangeResponse create(CreateChangeRequest request) {

        ServiceInfo service = serviceRegistryClient.getService(request.getServiceId())
                .orElseThrow(() -> new InvalidServiceReferenceException("Service not found"));

        if (service.getStatus() == ServiceStatus.DEPRECATED) {
            throw new ServiceDeprecatedException("Service is deprecated");
        }

        if (changeRepository.existsByServiceIdAndReferenceTypeAndReferenceId(
                request.getServiceId(),
                request.getReferenceType(),
                request.getReferenceId()
        )) {
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

        // ---- Kafka Event Emission (Step 6.5) ----
        ChangeRecordedEventV1 event = new ChangeRecordedEventV1(
                UUID.randomUUID(),
                Instant.now(),
                savedChange.getId(),
                savedChange.getServiceId(),
                savedChange.getChangeType().name(),
                savedChange.getReferenceType().name(),
                savedChange.getReferenceId(),
                savedChange.getAuthor()
        );

        eventProducer.publish(
                savedChange.getId().toString(),
                event
        );
        // ----------------------------------------

        return new ChangeResponse(
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
    }

    @Override
    @Transactional(readOnly = true)
    public ChangeResponse getById(UUID id) {

        Change change = changeRepository.findById(id)
                .orElseThrow(() -> new ChangeNotFoundException("Change not found"));

        return new ChangeResponse(
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
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChangeSummaryResponse> getByServiceId(UUID serviceId) {

        serviceRegistryClient.getService(serviceId)
                .orElseThrow(() -> new InvalidServiceReferenceException("Service not found"));

        List<Change> changes =
                changeRepository.findByServiceIdOrderByCreatedAtDesc(serviceId);

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
