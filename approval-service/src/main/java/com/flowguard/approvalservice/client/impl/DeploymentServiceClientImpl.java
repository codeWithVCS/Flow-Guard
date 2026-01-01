package com.flowguard.approvalservice.client.impl;

import com.flowguard.approvalservice.client.DeploymentServiceClient;
import com.flowguard.approvalservice.client.model.DeploymentInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;
import java.util.UUID;

@Service
public class DeploymentServiceClientImpl implements DeploymentServiceClient {

    private final WebClient webClient;
    private final String baseUrl;

    public DeploymentServiceClientImpl(
            WebClient webClient,
            @Value("${clients.deployment-service.base-url}") String baseUrl
    ){
        this.webClient = webClient;
        this.baseUrl = baseUrl;
    }

    @Override
    public Optional<DeploymentInfo> getDeployment(UUID deploymentId) {
        try{
            DeploymentInfo deployment = webClient
                    .get()
                    .uri(baseUrl + "/deployments/{id}", deploymentId)
                    .retrieve()
                    .bodyToMono(DeploymentInfo.class)
                    .block();
            return Optional.of(deployment);
        }catch (WebClientResponseException e){
            if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                return Optional.empty();
            }
            throw e;
        }
    }

}
