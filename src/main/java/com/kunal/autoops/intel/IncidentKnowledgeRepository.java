package com.kunal.autoops.intel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IncidentKnowledgeRepository extends JpaRepository<IncidentKnowledge, Long> {
    List<IncidentKnowledge> findByServiceName(String serviceName);
    List<IncidentKnowledge> findByIncidentType(String incidentType);
}
