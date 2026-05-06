package com.kunal.autoops.dto;

import com.kunal.autoops.enums.RemediationTool;

import java.util.List;

public class PlanResponse {
    private Long incidentId;
    private String classification;
    private double riskScore;
    private List<RemediationTool> recommendedTools;
    private String rationale;
    private String traceId;

    public PlanResponse(Long incidentId, String classification, double riskScore, List<RemediationTool> recommendedTools, String rationale, String traceId) {
        this.incidentId = incidentId;
        this.classification = classification;
        this.riskScore = riskScore;
        this.recommendedTools = recommendedTools;
        this.rationale = rationale;
        this.traceId = traceId;
    }

    public Long getIncidentId() { return incidentId; }
    public String getClassification() { return classification; }
    public double getRiskScore() { return riskScore; }
    public List<RemediationTool> getRecommendedTools() { return recommendedTools; }
    public String getRationale() { return rationale; }
    public String getTraceId() { return traceId; }
}
