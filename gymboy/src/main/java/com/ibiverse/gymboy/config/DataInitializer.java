package com.ibiverse.gymboy.config;

import com.ibiverse.gymboy.model.CheckInLog;
import com.ibiverse.gymboy.model.Facility;
import com.ibiverse.gymboy.model.Member;
import com.ibiverse.gymboy.repository.CheckInLogRepository;
import com.ibiverse.gymboy.repository.FacilityRepository;
import com.ibiverse.gymboy.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class DataInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner seedCheckInData(FacilityRepository facilityRepository,
                                      MemberRepository memberRepository,
                                      CheckInLogRepository checkInLogRepository) {
        return args -> {
            try {
            if (facilityRepository.count() == 0) {
                Facility gymFloor = new Facility();
                gymFloor.setFacilityName("Gym Floor");
                gymFloor.setDescription("Free weights and strength training area");
                facilityRepository.save(gymFloor);

                Facility cardio = new Facility();
                cardio.setFacilityName("Cardio Zone");
                cardio.setDescription("Treadmills, bikes, and rowers");
                facilityRepository.save(cardio);

                Facility recovery = new Facility();
                recovery.setFacilityName("Recovery Room");
                recovery.setDescription("Stretching and cool-down space");
                facilityRepository.save(recovery);
            }

            if (memberRepository.count() == 0) {
                Member memberOne = new Member();
                memberOne.setFirstName("Abrar");
                memberOne.setLastName("Ahmad");
                memberOne.setEmail("abrar@example.com");
                memberOne.setPhone("03001234567");
                memberOne.setPassword("123456");
                memberOne.setJoinDate(LocalDate.now().minusDays(30));
                memberOne.setExpiryDate(LocalDate.now().plusMonths(1));
                memberOne.setStatus("Active");
                memberRepository.save(memberOne);

                Member memberTwo = new Member();
                memberTwo.setFirstName("Taha");
                memberTwo.setLastName("Tanvir");
                memberTwo.setEmail("taha@example.com");
                memberTwo.setPhone("03007654321");
                memberTwo.setPassword("123456");
                memberTwo.setJoinDate(LocalDate.now().minusDays(10));
                memberTwo.setExpiryDate(LocalDate.now().plusMonths(2));
                memberTwo.setStatus("Active");
                memberRepository.save(memberTwo);
            }

            if (checkInLogRepository.count() == 0 && memberRepository.count() > 0 && facilityRepository.count() > 0) {
                CheckInLog log = new CheckInLog();
                log.setMember(memberRepository.findAll().get(0));
                log.setFacility(facilityRepository.findAll().get(0));
                log.setCheckInTime(LocalDateTime.now().minusMinutes(20));
                log.setDurationMinutes(20);
                log.setActive(true);
                checkInLogRepository.save(log);
            }
                logger.info("Database seeding completed successfully");
            } catch (Exception e) {
                logger.error("Failed to seed database - this may be normal on first startup if DB is not ready", e);
                // Don't crash the app if seeding fails - DB might not be ready yet
            }
        };
    }
}