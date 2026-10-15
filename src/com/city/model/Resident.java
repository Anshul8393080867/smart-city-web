package com.city.model;

public class Resident extends User {

    public Resident(int userId, String username, String password, String fullName, String zone) {
        // Residents are bound to a specific zone for localized service delivery and feedback
        super(userId, username, password, fullName, zone);
    }

    @Override
    public String getRole() {
        return "RESIDENT";
    }
}
