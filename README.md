# AutoOps X — Autonomous Reliability Control Plane (v2)

AutoOps X is an autonomous reliability control plane that detects production incidents, retrieves historical reliability memory, plans safe remediation, executes approved tools, validates recovery, and continuously improves through replay-based evaluation.

---

## Table of Contents
- Project Overview
- Key Features
- Architecture
- System Workflow
- Running Locally
- Running with Docker
- API
- Contributing

---

## Project Overview
AutoOps X demonstrates a safe, policy-driven agentic platform for SRE workflows. Agents propose remediation plans; a policy engine and approval workflow ensure safety before any tool executes changes in production.

## Key Features
- Incident detection and planning (Planner Agent)
- RAG-based incident intelligence and Reliability Memory Graph
- Policy-driven execution with approval gates
- Tool execution safety layer and rollback checks
- Audit logging and observability (OpenTelemetry/Grafana)
- Replay evaluation and chaos simulation scaffolds

## Architecture
```
Telemetry → Incident Detection → Incident Intelligence (RAG)
         → Reliability Memory Graph → Planner → Policy Engine
         → Executor (MCP tool layer) → Validator → Audit + Observability
         → Replay Evaluation
```

## System Workflow
1. Incident ingested via REST/Kafka
2. Planner produces recommended actions using memory + RAG context
3. Policy Engine evaluates risk and decides APPROVED / APPROVAL_REQUIRED / DENIED
4. If approval required, human operator reviews in Approval Workflow
5. Executor runs MCP-style tool (only if approved)
6. Validator confirms recovery; outcome is stored in Reliability Memory
7. Replay system evaluates decision quality over historical runs

## Running Locally
```bash
git clone https://github.com/kunal7216/autoops-mcp-ai-sre-platform.git
cd autoops-mcp-ai-sre-platform
mvn clean package
mvn spring-boot:run
# or run tests: mvn test
```

Application URL: http://localhost:8080

## Running with Docker
```bash
mvn clean package
docker build -t autoops-x:2.0.0 .
docker run -p 8080:8080 autoops-x:2.0.0
```

## API (example)
- POST /api/v1/incidents — create and process incident
- GET /api/v1/incidents — list incidents
- POST /api/v1/approvals/{id}/approve — approve pending action

## Contributing
- All commits should be authored by Kunal (project maintainer). Please coordinate before rebasing/pushing rewritten history.

---

Author: Kunal Kumar
