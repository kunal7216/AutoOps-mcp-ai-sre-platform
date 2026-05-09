package com.kunal.autoops.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI autoOpsApi() {
        return new OpenAPI().info(new Info()
                .title("AutoOps X API")
                .version("2.0.0")
                .description("AutoOps X — Autonomous Reliability Control Plane: detects incidents, retrieves historical memory, plans safe remediation, and evaluates recovery."));
    }
}
