package com.kunal.autoops.service;

import com.kunal.autoops.dto.PlanResponse;
import com.kunal.autoops.entity.Incident;
import com.kunal.autoops.enums.IncidentStatus;
import com.kunal.autoops.enums.RemediationTool;
import com.kunal.autoops.exception.ResourceNotFoundException;
import com.kunal.autoops.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlannerService {
    private final IncidentRepository incidentRepository;
    private final RiskScoringService riskScoringService;
    private final AuditService auditService;
    private final com.kunal.autoops.memory.ReliabilityMemoryGraphService reliabilityMemoryGraphService;
    private final com.kunal.autoops.intel.IncidentIntelligenceService incidentIntelligenceService;

    public PlannerService(IncidentRepository incidentRepository, RiskScoringService riskScoringService, AuditService auditService, com.kunal.autoops.memory.ReliabilityMemoryGraphService reliabilityMemoryGraphService, com.kunal.autoops.intel.IncidentIntelligenceService incidentIntelligenceService) {
        this.incidentRepository = incidentRepository;
        this.riskScoringService = riskScoringService;
        this.auditService = auditService;
        this.reliabilityMemoryGraphService = reliabilityMemoryGraphService;
        this.incidentIntelligenceService = incidentIntelligenceService;
    }

    public PlanResponse plan(Long incidentId) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found: " + incidentId));

        double risk = riskScoringService.calculateIncidentRisk(incident);
        incident.setRiskScore(risk);
        incident.setStatus(risk >= 0.75 ? IncidentStatus.APPROVAL_REQUIRED : IncidentStatus.PLANNED);
        incidentRepository.save(incident);

        // Build memory and RAG context (feature-flag guarded elsewhere)
        String memoryContext = reliabilityMemoryGraphService.buildMemoryContextForPlanner(incident);
        String ragContext = incidentIntelligenceService.buildContextForPlanner(incident);

        List<RemediationTool> tools = recommendTools(incident);
        String rationale = buildRationale(incident, risk, tools) + "; memoryContext=" + memoryContext + "; ragContext=" + ragContext;
        auditService.record(incident.getId(), "PLAN_CREATED", "PLANNER_AGENT", incident.getTraceId(), rationale);

        return new PlanResponse(incident.getId(), incident.getIncidentType(), risk, tools, rationale, incident.getTraceId());
    }

    private List<RemediationTool> recommendTools(Incident incident) {
        String type = incident.getIncidentType().toLowerCase();
        List<RemediationTool> tools = new ArrayList<>();
        tools.add(RemediationTool.HEALTH_CHECK);

        if (type.contains("latency") || type.contains("traffic")) {
            tools.add(RemediationTool.ADAPTIVE_RATE_LIMIT);
            tools.add(RemediationTool.SCALE_WORKERS);
        } else if (type.contains("cache")) {
            tools.add(RemediationTool.EVICT_CACHE);
            tools.add(RemediationTool.HEALTH_CHECK);
        } else if (type.contains("deploy") || type.contains("release")) {
            tools.add(RemediationTool.ROLLBACK_DEPLOYMENT);
        } else {
            tools.add(RemediationTool.RESTART_SERVICE);
        }

        tools.add(RemediationTool.VALIDATE_RECOVERY);
        return tools;
    }

    private String buildRationale(Incident incident, double risk, List<RemediationTool> tools) {
        return "Planner classified " + incident.getServiceName() + " as " + incident.getIncidentType()
                + " with risk=" + String.format("%.2f", risk) + ". Recommended tools=" + tools + ".";
    }
}
