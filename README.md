# AutoOps — Autonomous Reliability Control Plane

![Java](https://img.shields.io/badge/Java-21-blue) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen) ![Postgres](https://img.shields.io/badge/Postgres-DB-blue) ![Kafka](https://img.shields.io/badge/Kafka-Event%20Streaming-black) ![Redis](https://img.shields.io/badge/Redis-Cache-red) ![Docker](https://img.shields.io/badge/Docker-Containerized-blue)

## 1. Overview
AutoOps is an autonomous reliability control plane that detects production incidents, retrieves historical incident memory, plans safe remediation, executes approved tools, validates recovery, and continuously improves via replay-based evaluation. It is engineered for production safety: policy, approval, rollback, idempotency, and observability are first-class.

## 2. Problem Statement
Modern distributed systems suffer from latency spikes, cache saturation, deployment regressions and dependency failures. Manual remediation is slow and risky; naive automation can worsen outages. Teams need a disciplined control plane that reasons about incidents, enforces safety, and learns from history.

## 3. Solution
AutoOps ingests telemetry and incidents, uses RAG + Reliability Memory Graph to provide planner context, applies a Policy Engine (RBAC, risk-scoring, approval gating), executes approved MCP-style tools via a safety layer (dry-run/rollback support), validates outcomes, and records memory for replay evaluation.

## 4. Key Features
- Planner / Executor / Validator agent pipeline
- Reliability Memory Graph (service → incident → root cause → action → outcome)
- RAG-based incident intelligence (runbooks, RCA, embeddings)
- Policy Engine, approval workflow, and Tool Execution Safety Layer
- Dry-run, idempotency keys, rollback checkpoints, audit logs
- OpenTelemetry hooks & Grafana-ready metrics
- Flyway migrations + Testcontainers integration

## 5. System Architecture
Insert architecture diagram below (Mermaid). This shows high-level data and control flow.

```mermaid
flowchart TD
  A[Telemetry / Metrics / Alerts] --> B(Incident Detection)
  B --> C{Incident Intelligence}
  C -->|RAG / Runbooks| D[Reliability Memory Graph]
  C -->|Embeddings| E[Incident Knowledge Repo]
  D --> F[Planner Agent]
  F --> G[Policy Engine]
  G -->|approved| H[Executor / MCP Tool Layer]
  G -->|needs approval| I[Approval Workflow]
  H --> J[Validator Agent]
  J --> K[Audit & Observability]
  K --> L[Replay Evaluation]
  L --> D
  style A fill:#f9f,stroke:#333,stroke-width:1px
  style D fill:#efe,stroke:#333,stroke-width:1px
  style L fill:#eef,stroke:#333,stroke-width:1px
```

Short explanation:
- Incident Detection consumes telemetry and signals potential incidents.
- Incident Intelligence (RAG + memory) augments the planner with historical context and runbooks.
- Policy Engine enforces RBAC, risk scoring and decides approval requirements.
- Executor runs MCP-style tools through a safety layer; Validator confirms recovery and records outcome.
- Replay Evaluation feeds results back into the Reliability Memory Graph for continuous improvement.

## 6. Workflow
Mermaid sequence/flow that highlights decision points and validation.

```mermaid
sequenceDiagram
  participant S as Source (Monitoring)
  participant API as API
  participant P as Planner
  participant M as Memory/RAG
  participant Pol as Policy Engine
  participant U as User/Approver
  participant Ex as Executor
  participant V as Validator
  participant DB as Audit/DB

  S->>API: incident.report(payload)
  API->>M: buildMemoryContext(incident)
  API->>P: plan(incident, memoryContext)
  P->>Pol: evaluate(plan)
  alt requires approval
    Pol-->>U: requestApproval(plan)
    U-->>Pol: approve/deny
  end
  Pol->>Ex: executeIfAllowed(plan)
  Ex->>V: run and report outcome
  V->>DB: persist(outcome, audit)
  DB-->>M: updateMemory(outcome)
  V->>API: notify(status)
```

Notes:
- Approval is a synchronous gate in this simplified flow; in production it may be asynchronous with callbacks.
- The memory/RAG step is used to bias the planner toward historically safe actions.

## 7. Tech Stack
Java 21, Spring Boot, Spring Data JPA, PostgreSQL, Flyway, Kafka, Redis, OpenTelemetry, Grafana, Docker, Maven, JUnit, Testcontainers. Optional: pgvector / vector DB for embeddings.

## 8. Project Modules
- api: controllers & OpenAPI
- incident-service: ingestion + detection
- planner: plan generation
- intel: RAG, embeddings, runbooks
- memory: Reliability Memory Graph and repos
- policy: Policy Engine and risk scoring
- executor: MCP tool adapters & safety layer
- validator: recovery validation
- replay: evaluation & scoring
- chaos: simulation scaffolds

## 9. API Endpoints
- POST /api/v1/incidents — create & process
- GET /api/v1/incidents — list
- GET /api/v1/incidents/{id} — details
- POST /api/v1/approvals/{id}/approve — approve

## 10. Database / Entity Relationship
Mermaid ER diagram showing main nodes and relationships.

```mermaid
erDiagram
    SERVICE_NODE {
        UUID id PK
        string service_name
        string owner_team
        string environment
        string criticality
        float current_slo_target
        timestamp created_at
    }
    INCIDENT_NODE {
        UUID id PK
        UUID service_id FK
        string incident_type
        string severity
        timestamp detected_at
        string status
        float latency_ms
        float error_rate
        int throughput
        float slo_burn_rate
        int customer_impact_score
    }
    ROOT_CAUSE_NODE {
        UUID id PK
        UUID incident_id FK
        string root_cause_type
        float confidence_score
        text evidence
        string detected_by
        timestamp created_at
    }
    REMEDIATION_ACTION_NODE {
        UUID id PK
        UUID incident_id FK
        string action_type
        string tool_name
        float risk_score
        boolean approval_required
        string execution_status
        boolean rollback_available
        timestamp created_at
    }
    OUTCOME_NODE {
        UUID id PK
        UUID incident_id FK
        UUID action_id FK
        boolean success
        int mttr_seconds
        float latency_improvement_percent
        float error_rate_improvement_percent
        boolean rollback_triggered
        text notes
    }
    MEMORY_EDGE {
        UUID id PK
        string source_type
        UUID source_id
        string target_type
        UUID target_id
        string relation_type
        float weight
        timestamp created_at
    }

    SERVICE_NODE ||--o{ INCIDENT_NODE : "HAS_INCIDENT"
    INCIDENT_NODE ||--o{ ROOT_CAUSE_NODE : "HAS_ROOT_CAUSE"
    INCIDENT_NODE ||--o{ REMEDIATION_ACTION_NODE : "USED_ACTION"
    REMEDIATION_ACTION_NODE ||--o{ OUTCOME_NODE : "PRODUCED_OUTCOME"
    ROOT_CAUSE_NODE ||--o{ MEMORY_EDGE : "SIMILAR_TO"
    INCIDENT_NODE ||--o{ MEMORY_EDGE : "SIMILAR_TO"
```

Short explanation:
- The ER diagram models the graph-like memory using node tables and memory edges. Query patterns include: find similar incidents by root cause, find successful actions for a root cause, and compute historical risk given prior outcomes.

## 11. Safety and Reliability Design
RBAC, approval gates, dry-run, idempotency keys, rollback checkpoints, immutable audit logs; policy thresholds and mitigation suggestions are central.

## 12. How to Run Locally
Prereqs: Java 21, Maven, Docker. Quickstart:
```bash
git clone https://github.com/kunal7216/AutoOps--Autonomous-Reliability-Control-Plane.git
cd AutoOps--Autonomous-Reliability-Control-Plane
mvn clean package
mvn spring-boot:run
# or docker compose up --build
```

## 13. Testing
Unit tests (JUnit + Mockito). Integration tests use Testcontainers (Postgres).

## 14. Screenshots
Mermaid diagrams render in GitHub; add screenshots only for dashboards or UI pages when available.

## 15. Future Enhancements
pgvector/Vector DB, persistent approval UI, full replay/evaluation pipeline, Kubernetes operator, PagerDuty/Jira integration.

## 16. Resume / Interview Highlights
- Upgraded to AutoOps X v2 with Reliability Memory Graph, RAG scaffolding, policy & safety layers, and replay evaluation scaffolds.

---

*Mermaid diagrams are embedded above. They render on GitHub automatically.*
