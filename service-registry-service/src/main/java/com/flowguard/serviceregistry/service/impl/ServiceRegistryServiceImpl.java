package com.flowguard.serviceregistry.service.impl;

import com.flowguard.serviceregistry.api.dto.request.CreateServiceRequest;
import com.flowguard.serviceregistry.api.dto.response.ServiceResponse;
import com.flowguard.serviceregistry.api.dto.response.ServiceSummaryResponse;
import com.flowguard.serviceregistry.domain.ServiceStatus;
import com.flowguard.serviceregistry.exception.InvalidServiceStateException;
import com.flowguard.serviceregistry.exception.ServiceAlreadyExistsException;
import com.flowguard.serviceregistry.exception.ServiceDeprecatedException;
import com.flowguard.serviceregistry.exception.ServiceNotFoundException;
import com.flowguard.serviceregistry.repository.ServiceRepository;
import com.flowguard.serviceregistry.service.ServiceRegistryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ServiceRegistryServiceImpl implements ServiceRegistryService {

    private final ServiceRepository serviceRepository;

    public ServiceRegistryServiceImpl(ServiceRepository serviceRepository){
        this.serviceRepository = serviceRepository;
    }

    @Override
    public ServiceResponse register(CreateServiceRequest request) {
        if(serviceRepository.existsByServiceNameIgnoreCase(request.getServiceName())){
            throw  new ServiceAlreadyExistsException("Service already exists");
        }
        com.flowguard.serviceregistry.domain.Service service = new com.flowguard.serviceregistry.domain.Service(
                request.getServiceName(),
                request.getOwnerTeam(),
                request.getCriticality()
        );

        com.flowguard.serviceregistry.domain.Service savedService = serviceRepository.save(service);

        ServiceResponse response = new ServiceResponse(
                savedService.getId(),
                savedService.getServiceName(),
                savedService.getOwnerTeam(),
                savedService.getCriticality(),
                savedService.getStatus(),
                savedService.getCreatedAt(),
                savedService.getUpdatedAt()
        );
        return response;
    }

    @Override
    public ServiceResponse getById(UUID id) {
        com.flowguard.serviceregistry.domain.Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found"));
        ServiceResponse response = new ServiceResponse(
                service.getId(),
                service.getServiceName(),
                service.getOwnerTeam(),
                service.getCriticality(),
                service.getStatus(),
                service.getCreatedAt(),
                service.getUpdatedAt()
        );
        return response;
    }

    @Override
    public ServiceResponse getByName(String serviceName) {
        com.flowguard.serviceregistry.domain.Service service = serviceRepository.findByServiceNameIgnoreCase(serviceName)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found"));
        ServiceResponse response = new ServiceResponse(
                service.getId(),
                service.getServiceName(),
                service.getOwnerTeam(),
                service.getCriticality(),
                service.getStatus(),
                service.getCreatedAt(),
                service.getUpdatedAt()
        );
        return response;
    }

    @Override
    public List<ServiceSummaryResponse> listByStatus(ServiceStatus status) {
        List<com.flowguard.serviceregistry.domain.Service> services = serviceRepository.findAllByStatus(status);
        List<ServiceSummaryResponse> responseList = new ArrayList<>();
        for(com.flowguard.serviceregistry.domain.Service service : services){
            responseList.add(new ServiceSummaryResponse(
                    service.getId(),
                    service.getServiceName(),
                    service.getOwnerTeam(),
                    service.getCriticality(),
                    service.getStatus()
            ));
        }
        return responseList;
    }

    @Override
    public void deprecate(UUID id) {
        com.flowguard.serviceregistry.domain.Service service =  serviceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found"));
        try{
            service.deprecate();
        }catch (IllegalStateException ex){
            throw new InvalidServiceStateException(ex.getMessage());
        }
    }
}
