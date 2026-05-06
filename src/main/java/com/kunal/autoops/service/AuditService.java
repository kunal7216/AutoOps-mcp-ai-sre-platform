package com.kunal.autoops.service;

import com.kunal.autoops.entity.AuditLog;
import com.kunal.autoops.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {
    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void record(Long incidentId, String eventType, String actor, String traceId, String message) {
        AuditLog log = new AuditLog();
        log.setIncidentId(incidentId);
        log.setEventType(eventType);
        log.setActor(actor);
        log.setTraceId(traceId);
        log.setMessage(message);
        auditLogRepository.save(log);
    }

    public List<AuditLog> findByIncident(Long incidentId) {
        return auditLogRepository.findByIncidentIdOrderByCreatedAtAsc(incidentId);
    }
}
