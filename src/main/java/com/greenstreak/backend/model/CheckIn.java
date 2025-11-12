package com.greenstreak.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "checkins")
public class CheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private LocalDate checkInDate;

    // ✅ Constructors
    public CheckIn() {}

    public CheckIn(String email, LocalDate checkInDate) {
        this.email = email;
        this.checkInDate = checkInDate;
    }

    // ✅ Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }
}
