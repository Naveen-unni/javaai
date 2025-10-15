// src/main/java/com/example/bloodtracking/model/Donor.java
package com.example.bloodtracking.model;

public class Donor {
    private long id;
    private String name;
    private String bloodGroup;
    private String location;
    private String contactNumber;

    public Donor() {}

    public Donor(long id, String name, String bloodGroup, String location, String contactNumber) {
        this.id = id;
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.location = location;
        this.contactNumber = contactNumber;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}