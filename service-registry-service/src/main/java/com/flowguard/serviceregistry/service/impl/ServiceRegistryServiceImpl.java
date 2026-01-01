package com.flowguard.serviceregistry.service.impl;

import com.flowguard.serviceregistry.api.dto.request.CreateServiceRequest;
import com.flowguard.serviceregistry.api.dto.response.ServiceResponse;
import com.flowguard.serviceregistry.api.dto.response.ServiceSummaryResponse;
import com.flowguard.serviceregistry.domain.ServiceStatus;
import com.flowguard.serviceregistry.event.model.ServiceLifecycleEventType;
import com.flowguard.serviceregistry.event.model.ServiceLifecycleEventV1;
import com.flowguard.serviceregistry.event.producer.ServiceLifecycleEventProducer;
import com.flowguard.serviceregistry.exception.InvalidServiceStateException;
import com.flowguard.serviceregistry.exception.ServiceAlreadyExistsException;
import com.flowguard.serviceregistry.exception.ServiceNotFoundException;
import com.flowguard.serviceregistry.repository.ServiceRepository;
import com.flowguard.serviceregistry.service.ServiceRegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ServiceRegistryServiceImpl implements ServiceRegistryService {

    private static final Logger log =
            LoggerFactory.getLogger(ServiceRegistryServiceImpl.class);

    private final ServiceRepository serviceRepository;
    private final ServiceLifecycleEventProducer eventProducer;

    public ServiceRegistryServiceImpl(
            ServiceRepository serviceRepository,
            ServiceLifecycleEventProducer eventProducer
    ) {
        this.serviceRepository = serviceRepository;
        this.eventProducer = eventProducer;
    }

    @Override
    public ServiceResponse register(CreateServiceRequest request) {

        if (serviceRepository.existsByServiceNameIgnoreCase(request.getServiceName())) {
            throw new ServiceAlreadyExistsException("Service already exists");
        }

        com.flowguard.serviceregistry.domain.Service service =
                new com.flowguard.serviceregistry.domain.Service(
                        request.getServiceName(),
                        request.getOwnerTeam(),
                        request.getCriticality()
                );

        com.flowguard.serviceregistry.domain.Service savedService =
                serviceRepository.save(service);

        try {
            ServiceLifecycleEventV1 event = new ServiceLifecycleEventV1();
            event.setEventId(UUID.randomUUID());
            event.setEventType(ServiceLifecycleEventType.REGISTERED);
            event.setServiceId(savedService.getId().toString());
            event.setServiceName(savedService.getServiceName());
            event.setStatus(savedService.getStatus().name());
            event.setOccurredAt(Instant.now());

            eventProducer.publish(event);
        } catch (Exception ex) {
            log.error(
                    "Failed to publish REGISTERED event for serviceId={}",
                    savedService.getId(),
                    ex
            );
        }

        return new ServiceResponse(
                savedService.getId(),
                savedService.getServiceName(),
                savedService.getOwnerTeam(),
                savedService.getCriticality(),
                savedService.getStatus(),
                savedService.getCreatedAt(),
                savedService.getUpdatedAt()
        );
    }

    @Override
    public ServiceResponse getById(UUID id) {

        com.flowguard.serviceregistry.domain.Service service =
                serviceRepository.findById(id)
                        .orElseThrow(() -> new ServiceNotFoundException("Service not found"));

        return new ServiceResponse(
                service.getId(),
                service.getServiceName(),
                service.getOwnerTeam(),
                service.getCriticality(),
                service.getStatus(),
                service.getCreatedAt(),
                service.getUpdatedAt()
        );
    }

    @Override
    public ServiceResponse getByName(String serviceName) {

        com.flowguard.serviceregistry.domain.Service service =
                serviceRepository.findByServiceNameIgnoreCase(serviceName)
                        .orElseThrow(() -> new ServiceNotFoundException("Service not found"));

        return new ServiceResponse(
                service.getId(),
                service.getServiceName(),
                service.getOwnerTeam(),
                service.getCriticality(),
                service.getStatus(),
                service.getCreatedAt(),
                service.getUpdatedAt()
        );
    }

    @Override
    public List<ServiceSummaryResponse> listByStatus(ServiceStatus status) {

        List<com.flowguard.serviceregistry.domain.Service> services =
                serviceRepository.findAllByStatus(status);

        List<ServiceSummaryResponse> responseList = new ArrayList<>();

        for (com.flowguard.serviceregistry.domain.Service service : services) {
            responseList.add(
                    new ServiceSummaryResponse(
                            service.getId(),
                            service.getServiceName(),
                            service.getOwnerTeam(),
                            service.getCriticality(),
                            service.getStatus()
                    )
            );
        }

        return responseList;
    }

    @Override
    public void deprecate(UUID id) {

        com.flowguard.serviceregistry.domain.Service service =
                serviceRepository.findById(id)
                        .orElseThrow(() -> new ServiceNotFoundException("Service not found"));

        try {
            service.deprecate();
        } catch (IllegalStateException ex) {
            throw new InvalidServiceStateException(ex.getMessage());
        }

        com.flowguard.serviceregistry.domain.Service updatedService =
                serviceRepository.save(service);

        try {
            ServiceLifecycleEventV1 event = new ServiceLifecycleEventV1();
            event.setEventId(UUID.randomUUID());
            event.setEventType(ServiceLifecycleEventType.DEPRECATED);
            event.setServiceId(updatedService.getId().toString());
            event.setServiceName(updatedService.getServiceName());
            event.setStatus(updatedService.getStatus().name());
            event.setOccurredAt(Instant.now());

            eventProducer.publish(event);
        } catch (Exception ex) {
            log.error(
                    "Failed to publish DEPRECATED event for serviceId={}",
                    updatedService.getId(),
                    ex
            );
        }
    }
}
