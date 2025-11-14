package com.example.hmsbe.controller;

import com.example.hmsbe.model.Fee;
import com.example.hmsbe.repo.FeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/fees")
@CrossOrigin(origins = {"http://localhost:3000", "http://hmsclg.netlify.app", "https://hmsclg.netlify.app"})
public class FeeController {

    @Autowired
    private FeeRepository feeRepository;

    // ✅ Add Fee Record
    @PostMapping
    public Fee addFee(@RequestBody Fee fee) {
        return feeRepository.save(fee);
    }

    // ✅ Get All Fee Records
    @GetMapping
    public List<Fee> getAllFees() {
        return feeRepository.findAll();
    }

    // ✅ Delete Fee Record
    @DeleteMapping("/{id}")
    public void deleteFee(@PathVariable Long id) {
        feeRepository.deleteById(id);
    }
}


