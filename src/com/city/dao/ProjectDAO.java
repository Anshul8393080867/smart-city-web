package com.city.dao;

import com.city.model.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO extends BaseDAO<Project> {

    @Override
    protected String table() {
        return "projects";
    }

    @Override
    protected String idColumn() {
        return "project_id";
    }

    @Override
    protected Project mapRow(ResultSet rs) throws SQLException {
        return new Project(
            rs.getInt("project_id"),
            rs.getString("title"),
            rs.getString("zone"),
            rs.getDouble("budget"),
            rs.getString("status"),
            rs.getDate("start_date"),
            rs.getString("description")
        );
    }

    public List<Project> findByStatus(String status) throws DataAccessException {
        String sql = "SELECT * FROM projects WHERE status = ?";
        try (Connection conn = DBConnection.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Project> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new DataAccessException("findByStatus() failed on projects for status=" + status, e);
        }
    }

    public int insert(Project p) throws DataAccessException {
        String sql = "INSERT INTO projects (title, zone, budget, status, start_date, description) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.get();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, p.getTitle());
            stmt.setString(2, p.getZone());
            stmt.setDouble(3, p.getBudget());
            stmt.setString(4, p.getStatus());
            stmt.setDate(5, p.getStartDate());
            stmt.setString(6, p.getDescription());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                throw new DataAccessException("insert() failed on projects: no generated key returned");
            }
        } catch (SQLException e) {
            throw new DataAccessException("insert() failed on projects for " + p.getTitle(), e);
        }
    }
}
