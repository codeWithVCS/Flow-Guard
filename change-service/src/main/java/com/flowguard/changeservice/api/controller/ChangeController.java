package com.flowguard.changeservice.api.controller;

import com.flowguard.changeservice.api.dto.request.CreateChangeRequest;
import com.flowguard.changeservice.api.dto.response.ChangeResponse;
import com.flowguard.changeservice.api.dto.response.ChangeSummaryResponse;
import com.flowguard.changeservice.service.ChangeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class ChangeController {

    public final ChangeService changeService;

    public ChangeController(ChangeService changeService) {
        this.changeService = changeService;
    }

    @PostMapping("/changes")
    public ResponseEntity<ChangeResponse> createChange(@Valid @RequestBody CreateChangeRequest request){
        ChangeResponse response = changeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/changes/{id}")
    public ResponseEntity<ChangeResponse> getChangeById(@PathVariable UUID id){
        ChangeResponse response = changeService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/services/{serviceId}/changes")
    public ResponseEntity<List<ChangeSummaryResponse>> getChangeByServiceId(@PathVariable UUID serviceId){
        List<ChangeSummaryResponse> changes = changeService.getByServiceId(serviceId);
        return ResponseEntity.status(HttpStatus.OK).body(changes);
    }

}
