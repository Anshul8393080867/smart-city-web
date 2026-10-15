package com.city.dao;

import com.city.model.CityAdmin;
import com.city.model.Resident;
import com.city.model.UrbanPlanner;
import com.city.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO extends BaseDAO<User> {

    @Override
    protected String table() {
        return "users";
    }

    @Override
    protected String idColumn() {
        return "user_id";
    }

    @Override
    protected User mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("user_id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String fullName = rs.getString("full_name");
        String role = rs.getString("role");
        String zone = rs.getString("zone");

        // Instantiates the concrete subtype based on the database role discriminator
        switch (role) {
            case "ADMIN":
                return new CityAdmin(id, username, password, fullName);
            case "PLANNER":
                return new UrbanPlanner(id, username, password, fullName);
            default:
                return new Resident(id, username, password, fullName, zone);
        }
    }

    public User authenticate(String username, String password) throws DataAccessException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("authenticate() failed on users for " + username, e);
        }
    }

    public boolean usernameTaken(String username) throws DataAccessException {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DBConnection.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException("usernameTaken() failed on users for " + username, e);
        }
    }

    public int insertResident(String username, String password, String fullName, String zone) throws DataAccessException {
        String sql = "INSERT INTO users (username, password, full_name, role, zone) VALUES (?, ?, ?, 'RESIDENT', ?)";
        try (Connection conn = DBConnection.get();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, fullName);
            stmt.setString(4, zone);

            stmt.executeUpdate();

            // Retrieves the database-generated auto-increment user ID
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                throw new DataAccessException("insertResident() failed on users: no generated key returned");
            }
        } catch (SQLException e) {
            throw new DataAccessException("insertResident() failed on users for " + username, e);
        }
    }
}
