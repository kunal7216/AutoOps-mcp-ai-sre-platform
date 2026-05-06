package com.kunal.autoops.entity;

import com.kunal.autoops.enums.RemediationTool;
import com.kunal.autoops.enums.ToolExecutionStatus;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "remediation_actions")
public class RemediationAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id", nullable = false)
    private Incident incident;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RemediationTool tool;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ToolExecutionStatus status;

    @Column(length = 2000)
    private String reason;

    @Column(length = 2000)
    private String inputPayload;

    @Column(length = 2000)
    private String outputPayload;

    private double riskScore;
    private String requestedByRole;
    private String traceId;
    private Instant createdAt;
    private Instant executedAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        if (status == null) status = ToolExecutionStatus.REQUESTED;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Incident getIncident() { return incident; }
    public void setIncident(Incident incident) { this.incident = incident; }
    public RemediationTool getTool() { return tool; }
    public void setTool(RemediationTool tool) { this.tool = tool; }
    public ToolExecutionStatus getStatus() { return status; }
    public void setStatus(ToolExecutionStatus status) { this.status = status; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getInputPayload() { return inputPayload; }
    public void setInputPayload(String inputPayload) { this.inputPayload = inputPayload; }
    public String getOutputPayload() { return outputPayload; }
    public void setOutputPayload(String outputPayload) { this.outputPayload = outputPayload; }
    public double getRiskScore() { return riskScore; }
    public void setRiskScore(double riskScore) { this.riskScore = riskScore; }
    public String getRequestedByRole() { return requestedByRole; }
    public void setRequestedByRole(String requestedByRole) { this.requestedByRole = requestedByRole; }
    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getExecutedAt() { return executedAt; }
    public void setExecutedAt(Instant executedAt) { this.executedAt = executedAt; }
}
