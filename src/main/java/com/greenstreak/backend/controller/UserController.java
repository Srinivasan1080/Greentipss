package com.greenstreak.backend.controller;

import com.greenstreak.backend.model.User;
import com.greenstreak.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173") // Allow frontend
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // ✅ Get user by email
    @GetMapping("/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
    }

    // ✅ Update bio using email param
    @PutMapping("/update-bio/{email}")
    public ResponseEntity<?> updateBio(@PathVariable String email, @RequestBody String bio) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setBio(bio);
                userRepository.save(user);
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating bio: " + e.getMessage());
        }
    }

    // ✅ Update streak data (used by check-in feature)
    @PutMapping("/update-streak/{email}")
    public ResponseEntity<?> updateStreak(@PathVariable String email, @RequestBody User streakData) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setCurrentStreak(streakData.getCurrentStreak());
                user.setBestStreak(streakData.getBestStreak());
                user.setLastCheckIn(streakData.getLastCheckIn());
                userRepository.save(user);
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating streak: " + e.getMessage());
        }
    }
}
