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

        boolean requiresApproval = false;
        String reason = "Policy approved for role " + role + ".";
        String suggestedMitigation = null;

        if (tool == RemediationTool.ROLLBACK_DEPLOYMENT && !PRIVILEGED_ROLES.contains(role)) {
            requiresApproval = true;
            reason = "Rollback requires SRE/ADMIN/MANAGER approval.";
            suggestedMitigation = "Open a rollback PR or request SRE approval.";
        }

        if (incident.getSeverity() == IncidentSeverity.CRITICAL && riskScore >= highRiskThreshold && !PRIVILEGED_ROLES.contains(role)) {
            requiresApproval = true;
            reason = "Critical high-risk remediation requires privileged approval.";
            suggestedMitigation = "Escalate to on-call SRE and consider guarded mitigation.";
        }

        if (tool == RemediationTool.RESTART_SERVICE && incident.getServiceName().toLowerCase().contains("payment") && !PRIVILEGED_ROLES.contains(role)) {
            requiresApproval = true;
            reason = "Payment service restart requires privileged approval.";
            suggestedMitigation = "Coordinate maintenance window with payments team.";
        }

        boolean approved = !requiresApproval;
        return new PolicyDecision(approved, reason, suggestedMitigation, requiresApproval, riskScore);
    }

    public static class PolicyDecision {
        private final boolean approved;
        private final String reason;
        private final String suggestedMitigation;
        private final boolean requiresApproval;
        private final double riskScore;

        public PolicyDecision(boolean approved, String reason, String suggestedMitigation, boolean requiresApproval, double riskScore) {
            this.approved = approved;
            this.reason = reason;
            this.suggestedMitigation = suggestedMitigation;
            this.requiresApproval = requiresApproval;
            this.riskScore = riskScore;
        }

        public boolean approved() { return approved; }
        public String reason() { return reason; }
        public String suggestedMitigation() { return suggestedMitigation; }
        public boolean requiresApproval() { return requiresApproval; }
        public double getRiskScore() { return riskScore; }

        public static PolicyDecision approved(String reason) { return new PolicyDecision(true, reason, null, false, 0.0); }
        public static PolicyDecision denied(String reason) { return new PolicyDecision(false, reason, null, false, 0.0); }
    }
}
