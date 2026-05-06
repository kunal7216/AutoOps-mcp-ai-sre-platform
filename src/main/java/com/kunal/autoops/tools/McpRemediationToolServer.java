package com.kunal.autoops.tools;

import com.kunal.autoops.enums.RemediationTool;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;

@Component
public class McpRemediationToolServer {
    private final Random random = new Random();

    public McpToolResult execute(RemediationTool tool, String serviceName, Map<String, String> params) {
        return switch (tool) {
            case HEALTH_CHECK -> healthCheck(serviceName);
            case RESTART_SERVICE -> restartService(serviceName);
            case ROLLBACK_DEPLOYMENT -> rollbackDeployment(serviceName, params);
            case SCALE_WORKERS -> scaleWorkers(serviceName, params);
            case EVICT_CACHE -> evictCache(serviceName, params);
            case ADAPTIVE_RATE_LIMIT -> adaptiveRateLimit(serviceName, params);
            case VALIDATE_RECOVERY -> validateRecovery(serviceName);
        };
    }

    private McpToolResult healthCheck(String serviceName) {
        int latency = 60 + random.nextInt(250);
        int errorRate = random.nextInt(8);
        return new McpToolResult(true, "Health check completed for " + serviceName + ": p95=" + latency + "ms, errorRate=" + errorRate + "%");
    }

    private McpToolResult restartService(String serviceName) {
        return new McpToolResult(true, "Restart command simulated for service " + serviceName + ". New pod became healthy.");
    }

    private McpToolResult rollbackDeployment(String serviceName, Map<String, String> params) {
        String version = params == null ? "previous-stable" : params.getOrDefault("version", "previous-stable");
        return new McpToolResult(true, "Rollback simulated for " + serviceName + " to version " + version + ".");
    }

    private McpToolResult scaleWorkers(String serviceName, Map<String, String> params) {
        String replicas = params == null ? "3" : params.getOrDefault("replicas", "3");
        return new McpToolResult(true, "Scaled " + serviceName + " workers to " + replicas + " replicas.");
    }

    private McpToolResult evictCache(String serviceName, Map<String, String> params) {
        String cache = params == null ? serviceName + "-cache" : params.getOrDefault("cache", serviceName + "-cache");
        return new McpToolResult(true, "Evicted cache namespace " + cache + ".");
    }

    private McpToolResult adaptiveRateLimit(String serviceName, Map<String, String> params) {
        String percent = params == null ? "40" : params.getOrDefault("reductionPercent", "40");
        return new McpToolResult(true, "Adaptive rate limit enabled for " + serviceName + ": reduced low-priority traffic by " + percent + "%.");
    }

    private McpToolResult validateRecovery(String serviceName) {
        return new McpToolResult(true, "Recovery validation passed for " + serviceName + ": errors normalized, latency stable.");
    }
}
