package com.ibiverse.gymboy.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Members")
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Member_ID")
    private Integer memberId;
    
    @Column(name = "First_Name", nullable = false)
    private String firstName;
    
    @Column(name = "Last_Name", nullable = false)
    private String lastName;
    
    @Column(name = "Email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "Phone")
    private String phone;
    
    @Column(name = "Password")
    private String password;
    
    @Column(name = "Join_Date")
    private LocalDate joinDate;
    
    @Column(name = "Expiry_Date")
    private LocalDate expiryDate;
    
    @Column(name = "Status")
    private String status;
    
    @ManyToOne
    @JoinColumn(name = "Tier_ID")
    private MembershipTier tier;
    
    // Constructors
    public Member() {}
    
    // Getters and Setters
    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }
    
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public MembershipTier getTier() { return tier; }
    public void setTier(MembershipTier tier) { this.tier = tier; }
}