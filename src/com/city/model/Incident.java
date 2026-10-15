package com.city.model;

import java.sql.Timestamp;

public class Incident implements Comparable<Incident> {

    public enum Severity {
        LOW(1),
        MEDIUM(2),
        HIGH(3),
        CRITICAL(4);

        private final int weight;

        Severity(int weight) {
            this.weight = weight;
        }

        public int getWeight() {
            return weight;
        }
    }

    private final int incidentId;
    private final String type;
    private final String zone;
    private final Severity severity;
    private final String description;
    private String status;
    private final Timestamp reportedAt;

    public Incident(int incidentId, String type, String zone, Severity severity, String description, String status, Timestamp reportedAt) {
        this.incidentId = incidentId;
        this.type = type;
        this.zone = zone;
        this.severity = severity;
        this.description = description;
        this.status = status;
        this.reportedAt = reportedAt;
    }

    @Override
    public int compareTo(Incident o) {
        // CRITICAL sorts first, hence the reversed comparison
        return Integer.compare(o.severity.getWeight(), this.severity.getWeight());
    }

    public int getIncidentId() {
        return incidentId;
    }

    public String getType() {
        return type;
    }

    public String getZone() {
        return zone;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getReportedAt() {
        return reportedAt;
    }

    @Override
    public String toString() {
        return "[" + severity + "] " + type + " (" + zone + ") - " + status;
    }
}
