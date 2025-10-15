// src/main/java/com/example/bloodtracking/dao/DonorDAO.java
package com.example.bloodtracking.dao;

import com.example.bloodtracking.model.Donor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonorDAO {

    public void addDonor(Donor donor) {
        String sql = "INSERT INTO donors (name, bloodGroup, location, contactNumber) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, donor.getName());
            pstmt.setString(2, donor.getBloodGroup());
            pstmt.setString(3, donor.getLocation());
            pstmt.setString(4, donor.getContactNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Donor> getAllDonors() {
        List<Donor> donors = new ArrayList<>();
        String sql = "SELECT * FROM donors";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Donor donor = new Donor(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("bloodGroup"),
                        rs.getString("location"),
                        rs.getString("contactNumber")
                );
                donors.add(donor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return donors;
    }

    public List<Donor> searchDonors(String bloodGroup, String location) {
        List<Donor> donors = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM donors WHERE 1=1");
        List<String> params = new ArrayList<>();

        if (bloodGroup != null && !bloodGroup.isEmpty()) {
            sql.append(" AND bloodGroup = ?");
            params.add(bloodGroup);
        }
        if (location != null && !location.isEmpty()) {
            sql.append(" AND location = ?");
            params.add(location);
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setString(i + 1, params.get(i));
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Donor donor = new Donor(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("bloodGroup"),
                        rs.getString("location"),
                        rs.getString("contactNumber")
                );
                donors.add(donor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return donors;
    }
}