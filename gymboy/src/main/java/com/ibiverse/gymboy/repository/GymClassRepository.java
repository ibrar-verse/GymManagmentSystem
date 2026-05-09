package com.ibiverse.gymboy.repository;

import com.ibiverse.gymboy.model.GymClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GymClassRepository extends JpaRepository<GymClass, Integer> {
    List<GymClass> findByScheduleDay(String day);
}