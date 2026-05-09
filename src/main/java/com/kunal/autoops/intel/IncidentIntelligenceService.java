package com.kunal.autoops.intel;

import com.kunal.autoops.entity.Incident;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IncidentIntelligenceService {
    private final IncidentKnowledgeRepository knowledgeRepo;
    private final RunbookRepository runbookRepo;
    private final IncidentEmbeddingService embeddingService;

    public IncidentIntelligenceService(IncidentKnowledgeRepository knowledgeRepo, RunbookRepository runbookRepo, IncidentEmbeddingService embeddingService) {
        this.knowledgeRepo = knowledgeRepo;
        this.runbookRepo = runbookRepo;
        this.embeddingService = embeddingService;
    }

    public List<IncidentKnowledge> findSimilarIncidents(String serviceName) {
        return knowledgeRepo.findByServiceName(serviceName);
    }

    public List<Runbook> findRunbooksForIncident(String serviceName, String incidentType) {
        List<Runbook> byService = runbookRepo.findByServiceName(serviceName);
        List<Runbook> byType = runbookRepo.findByIncidentType(incidentType);
        return (List<Runbook>) java.util.stream.Stream.concat(byService.stream(), byType.stream()).distinct().collect(Collectors.toList());
    }

    public String buildContextForPlanner(Incident incident) {
        // lightweight context builder: summarize recent knowledge and runbooks
        String service = incident.getServiceName();
        List<IncidentKnowledge> similar = findSimilarIncidents(service);
        List<Runbook> runbooks = findRunbooksForIncident(service, incident.getIncidentType());
        StringBuilder sb = new StringBuilder();
        sb.append("similarIncidents=").append(similar.size()).append("; runbooks=").append(runbooks.size());
        return sb.toString();
    }
}
