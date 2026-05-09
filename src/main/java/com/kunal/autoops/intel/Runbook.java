package com.kunal.autoops.intel;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "runbook")
public class Runbook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String serviceName;
    private String incidentType;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String recommendedActions;

    @Column(columnDefinition = "TEXT")
    private String riskNotes;

    @Column(columnDefinition = "TEXT")
    private String embedding;

    private Instant createdAt = Instant.now();

    public Runbook() {}

    // getters and setters omitted for brevity
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getIncidentType() { return incidentType; }
    public void setIncidentType(String incidentType) { this.incidentType = incidentType; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getRecommendedActions() { return recommendedActions; }
    public void setRecommendedActions(String recommendedActions) { this.recommendedActions = recommendedActions; }
    public String getRiskNotes() { return riskNotes; }
    public void setRiskNotes(String riskNotes) { this.riskNotes = riskNotes; }
    public String getEmbedding() { return embedding; }
    public void setEmbedding(String embedding) { this.embedding = embedding; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
