package com.kunal.autoops.service;

import com.kunal.autoops.entity.Incident;
import com.kunal.autoops.enums.IncidentSeverity;
import com.kunal.autoops.enums.RemediationTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PolicyEngine {
    private final double highRiskThreshold;
    private static final Set<String> PRIVILEGED_ROLES = Set.of("SRE", "ADMIN", "MANAGER");

    public PolicyEngine(@Value("${app.policy.high-risk-threshold:0.75}") double highRiskThreshold) {
        this.highRiskThreshold = highRiskThreshold;
    }

    public PolicyDecision evaluate(Incident incident, RemediationTool tool, double riskScore, String requestedByRole) {
        String role = requestedByRole == null ? "USER" : requestedByRole.toUpperCase();

        if (tool == RemediationTool.ROLLBACK_DEPLOYMENT && !PRIVILEGED_ROLES.contains(role)) {
            return PolicyDecision.denied("Rollback requires SRE/ADMIN/MANAGER role.");
        }

        if (incident.getSeverity() == IncidentSeverity.CRITICAL && riskScore >= highRiskThreshold && !PRIVILEGED_ROLES.contains(role)) {
            return PolicyDecision.denied("Critical high-risk remediation requires privileged approval.");
        }

        if (tool == RemediationTool.RESTART_SERVICE && incident.getServiceName().toLowerCase().contains("payment") && !PRIVILEGED_ROLES.contains(role)) {
            return PolicyDecision.denied("Payment service restart requires privileged approval.");
        }

        return PolicyDecision.approved("Policy approved for role " + role + ".");
    }

    public record PolicyDecision(boolean approved, String reason) {
        public static PolicyDecision approved(String reason) { return new PolicyDecision(true, reason); }
        public static PolicyDecision denied(String reason) { return new PolicyDecision(false, reason); }
    }
}
