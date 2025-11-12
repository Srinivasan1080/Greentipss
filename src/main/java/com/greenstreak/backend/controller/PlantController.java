package com.greenstreak.backend.controller;

import com.greenstreak.backend.model.Plant;
import com.greenstreak.backend.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/plants")
@CrossOrigin(origins = "http://localhost:5173") // ✅ allow React frontend
public class PlantController {

    @Autowired
    private PlantRepository plantRepository;

    // ✅ Add new plant (POST /api/plants)
    @PostMapping
    public ResponseEntity<?> addPlant(@RequestBody Plant plant) {
        try {
            if (plant.getUserEmail() == null || plant.getUserEmail().isBlank()) {
                return ResponseEntity.badRequest().body("User email is required.");
            }
            if (plant.getPlantName() == null || plant.getPlantName().isBlank()) {
                plant.setPlantName("Unnamed Plant");
            }

            Plant saved = plantRepository.save(plant);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved); // ✅ returns saved JSON
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error adding plant: " + e.getMessage());
        }
    }

    // ✅ Get all plants (Admin use)
    @GetMapping
    public ResponseEntity<List<Plant>> getAllPlants() {
        return ResponseEntity.ok(plantRepository.findAll());
    }

    // ✅ Get plants by user (used by frontend: /api/plants/{email})
    @GetMapping("/{email}")
    public ResponseEntity<?> getPlantsByUser(@PathVariable String email) {
        try {
            if (email == null || email.isBlank()) {
                return ResponseEntity.badRequest().body("Email parameter is required.");
            }

            List<Plant> plants = plantRepository.findByUserEmailOrderByPlantedDateDesc(email);
            return ResponseEntity.ok(plants);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching plants for user: " + e.getMessage());
        }
    }

    // ✅ Optional legacy endpoint (if old frontend uses /user/{email})
    @GetMapping("/user/{email}")
    public ResponseEntity<List<Plant>> getPlantsByUserLegacy(@PathVariable String email) {
        List<Plant> plants = plantRepository.findByUserEmail(email);
        return ResponseEntity.ok(plants);
    }

    // ✅ Get single plant by ID (GET /api/plants/id/{id})
    @GetMapping("/id/{id}")
    public ResponseEntity<Plant> getPlantById(@PathVariable Long id) {
        Optional<Plant> plant = plantRepository.findById(id);
        return plant.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Update existing plant (PUT /api/plants/{id})
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlant(@PathVariable Long id, @RequestBody Plant updatedPlant) {
        Optional<Plant> optionalPlant = plantRepository.findById(id);
        if (optionalPlant.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plant not found.");
        }

        try {
            Plant existing = optionalPlant.get();
            existing.setPlantName(updatedPlant.getPlantName());
            existing.setPlantType(updatedPlant.getPlantType());
            existing.setPlantedDate(updatedPlant.getPlantedDate());
            existing.setLocation(updatedPlant.getLocation());
            existing.setNotes(updatedPlant.getNotes());
            existing.setGrowthStage(updatedPlant.getGrowthStage());
            existing.setPhotoUrl(updatedPlant.getPhotoUrl());
            existing.setUserEmail(updatedPlant.getUserEmail());

            Plant saved = plantRepository.save(existing);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating plant: " + e.getMessage());
        }
    }

    // ✅ Delete plant (DELETE /api/plants/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlant(@PathVariable Long id) {
        if (!plantRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plant not found.");
        }
        try {
            plantRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting plant: " + e.getMessage());
        }
    }
}
