package com.flowguard.serviceregistry.event.model;

import java.time.Instant;
import java.util.UUID;

public class ServiceLifecycleEventV1 {

    private UUID eventId;
    private ServiceLifecycleEventType eventType;
    private String serviceId;
    private String serviceName;
    private String status;
    private Instant occurredAt;

    public ServiceLifecycleEventV1() {}

    public UUID getEventId() {return eventId;}
    public void setEventId(UUID eventId) {this.eventId = eventId;}
    public ServiceLifecycleEventType getEventType() {return eventType;}
    public void setEventType(ServiceLifecycleEventType eventType) {this.eventType = eventType;}
    public String getServiceId() {return serviceId;}
    public void setServiceId(String serviceId) {this.serviceId = serviceId;}
    public String getServiceName() {return serviceName;}
    public void setServiceName(String serviceName) {this.serviceName = serviceName;}
    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
    public Instant getOccurredAt() {return occurredAt;}
    public void setOccurredAt(Instant occurredAt) {this.occurredAt = occurredAt;}

}
