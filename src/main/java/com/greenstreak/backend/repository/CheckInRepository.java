package com.greenstreak.backend.repository;

import com.greenstreak.backend.model.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    Optional<CheckIn> findByEmailAndCheckInDate(String email, LocalDate date);
    List<CheckIn> findByEmailOrderByCheckInDateDesc(String email);
}
