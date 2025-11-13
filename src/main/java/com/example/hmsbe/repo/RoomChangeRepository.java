package com.example.hmsbe.repo;

import com.example.hmsbe.model.RoomChange;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomChangeRepository extends JpaRepository<RoomChange, Long> {
    List<RoomChange> findByStudentUsername(String studentUsername);
}

