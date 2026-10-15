package com.city.service;

import com.city.model.Incident;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class IncidentQueue {

    private final PriorityQueue<Incident> queue = new PriorityQueue<>();

    public synchronized void load(Collection<? extends Incident> incidents) {
        queue.clear();
        for (Incident inc : incidents) {
            // Closed incidents are excluded from the active dispatch triage queue
            if (!"RESOLVED".equalsIgnoreCase(inc.getStatus())) {
                queue.offer(inc);
            }
        }
    }

    public synchronized Incident next() {
        return queue.poll();
    }

    public synchronized List<Incident> top(int n) {
        // Copies queue so non-destructive UI reads do not consume prioritized items
        PriorityQueue<Incident> copy = new PriorityQueue<>(queue);
        List<Incident> results = new ArrayList<>();
        int count = Math.min(n, copy.size());
        for (int i = 0; i < count; i++) {
            results.add(copy.poll());
        }
        return results;
    }

    public synchronized Map<String, List<Incident>> groupByZone() {
        Map<String, List<Incident>> grouped = new HashMap<>();
        for (Incident inc : queue) {
            grouped.computeIfAbsent(inc.getZone(), k -> new ArrayList<>()).add(inc);
        }
        return grouped;
    }

    public synchronized int size() {
        return queue.size();
    }
}
