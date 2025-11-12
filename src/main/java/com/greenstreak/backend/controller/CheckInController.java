package com.greenstreak.backend.controller;

import com.greenstreak.backend.model.CheckIn;
import com.greenstreak.backend.model.User;
import com.greenstreak.backend.repository.CheckInRepository;
import com.greenstreak.backend.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/checkin")
@CrossOrigin(origins = "http://localhost:5173")
public class CheckInController {

    private final CheckInRepository checkInRepo;
    private final UserRepository userRepo;

    public CheckInController(CheckInRepository checkInRepo, UserRepository userRepo) {
        this.checkInRepo = checkInRepo;
        this.userRepo = userRepo;
    }

    @PostMapping("/{email}")
    public Map<String, Object> checkIn(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        LocalDate today = LocalDate.now();

        Optional<User> userOpt = userRepo.findByEmail(email);
        if (userOpt.isEmpty()) {
            response.put("error", "User not found");
            return response;
        }

        User user = userOpt.get();

        // Prevent double check-in
        Optional<CheckIn> existingCheckIn = checkInRepo.findByEmailAndCheckInDate(email, today);
        if (existingCheckIn.isPresent()) {
            response.put("message", "Already checked in today");
            response.put("user", user);
            return response;
        }

        // Save today's check-in
        CheckIn checkIn = new CheckIn();
        checkIn.setEmail(email);
        checkIn.setCheckInDate(today);
        checkInRepo.save(checkIn);

        // Calculate streak
        LocalDate yesterday = today.minusDays(1);
        boolean consecutive = yesterday.toString().equals(user.getLastCheckIn());

        int newStreak = consecutive ? user.getCurrentStreak() + 1 : 1;
        user.setCurrentStreak(newStreak);
        user.setBestStreak(Math.max(newStreak, user.getBestStreak()));
        user.setLastCheckIn(today.toString());

        userRepo.save(user);

        response.put("message", "Checked in successfully");
        response.put("user", user);
        return response;
    }

    @GetMapping("/{email}")
    public List<CheckIn> getUserCheckIns(@PathVariable String email) {
        return checkInRepo.findByEmailOrderByCheckInDateDesc(email);
    }
}
