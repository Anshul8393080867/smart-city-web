package com.city.service;

public interface Monitorable {

    String getZone();

    String getMetric();

    double sample();

    // Default key formatting keeps metric indexing consistent across data stores
    default String key() {
        return getMetric() + "@" + getZone();
    }
}
