package com.ibiverse.gymboy.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Staff")
public class Staff {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Staff_ID")
    private Integer staffId;
    
    @Column(name = "First_Name", nullable = false)
    private String firstName;
    
    @Column(name = "Last_Name", nullable = false)
    private String lastName;
    
    @Column(name = "Email", unique = true)
    private String email;
    
    @Column(name = "Phone")
    private String phone;
    
    @Column(name = "Role")
    private String role;
    
    @Column(name = "Specialization")
    private String specialization;
    
    @Column(name = "Salary")
    private Double salary;
    
    @Column(name = "Photo_Path")
    private String photoPath;
    
    // Constructors
    public Staff() {}
    
    // Getters and Setters
    public Integer getStaffId() { return staffId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    
    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }
    
    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }
}