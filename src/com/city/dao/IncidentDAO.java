package com.city.dao;

import com.city.model.Incident;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class IncidentDAO extends BaseDAO<Incident> {

    @Override
    protected String table() {
        return "incidents";
    }

    @Override
    protected String idColumn() {
        return "incident_id";
    }

    @Override
    protected Incident mapRow(ResultSet rs) throws SQLException {
        return new Incident(
            rs.getInt("incident_id"),
            rs.getString("type"),
            rs.getString("zone"),
            Incident.Severity.valueOf(rs.getString("severity")),
            rs.getString("description"),
            rs.getString("status"),
            rs.getTimestamp("reported_at")
        );
    }

    public List<Incident> findOpen() throws DataAccessException {
        String sql = "SELECT * FROM incidents WHERE status <> 'RESOLVED' ORDER BY incident_id DESC";
        try (Connection conn = DBConnection.get();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<Incident> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DataAccessException("findOpen() failed on incidents", e);
        }
    }

    public boolean updateStatus(int id, String status) throws DataAccessException {
        String sql = "UPDATE incidents SET status = ? WHERE incident_id = ?";
        try (Connection conn = DBConnection.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("updateStatus() failed on incidents for id=" + id, e);
        }
    }

    public int insert(Incident inc) throws DataAccessException {
        String sql = "INSERT INTO incidents (type, zone, severity, description, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.get();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, inc.getType());
            stmt.setString(2, inc.getZone());
            stmt.setString(3, inc.getSeverity().name());
            stmt.setString(4, inc.getDescription());
            stmt.setString(5, inc.getStatus());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                throw new DataAccessException("insert() failed on incidents: no generated key returned");
            }
        } catch (SQLException e) {
            throw new DataAccessException("insert() failed on incidents for " + inc.getType(), e);
        }
    }
}
