package com.city.dao;

import com.city.model.Feedback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO extends BaseDAO<Feedback> {

    @Override
    protected String table() {
        return "feedback";
    }

    @Override
    protected String idColumn() {
        return "feedback_id";
    }

    @Override
    protected Feedback mapRow(ResultSet rs) throws SQLException {
        return new Feedback(
            rs.getInt("feedback_id"),
            rs.getInt("resident_id"),
            rs.getString("category"),
            rs.getString("message"),
            rs.getTimestamp("submitted_at")
        );
    }

    public List<Feedback> findByResident(int residentId) throws DataAccessException {
        String sql = "SELECT * FROM feedback WHERE resident_id = ? ORDER BY feedback_id DESC";
        try (Connection conn = DBConnection.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, residentId);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Feedback> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new DataAccessException("findByResident() failed on feedback for resident=" + residentId, e);
        }
    }

    public int insert(int residentId, String category, String message) throws DataAccessException {
        String sql = "INSERT INTO feedback (resident_id, category, message) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.get();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, residentId);
            stmt.setString(2, category);
            stmt.setString(3, message);

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                throw new DataAccessException("insert() failed on feedback: no generated key returned");
            }
        } catch (SQLException e) {
            throw new DataAccessException("insert() failed on feedback for resident=" + residentId, e);
        }
    }
}
