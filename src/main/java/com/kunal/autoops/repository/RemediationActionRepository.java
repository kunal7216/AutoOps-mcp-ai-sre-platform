package com.kunal.autoops.repository;

import com.kunal.autoops.entity.RemediationAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RemediationActionRepository extends JpaRepository<RemediationAction, Long> {
    List<RemediationAction> findByIncidentId(Long incidentId);
}
