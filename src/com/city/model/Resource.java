package com.city.model;

public class Resource {

    private final int resourceId;
    private final String type;
    private final String zone;
    private final double allocated;
    private final double consumed;
    private final String unit;

    public Resource(int resourceId, String type, String zone, double allocated, double consumed, String unit) {
        this.resourceId = resourceId;
        this.type = type;
        this.zone = zone;
        this.allocated = allocated;
        this.consumed = consumed;
        this.unit = unit;
    }

    public double utilisation() {
        // Guard against divide-by-zero when a resource allocation has not yet been assigned
        return allocated == 0.0 ? 0.0 : (consumed / allocated) * 100.0;
    }

    public int getResourceId() {
        return resourceId;
    }

    public String getType() {
        return type;
    }

    public String getZone() {
        return zone;
    }

    public double getAllocated() {
        return allocated;
    }

    public double getConsumed() {
        return consumed;
    }

    public String getUnit() {
        return unit;
    }
}
