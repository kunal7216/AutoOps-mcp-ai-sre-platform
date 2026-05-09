package com.kunal.autoops.safety;

import com.kunal.autoops.entity.Incident;
import com.kunal.autoops.entity.RemediationAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ToolExecutionSafetyLayer {
    private final ApprovalWorkflowService approvalService;

    private final double highRiskThreshold;
    private final boolean enabled;

    public ToolExecutionSafetyLayer(ApprovalWorkflowService approvalService,
                                    @Value("${app.policy.high-risk-threshold:0.75}") double highRiskThreshold,
                                    @Value("${app.features.reliabilityControlPlane.enabled:false}") boolean enabled) {
        this.approvalService = approvalService;
        this.highRiskThreshold = highRiskThreshold;
        this.enabled = enabled;
    }

    public SafetyDecision evaluate(Incident incident, RemediationAction action) {
        if (!enabled) return new SafetyDecision(true, null, null);
        boolean approvalRequired = true; // default to safe behavior
        try {
            // attempt to read approvalRequired from the remediation action node if available
            approvalRequired = (Boolean) action.getClass().getMethod("getApprovalRequired").invoke(action);
        } catch (Exception ignored) {
            // entity might not have approvalRequired field; fall back to true
        }

        if (approvalRequired && action.getRiskScore() >= highRiskThreshold) {
            Long approvalId = approvalService.requestApproval(action);
            return new SafetyDecision(false, "APPROVAL_REQUESTED", approvalId);
        }
        return new SafetyDecision(true, null, null);
    }

    public static class SafetyDecision {
        private final boolean allowed;
        private final String message;
        private final Long approvalId;

        public SafetyDecision(boolean allowed, String message, Long approvalId) {
            this.allowed = allowed;
            this.message = message;
            this.approvalId = approvalId;
        }

        public boolean isAllowed() { return allowed; }
        public String getMessage() { return message; }
        public Long getApprovalId() { return approvalId; }
    }
}