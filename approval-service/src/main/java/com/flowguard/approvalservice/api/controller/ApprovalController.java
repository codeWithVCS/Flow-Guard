package com.flowguard.approvalservice.api.controller;

import com.flowguard.approvalservice.api.dto.request.CreateApprovalRequest;
import com.flowguard.approvalservice.api.dto.response.ApprovalResponse;
import com.flowguard.approvalservice.service.ApprovalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/approvals")
public class ApprovalController {

    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @PostMapping
    public ResponseEntity<ApprovalResponse> createApproval(@Valid @RequestBody CreateApprovalRequest request){
        ApprovalResponse response = approvalService.approve(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/deployments/{deploymentId}")
    public ResponseEntity<ApprovalResponse> getByDeploymentId(@PathVariable("deploymentId") UUID deploymentId){
        ApprovalResponse response = approvalService.getByDeploymentId(deploymentId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
