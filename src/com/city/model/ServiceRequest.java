package com.city.model;

import java.sql.Timestamp;

public class ServiceRequest {

    private final int requestId;
    private final int residentId;
    private final String serviceType;
    private final String details;
    private String status;
    private final Timestamp createdAt;

    public ServiceRequest(int requestId, int residentId, String serviceType, String details, String status, Timestamp createdAt) {
        this.requestId = requestId;
        this.residentId = residentId;
        this.serviceType = serviceType;
        this.details = details;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getResidentId() {
        return residentId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getDetails() {
        return details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
