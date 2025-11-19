package com.example.hmsbe.controller;

import com.example.hmsbe.model.Complaint;
import com.example.hmsbe.repo.ComplaintRepository;
import com.example.hmsbe.repo.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/complaints")
@CrossOrigin(origins = {"http://localhost:3000", "http://hmsclg.netlify.app", "https://hmsclg.netlify.app"})
public class StudentComplaintController {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private StudentRepository studentRepository;

    // 🔹 Add new complaint
    @PostMapping
    public Complaint submitComplaint(@RequestBody Complaint complaint) {
        // Default status when a student submits a complaint
        complaint.setStatus("Pending");
        // If frontend sent username in studentName, convert to student's real name for admin view
        if (complaint.getStudentName() != null && !complaint.getStudentName().isBlank()) {
            studentRepository.findByUsername(complaint.getStudentName()).ifPresent(s ->
                    complaint.setStudentName(s.getName() != null && !s.getName().isBlank() ? s.getName() : s.getUsername())
            );
        }
        return complaintRepository.save(complaint);
    }

    // 🔹 Get all complaints (for that student or admin — you can filter later)
    @GetMapping
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }
}


