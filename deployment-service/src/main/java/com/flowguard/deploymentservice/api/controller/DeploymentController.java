package com.flowguard.deploymentservice.api.controller;

import com.flowguard.deploymentservice.api.dto.request.CreateDeploymentRequest;
import com.flowguard.deploymentservice.api.dto.response.DeploymentResponse;
import com.flowguard.deploymentservice.api.dto.response.DeploymentSummaryResponse;
import com.flowguard.deploymentservice.domain.Environment;
import com.flowguard.deploymentservice.service.DeploymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/deployments")
public class DeploymentController {

    private final DeploymentService deploymentService;

    public DeploymentController(DeploymentService deploymentService) {
        this.deploymentService = deploymentService;
    }

    @PostMapping
    public ResponseEntity<DeploymentResponse> create(
            @Valid @RequestBody CreateDeploymentRequest request) {
        DeploymentResponse response = deploymentService.deploy(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/latest")
    public ResponseEntity<DeploymentResponse> getLatest(
            @RequestParam UUID serviceId,
            @RequestParam Environment environment) {
        return ResponseEntity.ok(
                deploymentService.getLatest(serviceId, environment)
        );
    }

    @GetMapping
    public ResponseEntity<List<DeploymentSummaryResponse>> list(
            @RequestParam UUID serviceId,
            @RequestParam(required = false) Environment environment) {
        return ResponseEntity.ok(
                deploymentService.list(serviceId, environment)
        );
    }
}
