package com.kunal.autoops.memory;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReliabilityMemoryGraphService {
    private final ServiceNodeRepository serviceRepo;
    private final IncidentNodeRepository incidentRepo;
    private final RootCauseNodeRepository rootCauseRepo;
    private final RemediationActionNodeRepository actionRepo;
    private final OutcomeNodeRepository outcomeRepo;
    private final MemoryEdgeRepository edgeRepo;

    public ReliabilityMemoryGraphService(ServiceNodeRepository serviceRepo,
                                         IncidentNodeRepository incidentRepo,
                                         RootCauseNodeRepository rootCauseRepo,
                                         RemediationActionNodeRepository actionRepo,
                                         OutcomeNodeRepository outcomeRepo,
                                         MemoryEdgeRepository edgeRepo) {
        this.serviceRepo = serviceRepo;
        this.incidentRepo = incidentRepo;
        this.rootCauseRepo = rootCauseRepo;
        this.actionRepo = actionRepo;
        this.outcomeRepo = outcomeRepo;
        this.edgeRepo = edgeRepo;
    }

    // Scaffolding methods
    public ServiceNode createServiceNode(ServiceNode s) { return serviceRepo.save(s); }
    public IncidentNode createIncidentNode(IncidentNode i) { return incidentRepo.save(i); }
    public RootCauseNode createRootCause(RootCauseNode r) { return rootCauseRepo.save(r); }
    public RemediationActionNode createRemediationAction(RemediationActionNode a) { return actionRepo.save(a); }
    public OutcomeNode createOutcome(OutcomeNode o) { return outcomeRepo.save(o); }
    public MemoryEdge link(String sourceType, Long sourceId, String targetType, Long targetId, String relation, Double weight) {
        MemoryEdge e = new MemoryEdge();
        e.setSourceType(sourceType);
        e.setSourceId(sourceId);
        e.setTargetType(targetType);
        e.setTargetId(targetId);
        e.setRelationType(relation);
        e.setWeight(weight == null ? 1.0 : weight);
        return edgeRepo.save(e);
    }

    public Optional<ServiceNode> findServiceByName(String name) { return serviceRepo.findByServiceName(name); }

    // TODO: implement findSimilarIncidents, findSuccessfulActionsForRootCause, calculateRiskFromHistoricalOutcomes

    public String buildMemoryContextForPlanner(com.kunal.autoops.entity.Incident incident) {
        // Minimal context: presence of a ServiceNode and counts of historical incidents
        String serviceName = incident.getServiceName();
        StringBuilder sb = new StringBuilder();
        sb.append("service=").append(serviceName);
        serviceRepo.findByServiceName(serviceName).ifPresent(s -> sb.append(";serviceId=").append(s.getId()));
        long incidentCount = incidentRepo.findAll().stream().filter(i -> i.getServiceId() != null && i.getServiceId().equals(incident.getServiceId())).count();
        sb.append(";historicalIncidents=").append(incidentCount);
        return sb.toString();
    }
}

