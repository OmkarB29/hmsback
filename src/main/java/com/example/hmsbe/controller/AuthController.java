package com.example.hmsbe.controller;

import com.example.hmsbe.model.Student;
import com.example.hmsbe.model.User;
import com.example.hmsbe.model.Role;
import com.example.hmsbe.repo.StudentRepository;
import com.example.hmsbe.repo.UserRepository;
import com.example.hmsbe.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://hmsclg.netlify.app", "https://hmsclg.netlify.app"})
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private StudentRepository studentRepo;

    // ✅ Register endpoint (merged logic)
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, Object> requestBody) {
        try {
            // Extract user data from request
            String username = (String) requestBody.get("username");
            String password = (String) requestBody.get("password");
            String email = (String) requestBody.get("email");
            String name = (String) requestBody.get("name");
            String phone = (String) requestBody.get("phone");
            String roleStr = (String) requestBody.getOrDefault("role", "STUDENT");
            
            // Validate required fields
            if (username == null || username.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Username is required"));
            }
            if (password == null || password.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Password is required"));
            }
            if (email == null || email.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
            }

            // Check if user already exists
            if (userRepo.findByUsername(username).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
            }

            // Create and save user
            User user = new User();
            user.setUsername(username);
            user.setPassword(authService.encodePassword(password));
            user.setEmail(email);
            
            try {
                Role role = Role.valueOf(roleStr.toUpperCase());
                user.setRole(role);
            } catch (IllegalArgumentException e) {
                user.setRole(Role.STUDENT);  // Default to STUDENT if invalid
            }
            
            User savedUser = userRepo.save(user);

            // ✅ If the user is a student, create a linked Student record
            if (savedUser.getRole() == Role.STUDENT) {
                Student student = new Student();
                student.setUsername(savedUser.getUsername());
                student.setName(name != null ? name : savedUser.getUsername());
                student.setEmail(email);
                student.setPhone(phone);
                student.setDepartment("Not Assigned");
                student.setUser(savedUser);
                studentRepo.save(student);
            }

            return ResponseEntity.ok(Map.of(
                    "message", "User registered successfully!",
                    "user", Map.of(
                            "id", savedUser.getId(),
                            "username", savedUser.getUsername(),
                            "email", savedUser.getEmail(),
                            "role", savedUser.getRole().name()
                    )
            ));

        } catch (Exception e) {
            e.printStackTrace();  // Log error for debugging
            return ResponseEntity.badRequest().body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }


    // ✅ Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            Map<String, Object> result = authService.login(
                    credentials.get("username"),
                    credentials.get("password")
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }
}


