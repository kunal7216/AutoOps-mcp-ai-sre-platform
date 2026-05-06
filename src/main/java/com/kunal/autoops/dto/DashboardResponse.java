package com.kunal.autoops.dto;

public class DashboardResponse {
    private long totalIncidents;
    private long openIncidents;
    private long recoveredIncidents;
    private long blockedIncidents;
    private double estimatedMttrMinutes;

    public DashboardResponse(long totalIncidents, long openIncidents, long recoveredIncidents, long blockedIncidents, double estimatedMttrMinutes) {
        this.totalIncidents = totalIncidents;
        this.openIncidents = openIncidents;
        this.recoveredIncidents = recoveredIncidents;
        this.blockedIncidents = blockedIncidents;
        this.estimatedMttrMinutes = estimatedMttrMinutes;
    }

    public long getTotalIncidents() { return totalIncidents; }
    public long getOpenIncidents() { return openIncidents; }
    public long getRecoveredIncidents() { return recoveredIncidents; }
    public long getBlockedIncidents() { return blockedIncidents; }
    public double getEstimatedMttrMinutes() { return estimatedMttrMinutes; }
}
