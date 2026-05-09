package com.ibiverse.gymboy.repository;

import com.ibiverse.gymboy.model.CheckInLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CheckInLogRepository extends JpaRepository<CheckInLog, Integer> {
    List<CheckInLog> findByCheckInTimeBetween(LocalDateTime start, LocalDateTime end);
    List<CheckInLog> findByMember_MemberId(Integer memberId);
}