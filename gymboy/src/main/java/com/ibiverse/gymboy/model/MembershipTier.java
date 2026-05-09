package com.ibiverse.gymboy.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Membership_Tiers")
public class MembershipTier {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Tier_ID")
    private Integer tierId;
    
    @Column(name = "Tier_Name", nullable = false)
    private String tierName;
    
    @Column(name = "Monthly_Price", nullable = false)
    private Double monthlyPrice;
    
    @Column(name = "Access_Level", nullable = false)
    private Integer accessLevel;
    
    @Column(name = "Features")
    private String features;
    
    // Constructors
    public MembershipTier() {}
    
    // Getters and Setters
    public Integer getTierId() { return tierId; }
    public void setTierId(Integer tierId) { this.tierId = tierId; }
    
    public String getTierName() { return tierName; }
    public void setTierName(String tierName) { this.tierName = tierName; }
    
    public Double getMonthlyPrice() { return monthlyPrice; }
    public void setMonthlyPrice(Double monthlyPrice) { this.monthlyPrice = monthlyPrice; }
    
    public Integer getAccessLevel() { return accessLevel; }
    public void setAccessLevel(Integer accessLevel) { this.accessLevel = accessLevel; }
    
    public String getFeatures() { return features; }
    public void setFeatures(String features) { this.features = features; }
}