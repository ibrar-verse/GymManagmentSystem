package com.ibiverse.gymboy.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CheckIn_Logs")
public class CheckInLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Log_ID")
    private Integer logId;
    
    @ManyToOne
    @JoinColumn(name = "Member_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "Facility_ID")
    private Facility facility;
    
    @Column(name = "CheckIn_Time")
    private LocalDateTime checkInTime;

    @Column(name = "Duration_Minutes")
    private Integer durationMinutes;

    @Column(name = "Is_Active")
    private Boolean active;
    
    // Constructors
    public CheckInLog() {}
    
    // Getters and Setters
    public Integer getLogId() { return logId; }
    public void setLogId(Integer logId) { this.logId = logId; }
    
    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }

    public Facility getFacility() { return facility; }
    public void setFacility(Facility facility) { this.facility = facility; }
    
    public LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}