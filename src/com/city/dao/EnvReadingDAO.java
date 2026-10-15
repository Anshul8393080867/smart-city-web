package com.city.dao;

import com.city.service.LiveDataStore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EnvReadingDAO {

    private static final String[] ZONES = {"Sector 12", "Old Town", "Industrial Belt", "Central Hub"};

    public void saveSnapshot(LiveDataStore store) throws DataAccessException {
        String sql = "INSERT INTO env_readings (zone, aqi, noise_db, pm25) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int batchCount = 0;
            for (String zone : ZONES) {
                double aqi = store.average(zone, "AQI");
                double noise = store.average(zone, "NOISE");

                // Skip zones where sensor feeds have not yet generated telemetry
                if (aqi <= 0.0 && noise <= 0.0) {
                    continue;
                }

                // TODO: Replace linear 0.42 approximation with calibrated PM2.5 sensor telemetry
                double pm25 = aqi * 0.42;

                stmt.setString(1, zone);
                stmt.setInt(2, (int) Math.round(aqi));
                stmt.setDouble(3, Math.round(noise * 10.0) / 10.0);
                stmt.setDouble(4, Math.round(pm25 * 10.0) / 10.0);
                stmt.addBatch();
                batchCount++;
            }

            if (batchCount > 0) {
                stmt.executeBatch();
            }
        } catch (SQLException e) {
            throw new DataAccessException("saveSnapshot() failed on env_readings batch", e);
        }
    }
}
