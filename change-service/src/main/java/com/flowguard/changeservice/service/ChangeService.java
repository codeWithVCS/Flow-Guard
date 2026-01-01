package com.flowguard.changeservice.service;

import com.flowguard.changeservice.api.dto.request.CreateChangeRequest;
import com.flowguard.changeservice.api.dto.response.ChangeResponse;
import com.flowguard.changeservice.api.dto.response.ChangeSummaryResponse;

import java.util.List;
import java.util.UUID;

public interface ChangeService {

    ChangeResponse create(CreateChangeRequest request);

    ChangeResponse getById(UUID id);

    List<ChangeSummaryResponse> getByServiceId(UUID serviceId);

}
