package com.flowguard.deploymentservice.client.impl;

import com.flowguard.deploymentservice.client.ChangeServiceClient;
import com.flowguard.deploymentservice.client.model.ChangeInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;
import java.util.UUID;

@Service
public class ChangeServiceClientImpl implements ChangeServiceClient {

    private final WebClient webClient;
    private final String baseUrl;

    public ChangeServiceClientImpl(
            WebClient webClient,
            @Value("${clients.change-service.base-url}") String baseUrl){
        this.webClient = webClient;
        this.baseUrl = baseUrl;
    }

    @Override
    public Optional<ChangeInfo> getChange(UUID changeId) {
        try{
            ChangeInfo change = webClient
                    .get()
                    .uri(baseUrl + "/changes/{id}", changeId)
                    .retrieve()
                    .bodyToMono(ChangeInfo.class)
                    .block();
            return Optional.ofNullable(change);
        } catch (WebClientResponseException e) {
            if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                return Optional.empty();
            }
            throw e;
        }
    }
}
