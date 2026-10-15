package com.city.model;

public class CityAdmin extends User {

    public CityAdmin(int userId, String username, String password, String fullName) {
        // Admins hold municipal-wide jurisdiction and are not bound to a single zone
        super(userId, username, password, fullName, null);
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }

    // CityAdmin possesses unrestricted control across all city subsystems
    @Override
    public boolean canManageResources() {
        return true;
    }

    @Override
    public boolean canMonitorTraffic() {
        return true;
    }

    @Override
    public boolean canPlanInfrastructure() {
        return true;
    }
}
