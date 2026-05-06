package com.kunal.autoops.tools;

public class McpToolResult {
    private final boolean success;
    private final String output;

    public McpToolResult(boolean success, String output) {
        this.success = success;
        this.output = output;
    }

    public boolean isSuccess() { return success; }
    public String getOutput() { return output; }
}
