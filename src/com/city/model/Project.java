package com.city.model;

import java.sql.Date;

public class Project {

    private final int projectId;
    private final String title;
    private final String zone;
    private final double budget;
    private String status;
    private final Date startDate;
    private final String description;

    public Project(int projectId, String title, String zone, double budget, String status, Date startDate, String description) {
        this.projectId = projectId;
        this.title = title;
        this.zone = zone;
        this.budget = budget;
        this.status = status;
        this.startDate = startDate;
        this.description = description;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getTitle() {
        return title;
    }

    public String getZone() {
        return zone;
    }

    public double getBudget() {
        return budget;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getDescription() {
        return description;
    }
}
