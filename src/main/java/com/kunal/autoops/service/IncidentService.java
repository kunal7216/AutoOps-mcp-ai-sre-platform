package com.kunal.autoops.service;

import com.kunal.autoops.dto.CreateIncidentRequest;
import com.kunal.autoops.dto.DashboardResponse;
import com.kunal.autoops.dto.IncidentResponse;
import com.kunal.autoops.entity.Incident;
import com.kunal.autoops.enums.IncidentStatus;
import com.kunal.autoops.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
public class IncidentService {
    private final IncidentRepository incidentRepository;
    private final RiskScoringService riskScoringService;
    private final AuditService auditService;

    public IncidentService(IncidentRepository incidentRepository, RiskScoringService riskScoringService, AuditService auditService) {
        this.incidentRepository = incidentRepository;
        this.riskScoringService = riskScoringService;
        this.auditService = auditService;
    }

    public IncidentResponse create(CreateIncidentRequest request) {
        Incident incident = new Incident();
        incident.setServiceName(request.getServiceName());
        incident.setIncidentType(request.getIncidentType());
        incident.setSeverity(request.getSeverity());
        incident.setDescription(request.getDescription());
        incident.setStatus(IncidentStatus.OPEN);
        incident.setTraceId(UUID.randomUUID().toString());
        incident.setRiskScore(riskScoringService.calculateIncidentRisk(incident));
        Incident saved = incidentRepository.save(incident);
        auditService.record(saved.getId(), "INCIDENT_CREATED", "SYSTEM", saved.getTraceId(), "Incident opened for service " + saved.getServiceName());
        return IncidentResponse.from(saved);
    }

    public List<IncidentResponse> findAll() {
        return incidentRepository.findAll().stream().map(IncidentResponse::from).toList();
    }

    public IncidentResponse findOne(Long id) {
        return IncidentResponse.from(incidentRepository.findById(id).orElseThrow(() -> new RuntimeException("Incident not found: " + id)));
    }

    public DashboardResponse dashboard() {
        List<Incident> incidents = incidentRepository.findAll();
        long total = incidents.size();
        long open = incidents.stream().filter(i -> i.getStatus() == IncidentStatus.OPEN || i.getStatus() == IncidentStatus.PLANNED || i.getStatus() == IncidentStatus.EXECUTING).count();
        long recovered = incidents.stream().filter(i -> i.getStatus() == IncidentStatus.RECOVERED || i.getStatus() == IncidentStatus.VALIDATED).count();
        long blocked = incidents.stream().filter(i -> i.getStatus() == IncidentStatus.BLOCKED).count();
        double mttr = incidents.stream()
                .filter(i -> i.getRecoveredAt() != null && i.getCreatedAt() != null)
                .mapToLong(i -> Duration.between(i.getCreatedAt(), i.getRecoveredAt()).toMinutes())
                .average().orElse(0.0);
        return new DashboardResponse(total, open, recovered, blocked, mttr);
    }
}
