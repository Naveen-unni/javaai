// src/main/java/com/example/bloodtracking/dao/DatabaseConnection.java
package com.example.bloodtracking.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:blood.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            // Create table if not exists
            stmt.execute("CREATE TABLE IF NOT EXISTS donors (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "bloodGroup TEXT NOT NULL," +
                    "location TEXT NOT NULL," +
                    "contactNumber TEXT NOT NULL)");

            // Check if table is empty and insert example data
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM donors");
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.execute("INSERT INTO donors (name, bloodGroup, location, contactNumber) VALUES ('John Doe', 'A+', 'Kochi', '1234567890')");
                stmt.execute("INSERT INTO donors (name, bloodGroup, location, contactNumber) VALUES ('Jane Smith', 'O-', 'Bangalore', '0987654321')");
                stmt.execute("INSERT INTO donors (name, bloodGroup, location, contactNumber) VALUES ('Alice Johnson', 'B+', 'Kochi', '1122334455')");
                stmt.execute("INSERT INTO donors (name, bloodGroup, location, contactNumber) VALUES ('Bob Brown', 'AB-', 'Mumbai', '5566778899')");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}