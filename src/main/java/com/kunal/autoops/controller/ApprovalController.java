package com.kunal.autoops.controller;

import com.kunal.autoops.entity.RemediationAction;
import com.kunal.autoops.repository.RemediationActionRepository;
import com.kunal.autoops.repository.IncidentRepository;
import com.kunal.autoops.safety.ApprovalWorkflowService;
import com.kunal.autoops.service.AuditService;
import com.kunal.autoops.enums.ToolExecutionStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/approvals")
public class ApprovalController {
    private final ApprovalWorkflowService approvalService;
    private final RemediationActionRepository actionRepository;
    private final IncidentRepository incidentRepository;
    private final AuditService auditService;

    public ApprovalController(ApprovalWorkflowService approvalService, RemediationActionRepository actionRepository, IncidentRepository incidentRepository, AuditService auditService) {
        this.approvalService = approvalService;
        this.actionRepository = actionRepository;
        this.incidentRepository = incidentRepository;
        this.auditService = auditService;
    }

    @PostMapping("/{actionId}/approve")
    public ResponseEntity<String> approve(@PathVariable Long actionId, @RequestHeader(value = "X-Role", required = false) String role) {
        approvalService.approve(actionId);
        Optional<RemediationAction> aOpt = actionRepository.findById(actionId);
        if (aOpt.isPresent()) {
            RemediationAction action = aOpt.get();
            action.setStatus(ToolExecutionStatus.APPROVED);
            action.setReason("Approved by " + (role == null ? "UNKNOWN" : role));
            actionRepository.save(action);
            if (action.getIncident() != null) {
                var incident = action.getIncident();
                incident.setStatus(com.kunal.autoops.enums.IncidentStatus.PLANNED);
                incidentRepository.save(incident);
            }
            auditService.record(action.getIncident().getId(), "APPROVAL_GRANTED", "APPROVAL_CONTROLLER", action.getTraceId(), "Approved by " + (role == null ? "UNKNOWN" : role));
            return ResponseEntity.ok("approved");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{actionId}")
    public ResponseEntity<String> status(@PathVariable Long actionId) {
        boolean approved = approvalService.isApproved(actionId);
        Optional<RemediationAction> aOpt = actionRepository.findById(actionId);
        String status = aOpt.map(r -> r.getStatus().name()).orElse(approved ? "APPROVED" : "PENDING");
        return ResponseEntity.ok(status);
    }
}
