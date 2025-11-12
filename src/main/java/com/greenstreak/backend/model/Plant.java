package com.greenstreak.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "plants")
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String plantName;

    private String plantType;

    @JsonFormat(pattern = "yyyy-MM-dd") // ✅ ensures date format matches frontend input
    private LocalDate plantedDate;

    private String location;

    @Column(length = 500)
    private String notes;

    private String growthStage;

    private String photoUrl;

    @Column(nullable = false)
    private String userEmail; // to link the plant to the logged-in user

    // ✅ Constructors
    public Plant() {}

    public Plant(String plantName, String plantType, LocalDate plantedDate, String location,
                 String notes, String growthStage, String photoUrl, String userEmail) {
        this.plantName = plantName;
        this.plantType = plantType;
        this.plantedDate = plantedDate;
        this.location = location;
        this.notes = notes;
        this.growthStage = growthStage;
        this.photoUrl = photoUrl;
        this.userEmail = userEmail;
    }

    // ✅ Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlantName() { return plantName; }
    public void setPlantName(String plantName) { this.plantName = plantName; }

    public String getPlantType() { return plantType; }
    public void setPlantType(String plantType) { this.plantType = plantType; }

    public LocalDate getPlantedDate() { return plantedDate; }
    public void setPlantedDate(LocalDate plantedDate) { this.plantedDate = plantedDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getGrowthStage() { return growthStage; }
    public void setGrowthStage(String growthStage) { this.growthStage = growthStage; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    // ✅ Optional: helpful toString() for logging/debug
    @Override
    public String toString() {
        return "Plant{" +
                "id=" + id +
                ", plantName='" + plantName + '\'' +
                ", plantType='" + plantType + '\'' +
                ", plantedDate=" + plantedDate +
                ", location='" + location + '\'' +
                ", notes='" + notes + '\'' +
                ", growthStage='" + growthStage + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }
}
