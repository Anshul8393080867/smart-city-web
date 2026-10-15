package com.city.dao;

import com.city.model.ServiceRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceRequestDAO extends BaseDAO<ServiceRequest> {

    @Override
    protected String table() {
        return "service_requests";
    }

    @Override
    protected String idColumn() {
        return "request_id";
    }

    @Override
    protected ServiceRequest mapRow(ResultSet rs) throws SQLException {
        return new ServiceRequest(
            rs.getInt("request_id"),
            rs.getInt("resident_id"),
            rs.getString("service_type"),
            rs.getString("details"),
            rs.getString("status"),
            rs.getTimestamp("created_at")
        );
    }

    public List<ServiceRequest> findByResident(int residentId) throws DataAccessException {
        String sql = "SELECT * FROM service_requests WHERE resident_id = ? ORDER BY request_id DESC";
        try (Connection conn = DBConnection.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, residentId);
            try (ResultSet rs = stmt.executeQuery()) {
                List<ServiceRequest> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new DataAccessException("findByResident() failed on service_requests for resident=" + residentId, e);
        }
    }

    public int insert(int residentId, String type, String details) throws DataAccessException {
        String sql = "INSERT INTO service_requests (resident_id, service_type, details, status) VALUES (?, ?, ?, 'PENDING')";
        try (Connection conn = DBConnection.get();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, residentId);
            stmt.setString(2, type);
            stmt.setString(3, details);

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                throw new DataAccessException("insert() failed on service_requests: no generated key returned");
            }
        } catch (SQLException e) {
            throw new DataAccessException("insert() failed on service_requests for resident=" + residentId, e);
        }
    }
}
