package com.city.service;

import com.city.dao.DataAccessException;
import com.city.dao.EnvReadingDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SensorNetwork {

    public static final String[] ZONES = {"Sector 12", "Old Town", "Industrial Belt", "Central Hub"};

    private final List<SensorFeed> feeds = new ArrayList<>();
    private final ExecutorService sensorPool;
    private final ScheduledExecutorService persister;

    public SensorNetwork() {
        AtomicInteger sensorThreadId = new AtomicInteger(1);
        ThreadFactory sensorFactory = runnable -> {
            // Daemon status prevents worker threads from blocking JVM termination on window close
            Thread thread = new Thread(runnable, "sensor-feed-" + sensorThreadId.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        };

        ThreadFactory persisterFactory = runnable -> {
            Thread thread = new Thread(runnable, "reading-persister");
            thread.setDaemon(true);
            return thread;
        };

        for (String zone : ZONES) {
            feeds.add(new SensorFeed(zone, "TRAFFIC", 420, 90, 2000));
            feeds.add(new SensorFeed(zone, "AQI", 118, 38, 5000));
            feeds.add(new SensorFeed(zone, "NOISE", 62, 9, 4000));
        }

        sensorPool = Executors.newFixedThreadPool(feeds.size(), sensorFactory);
        persister = Executors.newSingleThreadScheduledExecutor(persisterFactory);
    }

    public void startAll() {
        for (SensorFeed feed : feeds) {
            sensorPool.submit(feed);
        }
        persister.scheduleAtFixedRate(this::persistSnapshot, 20, 20, TimeUnit.SECONDS);
    }

    public void stopAll() {
        for (SensorFeed feed : feeds) {
            feed.shutdown();
        }
        sensorPool.shutdownNow();
        persister.shutdownNow();
    }

    public void persistSnapshot() {
        try {
            new EnvReadingDAO().saveSnapshot(LiveDataStore.getInstance());
        } catch (DataAccessException e) {
            // Logs to stderr so background persistence failures do not interrupt active GUI threads
            System.err.println("SensorNetwork snapshot persistence failed: " + e.getMessage());
        }
    }

    public int feedCount() {
        return feeds.size();
    }
}
