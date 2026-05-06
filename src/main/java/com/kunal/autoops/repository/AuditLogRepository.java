package com.kunal.autoops.repository;

import com.kunal.autoops.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByIncidentIdOrderByCreatedAtAsc(Long incidentId);
}
