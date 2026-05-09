package com.kunal.autoops.intel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RunbookRepository extends JpaRepository<Runbook, Long> {
    List<Runbook> findByServiceName(String serviceName);
    List<Runbook> findByIncidentType(String incidentType);
}
