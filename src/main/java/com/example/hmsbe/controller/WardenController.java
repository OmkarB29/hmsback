package com.example.hmsbe.controller;

import com.example.hmsbe.model.Complaint;
import com.example.hmsbe.model.Notice;
import com.example.hmsbe.model.Student;
import com.example.hmsbe.repo.ComplaintRepository;
import com.example.hmsbe.repo.NoticeRepository;
import com.example.hmsbe.repo.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.hmsbe.model.RoomChangeRequest;
import com.example.hmsbe.repo.RoomChangeRequestRepository;


import java.util.List;

@RestController
@RequestMapping("/api/warden")
@CrossOrigin(origins = {"http://localhost:3000", "http://hmsclg.netlify.app", "https://hmsclg.netlify.app"})
public class WardenController {

    @Autowired
    private ComplaintRepository complaintRepo;

    @Autowired
    private NoticeRepository noticeRepo;
    @Autowired
    private RoomChangeRequestRepository roomChangeRepo;
    @Autowired
    private StudentRepository studentRepo;  // Make sure to import and have repository

    // 🔹 Get all student details
    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }
//    @GetMapping("/students")
//    public List<Student> getAllStudents() {
//        return studentRepo.findAll();
//    }



    // ✅ Get all room change requests
    @GetMapping("/room-change")
    public List<RoomChangeRequest> getAllRoomChangeRequests() {
        return roomChangeRepo.findAll();
    }
    @PutMapping("/students/{id}/room")
    public Student updateStudentRoom(@PathVariable Long id, @RequestParam String roomNo) {
        Student student = studentRepo.findById(id).orElseThrow();
        student.setRoomNumber(roomNo); // ✅ corrected field name
        return studentRepo.save(student);
    }


    @PutMapping("/students/{id}/fees")
    public Student updateStudentFeeStatus(@PathVariable Long id, @RequestParam String status) {
        Student student = studentRepo.findById(id).orElseThrow();
        // Note: Fee status management should be handled through FeeRepository, not Student.fee
        // For now, just returning the student as-is
        return student;
    }

    // ✅ Approve request
    @PutMapping("/room-change/{id}/approve")
    public RoomChangeRequest approveRequest(@PathVariable Long id) {
        RoomChangeRequest req = roomChangeRepo.findById(id).orElseThrow();
        req.setStatus("APPROVED");
        return roomChangeRepo.save(req);
    }

    // ✅ Reject request
    @PutMapping("/room-change/{id}/reject")
    public RoomChangeRequest rejectRequest(@PathVariable Long id) {
        RoomChangeRequest req = roomChangeRepo.findById(id).orElseThrow();
        req.setStatus("REJECTED");
        return roomChangeRepo.save(req);
    }


    // 🔹 Get all complaints
    @GetMapping("/complaints")
    public List<Complaint> getAllComplaints() {
        return complaintRepo.findAll();
    }

    // 🔹 Update complaint status
    @PutMapping("/complaints/{id}/resolve")
    public Complaint resolveComplaint(@PathVariable Long id) {
        Complaint c = complaintRepo.findById(id).orElseThrow();
        c.setStatus("RESOLVED");
        return complaintRepo.save(c);
    }

    // 🔹 Get all notices
    @GetMapping("/notices")
    public List<Notice> getAllNotices() {
        return noticeRepo.findAll();
    }

    // 🔹 Add a new notice
    @PostMapping("/notices")
    public Notice addNotice(@RequestBody Notice notice) {
        return noticeRepo.save(notice);
    }

    // 🔹 Delete a notice
    @DeleteMapping("/notices/{id}")
    public void deleteNotice(@PathVariable Long id) {
        noticeRepo.deleteById(id);
    }
}


