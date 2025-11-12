package com.greenstreak.backend.repository;

import com.greenstreak.backend.model.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {
    List<Plant> findByUserEmail(String userEmail);
        List<Plant> findByUserEmailOrderByPlantedDateDesc(String email);
}
