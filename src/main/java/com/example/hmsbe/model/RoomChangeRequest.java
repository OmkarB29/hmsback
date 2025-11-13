package com.example.hmsbe.model;

import jakarta.persistence.*;

@Entity
public class RoomChangeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentName;
    private String currentRoom;
    private String requestedRoom;
    private String reason;
    private String status = "PENDING"; // PENDING | APPROVED | REJECTED

    public RoomChangeRequest() {}

    public RoomChangeRequest(String studentName, String currentRoom, String requestedRoom, String reason) {
        this.studentName = studentName;
        this.currentRoom = currentRoom;
        this.requestedRoom = requestedRoom;
        this.reason = reason;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getCurrentRoom() { return currentRoom; }
    public void setCurrentRoom(String currentRoom) { this.currentRoom = currentRoom; }
    public String getRequestedRoom() { return requestedRoom; }
    public void setRequestedRoom(String requestedRoom) { this.requestedRoom = requestedRoom; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}


