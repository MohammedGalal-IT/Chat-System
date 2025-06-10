package com.chatsystem.server.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.chatsystem.server.config.ConfigDB;

public class DatabaseManager {

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(ConfigDB.URL, ConfigDB.USER, ConfigDB.PASSWORD);
    }

    //  INSERT / UPDATE / DELETE
    public static int executeUpdate(String sql, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            setParameters(stmt, params);
            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Select Queries
    public static <T> T executeQuerySingle(String sql, ResultSetMapper<T> mapper, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            setParameters(stmt, params);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapper.map(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Select Queries for multiple results
    public static <T> List<T> executeQueryList(String sql, ResultSetMapper<T> mapper, Object... params) {
        List<T> results = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            setParameters(stmt, params);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                results.add(mapper.map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    // Utility method to set parameters in PreparedStatement
    private static void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
    }

    // Functional interface for mapping ResultSet to an object
    public interface ResultSetMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }
    
}
