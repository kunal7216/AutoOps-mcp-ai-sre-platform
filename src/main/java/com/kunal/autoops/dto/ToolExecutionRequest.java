package com.kunal.autoops.dto;

import com.kunal.autoops.enums.RemediationTool;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public class ToolExecutionRequest {
    @NotNull
    private RemediationTool tool;
    private Map<String, String> parameters;

    public RemediationTool getTool() { return tool; }
    public void setTool(RemediationTool tool) { this.tool = tool; }
    public Map<String, String> getParameters() { return parameters; }
    public void setParameters(Map<String, String> parameters) { this.parameters = parameters; }
}
