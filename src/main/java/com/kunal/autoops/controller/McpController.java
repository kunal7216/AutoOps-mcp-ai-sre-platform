package com.kunal.autoops.controller;

import com.kunal.autoops.enums.RemediationTool;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/mcp")
public class McpController {
    @GetMapping("/tools")
    public List<Map<String, String>> listTools() {
        return Arrays.stream(RemediationTool.values())
                .map(tool -> Map.of(
                        "name", tool.name(),
                        "description", description(tool)
                ))
                .toList();
    }

    private String description(RemediationTool tool) {
        return switch (tool) {
            case HEALTH_CHECK -> "Inspect service health signals such as latency and error rate.";
            case RESTART_SERVICE -> "Safely restart an unhealthy service after policy approval.";
            case ROLLBACK_DEPLOYMENT -> "Rollback a service to a previous stable version.";
            case SCALE_WORKERS -> "Scale service workers/replicas for backlog or traffic spikes.";
            case EVICT_CACHE -> "Evict a cache namespace to clear stale data or memory pressure.";
            case ADAPTIVE_RATE_LIMIT -> "Reduce low-priority traffic during overload.";
            case VALIDATE_RECOVERY -> "Validate whether service metrics normalized after remediation.";
        };
    }
}
