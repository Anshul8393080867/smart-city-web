package com.city.model;

public abstract class User {

    private final int userId;
    private final String username;
    private final String password;
    private final String fullName;
    private final String zone;

    // Protected constructor ensures only concrete role subclasses can instantiate a User
    protected User(int userId, String username, String password, String fullName, String zone) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.zone = zone;
    }

    // Subclasses return their specific role designation (ADMIN, PLANNER, RESIDENT)
    public abstract String getRole();

    // Default permissions are restrictive (false); subclasses opt into their specific capabilities
    public boolean canManageResources() {
        return false;
    }

    public boolean canMonitorTraffic() {
        return false;
    }

    public boolean canPlanInfrastructure() {
        return false;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getZone() {
        return zone;
    }

    @Override
    public String toString() {
        return fullName + " [" + getRole() + " (" + username + ")]";
    }
}
