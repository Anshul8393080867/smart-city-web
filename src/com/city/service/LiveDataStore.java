package com.city.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class LiveDataStore {

    private static final LiveDataStore INSTANCE = new LiveDataStore();

    private final ConcurrentHashMap<String, Double> latest = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<Double>> history = new ConcurrentHashMap<>();
    private final Object monitor = new Object();
    private long updateCount = 0;

    private LiveDataStore() {
    }

    public static LiveDataStore getInstance() {
        return INSTANCE;
    }

    public void publish(String zone, String metric, double value) {
        synchronized (monitor) {
            String key = metric + "@" + zone;
            latest.put(key, value);

            List<Double> samples = history.computeIfAbsent(key, k -> new ArrayList<>());
            samples.add(value);
            // Limit historical buffer to 100 samples per metric to prevent unbounded memory growth
            if (samples.size() > 100) {
                samples.remove(0);
            }

            updateCount++;
            // wake awaitUpdate callers
            monitor.notifyAll();
        }
    }

    public double latest(String zone, String metric) {
        synchronized (monitor) {
            Double val = latest.get(metric + "@" + zone);
            return val != null ? val : 0.0;
        }
    }

    public double average(String zone, String metric) {
        synchronized (monitor) {
            List<Double> samples = history.get(metric + "@" + zone);
            if (samples == null || samples.isEmpty()) {
                return 0.0;
            }
            double sum = 0.0;
            for (Double s : samples) {
                sum += s;
            }
            return sum / samples.size();
        }
    }

    public long getUpdateCount() {
        synchronized (monitor) {
            return updateCount;
        }
    }

    public boolean awaitUpdate(long timeoutMs) throws InterruptedException {
        synchronized (monitor) {
            long deadline = System.currentTimeMillis() + timeoutMs;
            long remaining = timeoutMs;
            long startCount = updateCount;

            // Loop guards against spurious wakeups
            while (updateCount == startCount && remaining > 0) {
                monitor.wait(remaining);
                remaining = deadline - System.currentTimeMillis();
            }
            return updateCount > startCount;
        }
    }
}
