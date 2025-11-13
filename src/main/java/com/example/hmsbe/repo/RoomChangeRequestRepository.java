package com.example.hmsbe.repo;

import com.example.hmsbe.model.RoomChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomChangeRequestRepository extends JpaRepository<RoomChangeRequest, Long> {
}


