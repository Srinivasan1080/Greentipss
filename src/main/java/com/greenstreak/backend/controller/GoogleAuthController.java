package com.greenstreak.backend.controller;

import com.greenstreak.backend.model.User;
import com.greenstreak.backend.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class GoogleAuthController {

    private final UserRepository repo;
    private final JacksonFactory jsonFactory = new JacksonFactory();

    public GoogleAuthController(UserRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/google")
    public Map<String, Object> googleLogin(@RequestBody Map<String, String> body) {
        String token = body.get("credential");
        try {
            var httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
                    .setAudience(Collections.singletonList("727492588980-osick05lmlpt0n5ta53dlut504j6ue0v.apps.googleusercontent.com"))
                    .build();

            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                // Check if user exists, else create one
                User user = repo.findByEmail(email).orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setFullName(name);
                    newUser.setPassword("GOOGLE_LOGIN");
                    newUser.setProvider("GOOGLE");
                    return repo.save(newUser);
                });

                return Map.of("message", "Google login success", "user", user);
            } else {
                return Map.of("error", "Invalid Google token");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("error", "Exception verifying token");
        }
    }
}
