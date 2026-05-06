package com.kunal.autoops.service;

import com.kunal.autoops.dto.ToolExecutionRequest;
import com.kunal.autoops.dto.ToolExecutionResponse;
import com.kunal.autoops.entity.Incident;
import com.kunal.autoops.entity.RemediationAction;
import com.kunal.autoops.enums.IncidentStatus;
import com.kunal.autoops.enums.ToolExecutionStatus;
import com.kunal.autoops.exception.ResourceNotFoundException;
import com.kunal.autoops.repository.IncidentRepository;
import com.kunal.autoops.repository.RemediationActionRepository;
import com.kunal.autoops.tools.McpRemediationToolServer;
import com.kunal.autoops.tools.McpToolResult;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ToolExecutionService {
    private final IncidentRepository incidentRepository;
    private final RemediationActionRepository actionRepository;
    private final RiskScoringService riskScoringService;
    private final PolicyEngine policyEngine;
    private final McpRemediationToolServer toolServer;
    private final AuditService auditService;

    public ToolExecutionService(IncidentRepository incidentRepository,
                                RemediationActionRepository actionRepository,
                                RiskScoringService riskScoringService,
                                PolicyEngine policyEngine,
                                McpRemediationToolServer toolServer,
                                AuditService auditService) {
        this.incidentRepository = incidentRepository;
        this.actionRepository = actionRepository;
        this.riskScoringService = riskScoringService;
        this.policyEngine = policyEngine;
        this.toolServer = toolServer;
        this.auditService = auditService;
    }

    public ToolExecutionResponse execute(Long incidentId, ToolExecutionRequest request, String role) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found: " + incidentId));

        double risk = riskScoringService.calculateToolRisk(incident, request.getTool());
        PolicyEngine.PolicyDecision decision = policyEngine.evaluate(incident, request.getTool(), risk, role);

        RemediationAction action = new RemediationAction();
        action.setIncident(incident);
        action.setTool(request.getTool());
        action.setRiskScore(risk);
        action.setRequestedByRole(role == null ? "USER" : role);
        action.setTraceId(incident.getTraceId());
        action.setInputPayload(String.valueOf(request.getParameters()));

        if (!decision.approved()) {
            action.setStatus(ToolExecutionStatus.DENIED);
            action.setReason(decision.reason());
            RemediationAction saved = actionRepository.save(action);
            incident.setStatus(IncidentStatus.BLOCKED);
            incidentRepository.save(incident);
            auditService.record(incidentId, "TOOL_DENIED", "POLICY_ENGINE", incident.getTraceId(), decision.reason());
            return new ToolExecutionResponse(saved.getId(), incidentId, request.getTool(), saved.getStatus(), risk, decision.reason(), null, incident.getTraceId());
        }

        action.setStatus(ToolExecutionStatus.APPROVED);
        action.setReason(decision.reason());
        actionRepository.save(action);
        auditService.record(incidentId, "TOOL_APPROVED", "POLICY_ENGINE", incident.getTraceId(), decision.reason());

        incident.setStatus(IncidentStatus.EXECUTING);
        incidentRepository.save(incident);

        McpToolResult result = toolServer.execute(request.getTool(), incident.getServiceName(), request.getParameters());
        action.setStatus(result.isSuccess() ? ToolExecutionStatus.EXECUTED : ToolExecutionStatus.FAILED);
        action.setOutputPayload(result.getOutput());
        action.setExecutedAt(Instant.now());
        actionRepository.save(action);

        if (request.getTool().name().equals("VALIDATE_RECOVERY") && result.isSuccess()) {
            incident.setStatus(IncidentStatus.RECOVERED);
            incident.setRecoveredAt(Instant.now());
        } else if (result.isSuccess()) {
            incident.setStatus(IncidentStatus.VALIDATED);
        } else {
            incident.setStatus(IncidentStatus.FAILED);
        }
        incidentRepository.save(incident);

        auditService.record(incidentId, "TOOL_EXECUTED", "EXECUTOR_AGENT", incident.getTraceId(), result.getOutput());
        return new ToolExecutionResponse(action.getId(), incidentId, request.getTool(), action.getStatus(), risk, decision.reason(), result.getOutput(), incident.getTraceId());
    }
}
