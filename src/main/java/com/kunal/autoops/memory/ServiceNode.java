package com.kunal.autoops.memory;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "service_node")
public class ServiceNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    private String ownerTeam;
    private String environment;
    private String criticality;
    private String currentSloTarget;
    private Instant createdAt = Instant.now();

    public ServiceNode() {}

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getOwnerTeam() { return ownerTeam; }
    public void setOwnerTeam(String ownerTeam) { this.ownerTeam = ownerTeam; }
    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }
    public String getCriticality() { return criticality; }
    public void setCriticality(String criticality) { this.criticality = criticality; }
    public String getCurrentSloTarget() { return currentSloTarget; }
    public void setCurrentSloTarget(String currentSloTarget) { this.currentSloTarget = currentSloTarget; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
