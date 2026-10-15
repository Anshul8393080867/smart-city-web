package com.city.service;

import java.util.Random;

public class SensorFeed implements Monitorable, Runnable {

    private final String zone;
    private final String metric;
    private final double baseline;
    private final double jitter;
    private final long intervalMs;
    private final Random rng = new Random();
    private final LiveDataStore store = LiveDataStore.getInstance();
    private volatile boolean running = true;

    public SensorFeed(String zone, String metric, double baseline, double jitter, long intervalMs) {
        this.zone = zone;
        this.metric = metric;
        this.baseline = baseline;
        this.jitter = jitter;
        this.intervalMs = intervalMs;
    }

    @Override
    public String getZone() {
        return zone;
    }

    @Override
    public String getMetric() {
        return metric;
    }

    @Override
    public double sample() {
        // Generates continuous fluctuations centered on the baseline value
        return Math.max(0.0, baseline + (rng.nextDouble() - 0.5) * 2.0 * jitter);
    }

    @Override
    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            store.publish(zone, metric, sample());
            try {
                Thread.sleep(intervalMs);
            } catch (InterruptedException e) {
                // Restore interrupted status to adhere to the cooperative interruption contract
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void shutdown() {
        running = false;
    }
}
