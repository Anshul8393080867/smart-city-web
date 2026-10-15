package com.city.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO<T> {

    // Concrete subclasses specify their target table name
    protected abstract String table();

    // Primary key column name for generic lookups
    protected abstract String idColumn();

    // Mapping method throws SQLException so the calling template method can catch and wrap it with table context
    protected abstract T mapRow(ResultSet rs) throws SQLException;

    public List<T> findAll() throws DataAccessException {
        String sql = "SELECT * FROM " + table();
        // TODO: Add pagination support (LIMIT and OFFSET) for tables that grow indefinitely
        try (Connection conn = DBConnection.get();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<T> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DataAccessException("findAll() failed on " + table(), e);
        }
    }

    public T findById(int id) throws DataAccessException {
        String sql = "SELECT * FROM " + table() + " WHERE " + idColumn() + " = ?";
        try (Connection conn = DBConnection.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            // ResultSet opened in nested try-with-resources to ensure cursor closure after parameter binding
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("findById() failed on " + table() + " for id=" + id, e);
        }
    }

    public boolean deleteById(int id) throws DataAccessException {
        String sql = "DELETE FROM " + table() + " WHERE " + idColumn() + " = ?";
        try (Connection conn = DBConnection.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("deleteById() failed on " + table() + " for id=" + id, e);
        }
    }
}
