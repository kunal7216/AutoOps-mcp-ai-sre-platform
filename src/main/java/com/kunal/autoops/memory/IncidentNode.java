package com.kunal.autoops.memory;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "incident_node")
public class IncidentNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long serviceId;
    private String incidentType;
    private String severity;
    private Instant detectedAt;
    private String status;
    private Long latencyMs;
    private Double errorRate;
    private Long throughput;
    private Double sloBurnRate;
    private Double customerImpactScore;
    private Instant createdAt = Instant.now();

    public IncidentNode() {}

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }
    public String getIncidentType() { return incidentType; }
    public void setIncidentType(String incidentType) { this.incidentType = incidentType; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public Instant getDetectedAt() { return detectedAt; }
    public void setDetectedAt(Instant detectedAt) { this.detectedAt = detectedAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getLatencyMs() { return latencyMs; }
    public void setLatencyMs(Long latencyMs) { this.latencyMs = latencyMs; }
    public Double getErrorRate() { return errorRate; }
    public void setErrorRate(Double errorRate) { this.errorRate = errorRate; }
    public Long getThroughput() { return throughput; }
    public void setThroughput(Long throughput) { this.throughput = throughput; }
    public Double getSloBurnRate() { return sloBurnRate; }
    public void setSloBurnRate(Double sloBurnRate) { this.sloBurnRate = sloBurnRate; }
    public Double getCustomerImpactScore() { return customerImpactScore; }
    public void setCustomerImpactScore(Double customerImpactScore) { this.customerImpactScore = customerImpactScore; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
