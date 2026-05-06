AutoOps — MCP-Based AI SRE Platform
AutoOps is a production-style AI SRE automation platform built with Java, Spring Boot, MCP-style tool interfaces, Kafka-style workflows, RBAC, risk scoring, audit logging, OpenTelemetry-style trace IDs, SLO tracking, MTTR analysis, and recovery validation.
The project simulates how an enterprise reliability platform can use AI agents to detect service failures, classify incidents, plan remediation actions, execute approved recovery tools, and validate outcomes safely.
---
Table of Contents
Project Overview
Problem Statement
Core Idea
Key Features
Architecture
System Workflow
MCP Tool Layer
Policy Engine
Risk Scoring
Audit Logging
OpenTelemetry-Style Tracing
Tech Stack
Project Structure
API Endpoints
Running Locally
Running with Docker
Swagger UI
Future Enhancements
Interview Talking Points
---
Project Overview
AutoOps demonstrates a safe agentic runtime for SRE workflows. In modern distributed systems, incidents such as service crashes, latency spikes, cache failures, traffic overload, and deployment regressions require quick diagnosis and careful remediation.
AI agents can speed up incident response, but unrestricted AI-driven remediation is unsafe. AutoOps solves this by routing all agent actions through controlled MCP-style tools and policy checks.
The current system supports:
Incident creation
Agent-based remediation planning
Policy-constrained execution
MCP-style remediation tools
Audit logs
Trace IDs
Recovery validation
Dashboard metrics
---
Problem Statement
Production systems fail in many ways:
Services become unhealthy
Latency spikes under load
Cache layers degrade
Deployments require rollback
Error rates increase suddenly
Dependencies become slow
Manual response increases MTTR
A naive AI agent might suggest dangerous actions like restarting a critical service or modifying infrastructure without validation. AutoOps prevents this by introducing:
RBAC
Risk scoring
Approval gates
Tool allowlists
Rollback checks
Audit trails
Validation after execution
---
Core Idea
AutoOps follows this design principle:
> AI agents can recommend actions, but only approved tools can execute actions.
The system separates reasoning from execution:
```text
Incident → Planner Agent → Policy Engine → MCP Tool Server → Validator → Audit Log
```
This mirrors enterprise-grade AI platform design where governance, safety, and observability are required before autonomous actions are trusted.
---
Key Features
1. Incident Management
Users can create incidents using REST APIs. Each incident stores:
Service name
Severity
Description
Planned action
Policy decision
Status
Risk score
Trace ID
Created timestamp
Recovered timestamp
2. Planner Agent Simulation
The Planner Agent selects a remediation action based on incident context.
Example mappings:
Incident Signal	Planned Action
Latency spike	Enable adaptive rate limiting
Cache issue	Clear cache
Deployment failure	Rollback deployment
Generic service failure	Restart service
3. MCP-Style Remediation Tools
The project exposes tool-like remediation operations such as:
`restart_service`
`rollback_deployment`
`scale_service`
`clear_cache`
`enable_adaptive_rate_limit`
`health_check`
`validate_recovery`
4. Policy-Constrained Tool Execution
Before executing a tool, the Policy Engine checks:
Service criticality
Severity
Risk score
Restricted services
Approval requirement
Allowed actions
5. Audit Logs
Every planning, policy, execution, and validation event is logged with:
Actor
Action
Decision
Details
Incident ID
Trace ID
Timestamp
6. Dashboard Metrics
The dashboard summarizes:
Total incidents
Recovered incidents
Approval-required incidents
MTTR-related signals
Recovery success status
---
Architecture
```text
Client / Swagger
      |
      v
Incident Controller
      |
      v
Incident Service
      |
      +--> Planner Agent
      |
      +--> Policy Engine
      |
      +--> MCP Tool Service
      |
      +--> Recovery Validator
      |
      +--> Audit Log Repository
      |
      v
Dashboard Metrics
```
---
System Workflow
```text
1. Incident is created through REST API
2. Planner Agent classifies the incident
3. A remediation tool is selected
4. Policy Engine calculates risk
5. Action is approved, denied, or marked for approval
6. MCP Tool Service executes approved action
7. Recovery is validated
8. Audit logs and dashboard metrics are updated
```
---
MCP Tool Layer
MCP-style tools provide a standard interface between agents and backend operations. The important design point is that agents do not directly execute infrastructure actions.
Example:
```text
Agent requests: restart_service(payment-service)
Policy checks: RBAC + risk + rollback safety
Execution result: Approved / Denied / Approval Required
Audit log: Stored with trace ID
```
This pattern demonstrates safe tool orchestration for agentic AI systems.
---
Policy Engine
The Policy Engine returns one of three decisions:
Decision	Meaning
`APPROVED`	Tool can be executed automatically
`APPROVAL_REQUIRED`	Human approval is required
`DENIED`	Tool execution is blocked
Example restricted behavior:
```text
Service: database-service
Severity: HIGH
Decision: DENIED
Reason: Direct database remediation is restricted
```
---
Risk Scoring
Risk score is calculated from:
Incident severity
Service criticality
Payment/business context
Tool type
Restricted service checks
Example severity mapping:
Severity	Base Risk
CRITICAL	0.90
HIGH	0.70
MEDIUM	0.45
LOW	0.25
---
Audit Logging
Audit logs answer:
Who planned the action?
Which tool was selected?
Was the action approved?
Why was it denied or escalated?
Was recovery successful?
Which trace ID connects the full incident flow?
Example audit event:
```json
{
  "actor": "PolicyEngine",
  "action": "CHECK_POLICY",
  "decision": "APPROVED",
  "details": "Risk score below approval threshold",
  "traceId": "8f9c7d..."
}
```
---
OpenTelemetry-Style Tracing
Each incident gets a trace ID to correlate:
REST request
Planner decision
Policy decision
Tool execution
Recovery validation
Audit log records
This demonstrates observability thinking similar to OpenTelemetry-based production systems.
---
Tech Stack
Category	Technology
Language	Java 17
Framework	Spring Boot 3
API	REST APIs
Persistence	Spring Data JPA
Database	H2 / PostgreSQL-ready structure
API Docs	Swagger / Springdoc OpenAPI
Monitoring	Spring Boot Actuator
Architecture	MCP-style tool layer, AI agents, policy engine
DevOps	Docker, Docker Compose
---
Project Structure
```text
autoops-mcp-ai-sre-platform/
├── pom.xml
├── Dockerfile
├── docker-compose.yml
├── README.md
├── .gitignore
└── src/main/
    ├── java/com/kunal/autoops/
    │   ├── AutoOpsApplication.java
    │   ├── controller/
    │   ├── dto/
    │   ├── model/
    │   ├── repo/
    │   └── service/
    └── resources/
        └── application.properties
```
---
API Endpoints
Method	Endpoint	Description
POST	`/api/incidents`	Create and process incident
GET	`/api/incidents`	Fetch all incidents
GET	`/api/incidents/{id}/audit`	Fetch audit logs
GET	`/api/dashboard`	Fetch dashboard summary
---
Running Locally
```bash
git clone https://github.com/kunal7216/autoops-mcp-ai-sre-platform.git
cd autoops-mcp-ai-sre-platform
mvn spring-boot:run
```
Application URL:
```text
http://localhost:8080
```
---
Running with Docker
```bash
mvn clean package
docker build -t autoops-mcp-ai-sre-platform .
docker run -p 8080:8080 autoops-mcp-ai-sre-platform
```
Or:
```bash
docker compose up --build
```
---
Swagger UI
```text
http://localhost:8080/swagger-ui.html
```
---
Example Request
```bash
curl -X POST http://localhost:8080/api/incidents \
  -H "Content-Type: application/json" \
  -d '{
    "serviceName": "payment-service",
    "severity": "HIGH",
    "description": "Latency spike and error burst"
  }'
```
---
Future Enhancements
Real Kafka topics
Redis-backed workflow state
PostgreSQL persistence
Real OpenTelemetry SDK
JWT authentication
Human approval workflow
Kubernetes remediation tools
LLM-generated RCA reports
Grafana dashboard integration

Author
Kunal Kumar
