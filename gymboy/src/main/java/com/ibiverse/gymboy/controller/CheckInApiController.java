package com.ibiverse.gymboy.controller;

import com.ibiverse.gymboy.model.CheckInLog;
import com.ibiverse.gymboy.model.Facility;
import com.ibiverse.gymboy.model.Member;
import com.ibiverse.gymboy.repository.CheckInLogRepository;
import com.ibiverse.gymboy.repository.FacilityRepository;
import com.ibiverse.gymboy.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CheckInApiController {

    @Autowired private CheckInLogRepository checkInRepo;
    @Autowired private MemberRepository memberRepo;
    @Autowired private FacilityRepository facilityRepo;

    @GetMapping("/facilities/list")
    public Map<String, Object> listFacilities() {
        List<Map<String, Object>> facilities = facilityRepo.findAll().stream()
            .map(facility -> {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("id", facility.getFacilityId());
                item.put("name", facility.getFacilityName());
                item.put("description", facility.getDescription());
                return item;
            })
            .collect(Collectors.toList());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("facilities", facilities);
        return response;
    }

    @PostMapping("/check-in/process")
    public ResponseEntity<Map<String, Object>> processCheckIn(@RequestBody Map<String, String> request) {
        String memberIdOrEmail = request.getOrDefault("memberIdOrEmail", "").trim();
        String facilityIdValue = request.getOrDefault("facilityId", "").trim();

        if (memberIdOrEmail.isEmpty() || facilityIdValue.isEmpty()) {
            return ResponseEntity.badRequest().body(error("Please provide a member and facility."));
        }

        Member member = findMember(memberIdOrEmail).orElse(null);
        if (member == null) {
            return ResponseEntity.badRequest().body(error("Member not found."));
        }

        Integer facilityId;
        try {
            facilityId = Integer.valueOf(facilityIdValue);
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().body(error("Invalid facility selection."));
        }

        Facility facility = facilityRepo.findById(facilityId).orElse(null);
        if (facility == null) {
            return ResponseEntity.badRequest().body(error("Facility not found."));
        }

        CheckInLog log = new CheckInLog();
        log.setMember(member);
        log.setFacility(facility);
        log.setCheckInTime(LocalDateTime.now());
        log.setDurationMinutes(0);
        log.setActive(true);
        checkInRepo.save(log);

        Map<String, Object> response = success();
        response.put("memberName", member.getFirstName() + " " + member.getLastName());
        response.put("facilityName", facility.getFacilityName());
        response.put("checkInTime", log.getCheckInTime());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-in/today-summary")
    public Map<String, Object> todaySummary() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().plusDays(1).atStartOfDay();
        List<CheckInLog> checkIns = checkInRepo.findByCheckInTimeBetween(start, end);

        long activeMembers = checkIns.stream()
            .map(CheckInLog::getMember)
            .filter(member -> member != null)
            .map(Member::getMemberId)
            .distinct()
            .count();

        String peakTime = checkIns.stream()
            .collect(Collectors.groupingBy(log -> log.getCheckInTime().getHour(), Collectors.counting()))
            .entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(entry -> String.format("%02d:00", entry.getKey()))
            .orElse("N/A");

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("totalCheckIns", checkIns.size());
        summary.put("activeMembers", activeMembers);
        summary.put("peakTime", peakTime);

        Map<String, Object> response = success();
        response.put("summary", summary);
        return response;
    }

    @GetMapping("/check-in/today")
    public Map<String, Object> todayCheckIns() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().plusDays(1).atStartOfDay();

        List<Map<String, Object>> checkIns = checkInRepo.findByCheckInTimeBetween(start, end).stream()
            .sorted(Comparator.comparing(CheckInLog::getCheckInTime).reversed())
            .map(log -> {
                Map<String, Object> item = new LinkedHashMap<>();
                Member member = log.getMember();
                Facility facility = log.getFacility();

                item.put("memberName", member == null ? "Unknown" : member.getFirstName() + " " + member.getLastName());
                item.put("checkInTime", log.getCheckInTime());
                item.put("facilityName", facility == null ? "N/A" : facility.getFacilityName());
                item.put("durationMinutes", log.getDurationMinutes() == null ? 0 : log.getDurationMinutes());
                item.put("isActive", log.getActive() == null || log.getActive());
                return item;
            })
            .collect(Collectors.toList());

        Map<String, Object> response = success();
        response.put("checkIns", checkIns);
        return response;
    }

    private Optional<Member> findMember(String memberIdOrEmail) {
        try {
            Integer memberId = Integer.valueOf(memberIdOrEmail);
            return memberRepo.findById(memberId);
        } catch (NumberFormatException ex) {
            return memberRepo.findByEmail(memberIdOrEmail);
        }
    }

    private Map<String, Object> success() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        return response;
    }

    private Map<String, Object> error(String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }
}