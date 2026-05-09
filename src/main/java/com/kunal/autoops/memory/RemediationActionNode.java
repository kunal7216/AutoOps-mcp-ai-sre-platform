package com.kunal.autoops.memory;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "remediation_action_node")
public class RemediationActionNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long incidentId;
    private String actionType;
    private String toolName;
    private Double riskScore;
    private Boolean approvalRequired = Boolean.TRUE;
    private String executionStatus;
    private Boolean rollbackAvailable = Boolean.FALSE;
    private Instant createdAt = Instant.now();

    public RemediationActionNode() {}

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIncidentId() { return incidentId; }
    public void setIncidentId(Long incidentId) { this.incidentId = incidentId; }
    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }
    public String getToolName() { return toolName; }
    public void setToolName(String toolName) { this.toolName = toolName; }
    public Double getRiskScore() { return riskScore; }
    public void setRiskScore(Double riskScore) { this.riskScore = riskScore; }
    public Boolean getApprovalRequired() { return approvalRequired; }
    public void setApprovalRequired(Boolean approvalRequired) { this.approvalRequired = approvalRequired; }
    public String getExecutionStatus() { return executionStatus; }
    public void setExecutionStatus(String executionStatus) { this.executionStatus = executionStatus; }
    public Boolean getRollbackAvailable() { return rollbackAvailable; }
    public void setRollbackAvailable(Boolean rollbackAvailable) { this.rollbackAvailable = rollbackAvailable; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
