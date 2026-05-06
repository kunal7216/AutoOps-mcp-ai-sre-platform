package com.kunal.autoops.controller;

import com.kunal.autoops.dto.DashboardResponse;
import com.kunal.autoops.service.IncidentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
    private final IncidentService incidentService;

    public DashboardController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping
    public DashboardResponse dashboard() {
        return incidentService.dashboard();
    }
}
