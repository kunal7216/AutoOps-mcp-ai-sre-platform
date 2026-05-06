package com.kunal.autoops.service;

import com.kunal.autoops.entity.Incident;
import com.kunal.autoops.enums.IncidentSeverity;
import com.kunal.autoops.enums.RemediationTool;
import org.springframework.stereotype.Service;

@Service
public class RiskScoringService {
    public double calculateIncidentRisk(Incident incident) {
        double severityScore = switch (incident.getSeverity()) {
            case LOW -> 0.20;
            case MEDIUM -> 0.45;
            case HIGH -> 0.70;
            case CRITICAL -> 0.90;
        };
        double typeScore = incident.getIncidentType().toLowerCase().contains("database") ? 0.10 : 0.0;
        double paymentPenalty = incident.getServiceName().toLowerCase().contains("payment") ? 0.08 : 0.0;
        return Math.min(0.99, severityScore + typeScore + paymentPenalty);
    }

    public double calculateToolRisk(Incident incident, RemediationTool tool) {
        double base = calculateIncidentRisk(incident);
        double toolPenalty = switch (tool) {
            case HEALTH_CHECK, VALIDATE_RECOVERY -> -0.20;
            case EVICT_CACHE, ADAPTIVE_RATE_LIMIT -> -0.05;
            case SCALE_WORKERS -> 0.02;
            case RESTART_SERVICE -> 0.10;
            case ROLLBACK_DEPLOYMENT -> 0.15;
        };
        return Math.max(0.01, Math.min(0.99, base + toolPenalty));
    }
}
