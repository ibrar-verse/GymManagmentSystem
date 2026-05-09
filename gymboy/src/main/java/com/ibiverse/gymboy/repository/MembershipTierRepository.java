package com.ibiverse.gymboy.repository;

import com.ibiverse.gymboy.model.MembershipTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipTierRepository extends JpaRepository<MembershipTier, Integer> {
    // Spring automatically creates methods like:
    // findAll(), findById(), save(), delete()
}