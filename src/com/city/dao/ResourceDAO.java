package com.city.dao;

import com.city.model.Resource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResourceDAO extends BaseDAO<Resource> {

    @Override
    protected String table() {
        return "resources";
    }

    @Override
    protected String idColumn() {
        return "resource_id";
    }

    @Override
    protected Resource mapRow(ResultSet rs) throws SQLException {
        return new Resource(
            rs.getInt("resource_id"),
            rs.getString("type"),
            rs.getString("zone"),
            rs.getDouble("allocated"),
            rs.getDouble("consumed"),
            rs.getString("unit")
        );
    }

    public boolean topUp(String type, String zone, double extra) throws DataAccessException {
        String sql = "UPDATE resources SET allocated = allocated + ? WHERE type = ? AND zone = ?";
        try (Connection conn = DBConnection.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, extra);
            stmt.setString(2, type);
            stmt.setString(3, zone);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("topUp() failed on resources for " + type + " in " + zone, e);
        }
    }

    public List<Resource> findByZone(String zone) throws DataAccessException {
        String sql = "SELECT * FROM resources WHERE zone = ?";
        try (Connection conn = DBConnection.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, zone);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Resource> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new DataAccessException("findByZone() failed on resources for zone " + zone, e);
        }
    }
}
