package com.kunal.autoops.dto;

import com.kunal.autoops.enums.RemediationTool;
import com.kunal.autoops.enums.ToolExecutionStatus;

public class ToolExecutionResponse {
    private Long actionId;
    private Long incidentId;
    private RemediationTool tool;
    private ToolExecutionStatus status;
    private double riskScore;
    private String message;
    private String output;
    private String traceId;

    public ToolExecutionResponse(Long actionId, Long incidentId, RemediationTool tool, ToolExecutionStatus status, double riskScore, String message, String output, String traceId) {
        this.actionId = actionId;
        this.incidentId = incidentId;
        this.tool = tool;
        this.status = status;
        this.riskScore = riskScore;
        this.message = message;
        this.output = output;
        this.traceId = traceId;
    }

    public Long getActionId() { return actionId; }
    public Long getIncidentId() { return incidentId; }
    public RemediationTool getTool() { return tool; }
    public ToolExecutionStatus getStatus() { return status; }
    public double getRiskScore() { return riskScore; }
    public String getMessage() { return message; }
    public String getOutput() { return output; }
    public String getTraceId() { return traceId; }
}
