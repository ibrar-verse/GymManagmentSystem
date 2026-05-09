package com.ibiverse.gymboy.repository;

import com.ibiverse.gymboy.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    // Custom query methods
    Optional<Member> findByEmail(String email);
    List<Member> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName);
    List<Member> findByStatus(String status);
}