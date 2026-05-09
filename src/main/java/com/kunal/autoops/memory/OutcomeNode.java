package com.kunal.autoops.memory;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "outcome_node")
public class OutcomeNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long incidentId;
    private Long actionId;
    private Boolean success;
    private Long mttrSeconds;
    private Double latencyImprovementPercent;
    private Double errorRateImprovementPercent;
    private Boolean rollbackTriggered = Boolean.FALSE;

    @Column(columnDefinition = "TEXT")
    private String notes;
    private Instant createdAt = Instant.now();

    public OutcomeNode() {}

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIncidentId() { return incidentId; }
    public void setIncidentId(Long incidentId) { this.incidentId = incidentId; }
    public Long getActionId() { return actionId; }
    public void setActionId(Long actionId) { this.actionId = actionId; }
    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }
    public Long getMttrSeconds() { return mttrSeconds; }
    public void setMttrSeconds(Long mttrSeconds) { this.mttrSeconds = mttrSeconds; }
    public Double getLatencyImprovementPercent() { return latencyImprovementPercent; }
    public void setLatencyImprovementPercent(Double latencyImprovementPercent) { this.latencyImprovementPercent = latencyImprovementPercent; }
    public Double getErrorRateImprovementPercent() { return errorRateImprovementPercent; }
    public void setErrorRateImprovementPercent(Double errorRateImprovementPercent) { this.errorRateImprovementPercent = errorRateImprovementPercent; }
    public Boolean getRollbackTriggered() { return rollbackTriggered; }
    public void setRollbackTriggered(Boolean rollbackTriggered) { this.rollbackTriggered = rollbackTriggered; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
