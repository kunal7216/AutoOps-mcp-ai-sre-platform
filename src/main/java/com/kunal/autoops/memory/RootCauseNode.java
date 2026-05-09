package com.kunal.autoops.memory;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "root_cause_node")
public class RootCauseNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long incidentId;
    private String rootCauseType;
    private Double confidenceScore;

    @Column(columnDefinition = "TEXT")
    private String evidence;
    private String detectedBy;
    private Instant createdAt = Instant.now();

    public RootCauseNode() {}

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIncidentId() { return incidentId; }
    public void setIncidentId(Long incidentId) { this.incidentId = incidentId; }
    public String getRootCauseType() { return rootCauseType; }
    public void setRootCauseType(String rootCauseType) { this.rootCauseType = rootCauseType; }
    public Double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }
    public String getEvidence() { return evidence; }
    public void setEvidence(String evidence) { this.evidence = evidence; }
    public String getDetectedBy() { return detectedBy; }
    public void setDetectedBy(String detectedBy) { this.detectedBy = detectedBy; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
