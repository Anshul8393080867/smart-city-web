package com.city.model;

public class UrbanPlanner extends User {

    public UrbanPlanner(int userId, String username, String password, String fullName) {
        // Planners oversee city-wide infrastructure projects rather than a single residential zone
        super(userId, username, password, fullName, null);
    }

    @Override
    public String getRole() {
        return "PLANNER";
    }

    // Planners can draft, monitor, and approve infrastructure projects
    @Override
    public boolean canPlanInfrastructure() {
        return true;
    }
}
