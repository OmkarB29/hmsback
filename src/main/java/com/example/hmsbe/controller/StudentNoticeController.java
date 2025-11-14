package com.example.hmsbe.controller;

import com.example.hmsbe.model.Notice;
import com.example.hmsbe.repo.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = {"http://localhost:3000", "http://hmsclg.netlify.app", "https://hmsclg.netlify.app"})
public class StudentNoticeController {

    @Autowired
    private NoticeRepository noticeRepo;

    @GetMapping("/notices")
    public List<Notice> getAllNoticesForStudents() {
        return noticeRepo.findAll();
    }
}


