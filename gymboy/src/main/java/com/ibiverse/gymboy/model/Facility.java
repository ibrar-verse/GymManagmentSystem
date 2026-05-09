package com.ibiverse.gymboy.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Facilities")
public class Facility {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Facility_ID")
    private Integer facilityId;
    
    @Column(name = "Facility_Name", nullable = false)
    private String facilityName;
    
    @Column(name = "Description")
    private String description;
    
    // Constructors
    public Facility() {}
    
    // Getters and Setters
    public Integer getFacilityId() { return facilityId; }
    public void setFacilityId(Integer facilityId) { this.facilityId = facilityId; }
    
    public String getFacilityName() { return facilityName; }
    public void setFacilityName(String facilityName) { this.facilityName = facilityName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}