package com.city.model;

public class TrafficZone {

    private final int zoneId;
    private final String zoneName;
    private int vehicleCount;
    private double avgSpeed;
    private String congestion;

    public TrafficZone(int zoneId, String zoneName, int vehicleCount, double avgSpeed, String congestion) {
        this.zoneId = zoneId;
        this.zoneName = zoneName;
        this.vehicleCount = vehicleCount;
        this.avgSpeed = avgSpeed;
        this.congestion = congestion;
    }

    public int getZoneId() {
        return zoneId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public int getVehicleCount() {
        return vehicleCount;
    }

    public void setVehicleCount(int vehicleCount) {
        this.vehicleCount = vehicleCount;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public String getCongestion() {
        return congestion;
    }

    public void setCongestion(String congestion) {
        this.congestion = congestion;
    }
}
