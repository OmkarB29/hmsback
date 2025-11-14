package com.example.hmsbe.controller;

import com.example.hmsbe.model.Complaint;
import com.example.hmsbe.repo.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/complaints")
@CrossOrigin(origins = {"http://localhost:3000", "http://hmsclg.netlify.app", "https://hmsclg.netlify.app"})
public class StudentComplaintController {

    @Autowired
    private ComplaintRepository complaintRepository;

    // 🔹 Add new complaint
    @PostMapping
    public Complaint submitComplaint(@RequestBody Complaint complaint) {
        // Default status when a student submits a complaint
        complaint.setStatus("Pending");
        return complaintRepository.save(complaint);
    }

    // 🔹 Get all complaints (for that student or admin — you can filter later)
    @GetMapping
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }
}


