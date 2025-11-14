package com.example.hmsbe.controller;

import com.example.hmsbe.model.Student;
import com.example.hmsbe.model.Room;
import com.example.hmsbe.model.Fee;
import com.example.hmsbe.repo.StudentRepository;
import com.example.hmsbe.repo.RoomRepository;
import com.example.hmsbe.repo.FeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = {"http://localhost:3000", "http://hmsclg.netlify.app", "https://hmsclg.netlify.app"})
public class AdminController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private FeeRepository feeRepository;

    /**
     * Get all students
     */
    @GetMapping("/all-students")
    public ResponseEntity<?> getAllStudents() {
        try {
            List<Student> students = studentRepository.findAll();
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch students: " + e.getMessage()));
        }
    }

    /**
     * Get all rooms
     */
    @GetMapping("/all-rooms")
    public ResponseEntity<?> getAllRooms() {
        try {
            List<Room> rooms = roomRepository.findAll();
            return ResponseEntity.ok(rooms);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch rooms: " + e.getMessage()));
        }
    }

    /**
     * Get all fees
     */
    @GetMapping("/all-fees")
    public ResponseEntity<?> getAllFees() {
        try {
            List<Fee> fees = feeRepository.findAll();
            return ResponseEntity.ok(fees);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch fees: " + e.getMessage()));
        }
    }

    /**
     * Get pending registrations (users with STUDENT role who don't have student
     * records yet)
     */
    @GetMapping("/pending-registrations")
    public ResponseEntity<?> getPendingRegistrations() {
        try {
            // This could be enhanced based on your registration workflow
            // For now, returning an empty list
            return ResponseEntity.ok(List.of());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Failed to fetch pending registrations: " + e.getMessage()));
        }
    }

    /**
     * Get dashboard overview statistics
     */
    @GetMapping("/overview")
    public ResponseEntity<?> overview() {
        try {
            long totalStudents = studentRepository.count();
            long totalRooms = roomRepository.count();
            long totalFees = feeRepository.count();

            double totalFeesCollected = feeRepository.findAll()
                    .stream()
                    .mapToDouble(Fee::getAmount)
                    .sum();

            return ResponseEntity.ok(Map.of(
                    "totalStudents", totalStudents,
                    "totalRooms", totalRooms,
                    "totalFees", totalFees,
                    "totalFeesCollected", totalFeesCollected,
                    "pendingRegistrations", 0));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to fetch overview: " + e.getMessage()));
        }
    }

    /**
     * Allocate room to a student
     */
    @PutMapping("/allocate-room/{studentId}")
    public ResponseEntity<?> allocateRoom(@PathVariable Long studentId, @RequestBody Map<String, String> request) {
        try {
            String roomNumber = request.get("roomNumber");
            if (roomNumber == null || roomNumber.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Room number is required"));
            }

            // Find student
            Student student = studentRepository.findById(studentId)
                    .orElse(null);
            if (student == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Student not found"));
            }

            // Update student's room number
            student.setRoomNumber(roomNumber);
            Student updatedStudent = studentRepository.save(student);

            return ResponseEntity.ok(Map.of(
                    "message", "Room allocated successfully",
                    "student", Map.of(
                            "id", updatedStudent.getId(),
                            "username", updatedStudent.getUsername(),
                            "name", updatedStudent.getName(),
                            "roomNumber", updatedStudent.getRoomNumber())));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to allocate room: " + e.getMessage()));
        }
    }
}
