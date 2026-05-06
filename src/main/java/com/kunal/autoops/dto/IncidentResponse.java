package com.kunal.autoops.dto;

import com.kunal.autoops.entity.Incident;
import com.kunal.autoops.enums.IncidentSeverity;
import com.kunal.autoops.enums.IncidentStatus;

import java.time.Instant;

public class IncidentResponse {
    private Long id;
    private String serviceName;
    private String incidentType;
    private IncidentSeverity severity;
    private IncidentStatus status;
    private String description;
    private double riskScore;
    private String traceId;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant recoveredAt;

    public static IncidentResponse from(Incident incident) {
        IncidentResponse response = new IncidentResponse();
        response.id = incident.getId();
        response.serviceName = incident.getServiceName();
        response.incidentType = incident.getIncidentType();
        response.severity = incident.getSeverity();
        response.status = incident.getStatus();
        response.description = incident.getDescription();
        response.riskScore = incident.getRiskScore();
        response.traceId = incident.getTraceId();
        response.createdAt = incident.getCreatedAt();
        response.updatedAt = incident.getUpdatedAt();
        response.recoveredAt = incident.getRecoveredAt();
        return response;
    }

    public Long getId() { return id; }
    public String getServiceName() { return serviceName; }
    public String getIncidentType() { return incidentType; }
    public IncidentSeverity getSeverity() { return severity; }
    public IncidentStatus getStatus() { return status; }
    public String getDescription() { return description; }
    public double getRiskScore() { return riskScore; }
    public String getTraceId() { return traceId; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Instant getRecoveredAt() { return recoveredAt; }
}
