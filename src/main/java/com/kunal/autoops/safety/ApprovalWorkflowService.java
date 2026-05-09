package com.kunal.autoops.safety;

import com.kunal.autoops.entity.RemediationAction;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class ApprovalWorkflowService {
    private final ConcurrentMap<Long, Boolean> approvals = new ConcurrentHashMap<>();

    public Long requestApproval(RemediationAction action) {
        Long id = action.getId();
        if (id == null) {
            // should be saved first; caller must save before requesting approval
            return null;
        }
        approvals.put(id, false);
        return id;
    }

    public void approve(Long actionId) {
        approvals.put(actionId, true);
    }

    public boolean isApproved(Long actionId) {
        return approvals.getOrDefault(actionId, false);
    }
}