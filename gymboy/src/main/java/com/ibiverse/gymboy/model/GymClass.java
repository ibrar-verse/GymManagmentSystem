package com.ibiverse.gymboy.model;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "Classes")
public class GymClass {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Class_ID")
    private Integer classId;
    
    @Column(name = "Class_Name", nullable = false)
    private String className;
    
    @ManyToOne
    @JoinColumn(name = "Trainer_ID")
    private Staff trainer;
    
    @Column(name = "Schedule_Day")
    private String scheduleDay;
    
    @Column(name = "Schedule_Time")
    private LocalTime scheduleTime;
    
    @Column(name = "Duration_Minutes")
    private Integer durationMinutes;
    
    @Column(name = "Max_Capacity")
    private Integer maxCapacity;
    
    // Constructors
    public GymClass() {}
    
    // Getters and Setters
    public Integer getClassId() { return classId; }
    public void setClassId(Integer classId) { this.classId = classId; }
    
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    
    public Staff getTrainer() { return trainer; }
    public void setTrainer(Staff trainer) { this.trainer = trainer; }
    
    public String getScheduleDay() { return scheduleDay; }
    public void setScheduleDay(String scheduleDay) { this.scheduleDay = scheduleDay; }
    
    public LocalTime getScheduleTime() { return scheduleTime; }
    public void setScheduleTime(LocalTime scheduleTime) { this.scheduleTime = scheduleTime; }
    
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    
    public Integer getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(Integer maxCapacity) { this.maxCapacity = maxCapacity; }
}