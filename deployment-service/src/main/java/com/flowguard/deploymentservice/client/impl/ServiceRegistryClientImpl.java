package com.flowguard.deploymentservice.client.impl;

import com.flowguard.deploymentservice.client.ServiceRegistryClient;
import com.flowguard.deploymentservice.client.model.ServiceInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;
import java.util.UUID;

@Service
public class ServiceRegistryClientImpl implements ServiceRegistryClient {

    private final WebClient webClient;
    private final String baseUrl;

    public ServiceRegistryClientImpl(
            WebClient webClient,
            @Value("${clients.service-registry.base-url}") String baseUrl) {
        this.webClient = webClient;
        this.baseUrl = baseUrl;
    }

    @Override
    public Optional<ServiceInfo> getService(UUID serviceId) {
        try{
            ServiceInfo service = webClient
                    .get()
                    .uri(baseUrl + "/services/{id}", serviceId)
                    .retrieve()
                    .bodyToMono(ServiceInfo.class)
                    .block();
            return Optional.ofNullable(service);
        }catch (WebClientResponseException e){
            if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                return Optional.empty();
            }
            throw e;
        }
    }
}
