# AutoOps -- MCP-Based AI SRE Platform

AutoOps is a Spring Boot project that simulates a secure multi-agent incident recovery platform.
It demonstrates MCP-style tool interfaces, policy-constrained remediation, RBAC, risk scoring,
OpenTelemetry-style trace IDs, audit logs, MTTR tracking, and recovery validation.

## Tech Stack

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- H2 Database
- Swagger / OpenAPI
- Actuator
- MCP-style remediation tool server

## Run Locally

```bash
mvn spring-boot:run
```

Open Swagger:

```text
http://localhost:8080/swagger-ui.html
```

Open H2 console:

```text
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:autoops
Username: sa
Password: blank
```

## Main APIs

### 1. Create incident

```bash
curl -X POST http://localhost:8080/api/v1/incidents \
  -H "Content-Type: application/json" \
  -d '{
    "serviceName": "payment-service",
    "incidentType": "high latency",
    "severity": "HIGH",
    "description": "p95 latency crossed threshold"
  }'
```

### 2. Generate agent plan

```bash
curl -X POST http://localhost:8080/api/v1/incidents/1/plan
```

### 3. Execute MCP tool as privileged SRE

```bash
curl -X POST http://localhost:8080/api/v1/incidents/1/tools/execute \
  -H "Content-Type: application/json" \
  -H "X-Role: SRE" \
  -d '{
    "tool": "ADAPTIVE_RATE_LIMIT",
    "parameters": {
      "reductionPercent": "40"
    }
  }'
```

### 4. Validate recovery

```bash
curl -X POST http://localhost:8080/api/v1/incidents/1/tools/execute \
  -H "Content-Type: application/json" \
  -H "X-Role: SRE" \
  -d '{
    "tool": "VALIDATE_RECOVERY",
    "parameters": {}
  }'
```

### 5. View audit trail

```bash
curl http://localhost:8080/api/v1/incidents/1/audit
```

### 6. Dashboard

```bash
curl http://localhost:8080/api/v1/dashboard
```

## Resume Mapping

This code maps to the resume project:

**AutoOps -- MCP-Based Agentic Runtime Platform**

- Agentic runtime simulation
- MCP remediation tools
- RBAC and policy engine
- Risk scoring
- Audit logs
- Trace IDs
- MTTR dashboard
- Recovery validation

## Suggested Next Enhancements

- Replace simulated tools with Kubernetes API calls
- Add Kafka broker and real event consumers
- Add Redis cache controls
- Add JWT authentication
- Add React dashboard
- Add real OpenTelemetry collector integration
