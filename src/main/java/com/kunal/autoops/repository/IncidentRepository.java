package com.kunal.autoops.repository;

import com.kunal.autoops.entity.Incident;
import com.kunal.autoops.enums.IncidentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByStatus(IncidentStatus status);
    List<Incident> findByServiceNameIgnoreCase(String serviceName);
}
