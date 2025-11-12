package com.greenstreak.backend.controller;

import com.greenstreak.backend.model.User;
import com.greenstreak.backend.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(UserRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/signup")
    public Map<String, Object> signup(@RequestBody User user) {
        if (repo.findByEmail(user.getEmail()).isPresent()) {
            return Map.of("error", "Email already registered!");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setProvider("LOCAL");
        repo.save(user);
        return Map.of("message", "Signup successful! Welcome to GreenStreak.");
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        var opt = repo.findByEmail(email);

        if (opt.isEmpty()) return Map.of("error", "User not found!");

        var user = opt.get();
        if (!encoder.matches(password, user.getPassword()))
            return Map.of("error", "Incorrect password!");

        return Map.of("message", "Login successful!", "user", user);
    }
}
