package com.city.dao;

import com.city.model.TrafficZone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TrafficDAO extends BaseDAO<TrafficZone> {

    @Override
    protected String table() {
        return "traffic_zones";
    }

    @Override
    protected String idColumn() {
        return "zone_id";
    }

    @Override
    protected TrafficZone mapRow(ResultSet rs) throws SQLException {
        return new TrafficZone(
            rs.getInt("zone_id"),
            rs.getString("zone_name"),
            rs.getInt("vehicle_count"),
            rs.getDouble("avg_speed"),
            rs.getString("congestion")
        );
    }

    public boolean updateCongestion(String zone, String level, int vehicles, double speed) throws DataAccessException {
        String sql = "UPDATE traffic_zones SET congestion = ?, vehicle_count = ?, avg_speed = ? WHERE zone_name = ?";
        try (Connection conn = DBConnection.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, level);
            stmt.setInt(2, vehicles);
            stmt.setDouble(3, speed);
            stmt.setString(4, zone);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("updateCongestion() failed on traffic_zones for " + zone, e);
        }
    }
}
