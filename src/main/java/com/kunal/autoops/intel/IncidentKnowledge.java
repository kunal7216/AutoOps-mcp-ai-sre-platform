package com.kunal.autoops.intel;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "incident_knowledge")
public class IncidentKnowledge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long incidentId;
    private String serviceName;
    private String incidentType;

    @Column(columnDefinition = "TEXT")
    private String summary;

    private String rootCause;

    @Column(columnDefinition = "TEXT")
    private String actionTaken;

    @Column(columnDefinition = "TEXT")
    private String outcome;

    @Column(columnDefinition = "TEXT")
    private String embedding;

    private Instant createdAt = Instant.now();

    public IncidentKnowledge() {}

    // getters and setters omitted for brevity (add as needed)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIncidentId() { return incidentId; }
    public void setIncidentId(Long incidentId) { this.incidentId = incidentId; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getIncidentType() { return incidentType; }
    public void setIncidentType(String incidentType) { this.incidentType = incidentType; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getRootCause() { return rootCause; }
    public void setRootCause(String rootCause) { this.rootCause = rootCause; }
    public String getActionTaken() { return actionTaken; }
    public void setActionTaken(String actionTaken) { this.actionTaken = actionTaken; }
    public String getOutcome() { return outcome; }
    public void setOutcome(String outcome) { this.outcome = outcome; }
    public String getEmbedding() { return embedding; }
    public void setEmbedding(String embedding) { this.embedding = embedding; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
