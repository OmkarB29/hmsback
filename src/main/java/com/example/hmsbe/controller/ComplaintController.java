package com.example.hmsbe.controller;

import com.example.hmsbe.model.Complaint;
import com.example.hmsbe.repo.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "http://localhost:3000")
public class ComplaintController {

    @Autowired
    private ComplaintRepository complaintRepository;

    // 🧑‍🎓 Student submits complaint
    @PostMapping
    public Complaint addComplaint(@RequestBody Complaint complaint) {
        return complaintRepository.save(complaint);
    }

    // 👮‍♂️ Admin/Warden fetch all complaints
    @GetMapping
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    // 👮‍♂️ Update complaint status
    @PutMapping("/{id}")
    public Complaint updateStatus(@PathVariable Long id, @RequestBody Complaint updated) {
        Complaint c = complaintRepository.findById(id).orElseThrow();
        c.setStatus(updated.getStatus());
        return complaintRepository.save(c);
    }

    // 🧹 Delete complaint (optional)
    @DeleteMapping("/{id}")
    public void deleteComplaint(@PathVariable Long id) {
        complaintRepository.deleteById(id);
    }
}


