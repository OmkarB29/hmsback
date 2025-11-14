package com.example.hmsbe.controller;

import com.example.hmsbe.model.*;
import com.example.hmsbe.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = {"http://localhost:3000", "http://hmsclg.netlify.app", "https://hmsclg.netlify.app"})
public class StudentController {
    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentRepository studentRepository;

    // add these if controller uses them
    @Autowired
    private RoomChangeRequestRepository roomChangeRepo;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private FeeRepository feeRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    // simpler helper
    private String resolveUsername(String xTestUser, String auth) {
        if (xTestUser != null && !xTestUser.isBlank())
            return xTestUser;
        if (auth != null && auth.startsWith("Bearer "))
            return auth.substring(7);
        return null;
    }

    @PutMapping("/profile")
    @Transactional
    public ResponseEntity<?> updateProfile(
            @RequestHeader(value = "x-test-user", required = false) String xTestUser,
            @RequestHeader(value = "Authorization", required = false) String auth,
            @RequestBody Student payload) {

        String username = resolveUsername(xTestUser, auth);
        log.info("PUT /profile for username={} payload={}", username, payload);
        if (username == null)
            return ResponseEntity.badRequest().body("Missing user header");

        try {
            Student s = studentRepository.findByUsername(username).orElseGet(() -> {
                Student n = new Student();
                n.setUsername(username);
                n.setRole("student");
                return n;
            });

            if (payload.getName() != null)
                s.setName(payload.getName());
            if (payload.getEmail() != null)
                s.setEmail(payload.getEmail());
            if (payload.getPhone() != null)
                s.setPhone(payload.getPhone());
            if (payload.getRole() != null)
                s.setRole(payload.getRole());

            Student saved = studentRepository.saveAndFlush(s); // flush immediately
            log.info("Profile saved for username={} id={}", saved.getUsername(), saved.getId());
            return ResponseEntity.ok(saved);
        } catch (Exception ex) {
            log.error("Failed to save profile for username={}", username, ex);
            return ResponseEntity.status(500).body("Server error");
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader(value = "x-test-user", required = false) String xTestUser,
            @RequestHeader(value = "Authorization", required = false) String auth) {
        String username = resolveUsername(xTestUser, auth);
        log.info("GET /profile request for username={}", username);
        if (username == null)
            return ResponseEntity.badRequest().body("Missing user header");
        return studentRepository.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok().body(new Student()));
    }

    // 📝 Submit room change request
    @PostMapping("/room-change")
    public RoomChangeRequest requestRoomChange(@RequestBody RoomChangeRequest request) {
        request.setStatus("PENDING");
        return roomChangeRepo.save(request);
    }

    // 📋 View all own requests
    @GetMapping("/room-change")
    public List<RoomChangeRequest> getRequests() {
        return roomChangeRepo.findAll();
    }

    // 🗣 Add Complaint
    // @PostMapping("/complaints")
    // public Complaint addComplaint(@RequestBody Complaint complaint) {
    // return complaintRepository.save(complaint);
    // }

    // 📋 Get Complaints for a student
    // @GetMapping("/complaints")
    // public List<Complaint> getComplaints(@RequestHeader("Authorization") String
    // token) {
    // // Normally you'd extract username from JWT token
    // return complaintRepository.findAll(); // or findByStudentName(username)
    // }

    // 📢 Get Notices (same as for students)
    // Note: student notices endpoint is provided by `StudentNoticeController`.

    // 🏠 Get Room Details for logged-in student
    @GetMapping("/room")
    public ResponseEntity<?> getRoom(
            @RequestHeader(value = "x-test-user", required = false) String xTestUser,
            @RequestHeader(value = "Authorization", required = false) String auth) {
        String username = resolveUsername(xTestUser, auth);
        if (username == null)
            return ResponseEntity.badRequest().body("Missing user header");

        try {
            // Get the student record for the logged-in user
            Optional<Student> student = studentRepository.findByUsername(username);
            if (student.isEmpty()) {
                return ResponseEntity.ok().body(new Room()); // Return empty room if student not found
            }

            // Get the room number from student record
            String roomNumber = student.get().getRoomNumber();
            if (roomNumber == null || roomNumber.isEmpty()) {
                return ResponseEntity.ok().body(null); // No room assigned yet
            }

            // Try to find the room by room number
            List<Room> allRooms = roomRepository.findAll();
            for (Room room : allRooms) {
                if (room.getRoomNo() != null && room.getRoomNo().equals(roomNumber)) {
                    return ResponseEntity.ok(room);
                }
            }

            return ResponseEntity.ok().body(null); // Room number not found in database
        } catch (Exception e) {
            log.error("Error fetching room for username={}", username, e);
            return ResponseEntity.status(500).body("Error fetching room details");
        }
    }

    // 💰 Get Fees for the logged-in student
    @GetMapping("/fees")
    public ResponseEntity<?> getFee(
            @RequestHeader(value = "x-test-user", required = false) String xTestUser,
            @RequestHeader(value = "Authorization", required = false) String auth) {
        String username = resolveUsername(xTestUser, auth);
        log.info("GET /fees request for username={}", username);
        
        if (username == null) {
            log.warn("No username found in headers");
            return ResponseEntity.badRequest().body("Missing user header");
        }

        try {
            // Find fee by student username
            List<Fee> allFees = feeRepository.findAll();
            log.info("Total fees in database: {}", allFees.size());
            
            for (Fee fee : allFees) {
                log.debug("Checking fee for student: {}", fee.getStudentName());
                if (fee.getStudentName() != null && fee.getStudentName().equals(username)) {
                    log.info("Found fee for username={}: amount={}, status={}", username, fee.getAmount(), fee.getStatus());
                    return ResponseEntity.ok(fee);
                }
            }

            // Return a default fee object if not found
            log.warn("No fee found for username={}, returning default", username);
            Fee defaultFee = new Fee();
            defaultFee.setStudentName(username);
            defaultFee.setAmount(0);
            defaultFee.setStatus("UNPAID");
            return ResponseEntity.ok(defaultFee);
        } catch (Exception e) {
            log.error("Error fetching fees for username={}", username, e);
            return ResponseEntity.status(500).body("Error fetching fee details: " + e.getMessage());
        }
    }

    // 💳 Pay Fees (mock payment)
    @PostMapping("/fees/pay")
    public ResponseEntity<?> payFee(
            @RequestHeader(value = "x-test-user", required = false) String xTestUser,
            @RequestHeader(value = "Authorization", required = false) String auth) {
        String username = resolveUsername(xTestUser, auth);
        if (username == null)
            return ResponseEntity.badRequest().body("Missing user header");

        try {
            // Find fee by student username
            List<Fee> allFees = feeRepository.findAll();
            for (Fee fee : allFees) {
                if (fee.getStudentName() != null && fee.getStudentName().equals(username)) {
                    fee.setStatus("PAID");
                    feeRepository.save(fee);
                    return ResponseEntity.ok(fee);
                }
            }

            return ResponseEntity.ok().body("Fee record not found");
        } catch (Exception e) {
            log.error("Error paying fees for username={}", username, e);
            return ResponseEntity.status(500).body("Error processing payment");
        }
    }

    // ✅ Delete a complaint by ID
    @DeleteMapping("/complaints/{id}")
    public void deleteComplaint(@PathVariable Long id) {
        complaintRepository.deleteById(id);
    }

}
