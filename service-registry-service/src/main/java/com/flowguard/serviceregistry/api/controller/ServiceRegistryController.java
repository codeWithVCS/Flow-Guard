package com.flowguard.serviceregistry.api.controller;

import com.flowguard.serviceregistry.api.dto.request.CreateServiceRequest;
import com.flowguard.serviceregistry.api.dto.response.ServiceResponse;
import com.flowguard.serviceregistry.api.dto.response.ServiceSummaryResponse;
import com.flowguard.serviceregistry.domain.ServiceStatus;
import com.flowguard.serviceregistry.service.ServiceRegistryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/services")
public class ServiceRegistryController {

    private final ServiceRegistryService serviceRegistryService;

    public ServiceRegistryController(ServiceRegistryService serviceRegistryService) {
        this.serviceRegistryService = serviceRegistryService;
    }

    @PostMapping
    public ResponseEntity<ServiceResponse> registerService(@Valid @RequestBody CreateServiceRequest request){
        ServiceResponse response = serviceRegistryService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getServiceById(@PathVariable UUID id){
        ServiceResponse response = serviceRegistryService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/by-name/{serviceName}")
    public ResponseEntity<ServiceResponse> getServiceByName(@PathVariable String serviceName){
        ServiceResponse response = serviceRegistryService.getByName(serviceName);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ServiceSummaryResponse>> getServices(@RequestParam ServiceStatus status){
        List<ServiceSummaryResponse> services = serviceRegistryService.listByStatus(status);
        return ResponseEntity.status(HttpStatus.OK).body(services);
    }

    @PatchMapping("/{id}/deprecate")
    public ResponseEntity<Void> deprecate(@PathVariable UUID id){
        serviceRegistryService.deprecate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
