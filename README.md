# AutoOps X — Autonomous Reliability Control Plane (v2)

1. Overview

AutoOps X is an autonomous reliability control plane that detects incidents, reasons about root causes, retrieves historical reliability memory, plans safe remediation, executes approved tools, validates recovery, and continuously improves through replay-based evaluation.

2. Problem Statement

Production systems face failures (latency spikes, cache saturation, deployment regressions) where manual response increases MTTR and risky automated actions can cause outages. AutoOps X reduces risk by combining incident intelligence, policy controls, and safe execution.

3. Solution

AutoOps X ingests telemetry, detects incidents, uses RAG + Reliability Memory Graph to inform planning, runs policy checks and approvals, executes via MCP tool layer, validates outcome, and records memories for future decisions.

4. Key Features
- Incident detection and classification
- RAG-based incident intelligence and runbook retrieval
- Reliability Memory Graph (graph of services, incidents, root causes, actions, outcomes)
- Policy Engine with approval gates and risk scoring
- Tool Execution Safety Layer and approval workflow
- Validator agent and replay-based evaluation
- Observability (OpenTelemetry/Grafana)

5. System Architecture

```
Telemetry / Events -> Incident Detection -> Incident Intelligence (RAG)
 -> Reliability Memory Graph -> Planner -> Policy Engine -> Executor -> Validator
 -> Audit + Observability -> Replay Evaluation
```

6. Workflow

1. Telemetry/ingest
2. Incident detection
3. Build memory + RAG context
4. Planner proposes actions
5. Policy Engine decides APPROVED / APPROVAL_REQUIRED / DENIED
6. Approval workflow (if required)
7. Executor runs MCP tool (safe execution)
8. Validator verifies recovery and records outcome
9. Replay evaluates decision quality

7. Tech Stack
- Java, Spring Boot
- PostgreSQL (Flyway migrations), H2 for tests
- Kafka for events (scaffold)
- Redis for state/cache (scaffold)
- OpenTelemetry, Grafana for observability
- Docker, Kubernetes-ready

8. Project Modules
- incident-service: ingest + detection
- intel: RAG, embeddings, knowledge repo
- memory: Reliability Memory Graph entities + service
- planner: Planner Agent
- policy: Policy Engine
- executor: Tool execution + safety layer
- validator: Recovery validator
- replay: Replay evaluation and chaos simulation
- api: REST controllers and approval endpoints

9. API Endpoints (examples)
- POST /api/v1/incidents — create + process incident
- GET /api/v1/incidents — list incidents
- GET /api/v1/incidents/{id} — incident details
- POST /api/v1/approvals/{id}/approve — approve action

10. Database Design (high-level)
- service_node, incident_node, root_cause_node, remediation_action_node, outcome_node, memory_edge
- incident_knowledge, runbook, rca_report (for RAG)

11. Kafka / Redis / MCP / RAG Usage
- Kafka: event bus for telemetry and incident events
- Redis: ephemeral workflow state and caches
- MCP: tool interface for safe remediation tools
- RAG: retrieve similar incidents, runbooks, RCA reports for planner context

12. Safety and Reliability Design
- RBAC and tool allowlists
- Risk scoring and approval gates
- Dry-run / validate before committing irreversible changes
- Rollback availability and idempotent operations

13. How to Run Locally

```bash
git clone https://github.com/kunal7216/AutoOps--Autonomous-Reliability-Control-Plane.git
cd AutoOps--Autonomous-Reliability-Control-Plane
mvn clean package
mvn spring-boot:run
```

14. Screenshots / Examples
- Swagger UI: /swagger-ui.html
- Actuator endpoints: /actuator
- Dashboard (Grafana) - link when available

15. Testing
- Unit tests under src/test
- Integration: Testcontainers Postgres (requires Docker)
- Run: mvn test

16. Future Enhancements
- pgvector/Vector DB for embeddings
- Persistent approval store and UI
- Full replay/evaluation pipeline with historical telemetry
- Kubernetes operator for deployment

17. Resume / Interview Highlights
- Upgraded v1 → v2: Reliability Control Plane
- Added Memory Graph, RAG scaffolding, Policy & Safety layers
- Flyway migrations and integration test scaffolding

