package com.kunal.autoops.controller;

import com.kunal.autoops.dto.*;
import com.kunal.autoops.entity.AuditLog;
import com.kunal.autoops.service.AuditService;
import com.kunal.autoops.service.IncidentService;
import com.kunal.autoops.service.PlannerService;
import com.kunal.autoops.service.ToolExecutionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/incidents")
public class IncidentController {
    private final IncidentService incidentService;
    private final PlannerService plannerService;
    private final ToolExecutionService toolExecutionService;
    private final AuditService auditService;

    public IncidentController(IncidentService incidentService,
                              PlannerService plannerService,
                              ToolExecutionService toolExecutionService,
                              AuditService auditService) {
        this.incidentService = incidentService;
        this.plannerService = plannerService;
        this.toolExecutionService = toolExecutionService;
        this.auditService = auditService;
    }

    @PostMapping
    public IncidentResponse createIncident(@Valid @RequestBody CreateIncidentRequest request) {
        return incidentService.create(request);
    }

    @GetMapping
    public List<IncidentResponse> listIncidents() {
        return incidentService.findAll();
    }

    @GetMapping("/{id}")
    public IncidentResponse getIncident(@PathVariable Long id) {
        return incidentService.findOne(id);
    }

    @PostMapping("/{id}/plan")
    public PlanResponse plan(@PathVariable Long id) {
        return plannerService.plan(id);
    }

    @PostMapping("/{id}/tools/execute")
    public ToolExecutionResponse executeTool(@PathVariable Long id,
                                             @RequestHeader(value = "X-Role", defaultValue = "USER") String role,
                                             @Valid @RequestBody ToolExecutionRequest request) {
        return toolExecutionService.execute(id, request, role);
    }

    @GetMapping("/{id}/audit")
    public List<AuditLog> audit(@PathVariable Long id) {
        return auditService.findByIncident(id);
    }
}
