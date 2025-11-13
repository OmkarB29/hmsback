package com.example.hmsbe.service;

import com.example.hmsbe.model.User;
import com.example.hmsbe.repo.UserRepository;
import com.example.hmsbe.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }


    // ✅ Register new user
    public Map<String, Object> register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("msg", "User registered successfully");
        return response;
    }

    // ✅ Login user and generate JWT
    public Map<String, Object> login(String username, String password) {
        System.out.println("🔍 Login attempt for: " + username);
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            System.out.println("✅ Authentication successful for: " + username);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtUtil.generateToken(username, user.getRole().name());
            System.out.println("🎟️ Token generated: " + token);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", Map.of(
                    "username", user.getUsername(),
                    "role", user.getRole().name()
            ));
            return response;

        } catch (AuthenticationException e) {
            e.printStackTrace();
            System.out.println("❌ Authentication failed: " + e.getMessage());
            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ General error: " + e.getMessage());
            throw new RuntimeException("Internal server error: " + e.getMessage());
        }
    }

}


