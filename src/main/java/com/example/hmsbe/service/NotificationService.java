package com.example.hmsbe.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;

@Service
public class NotificationService {
    private final List<SseEmitter> emitters = Collections.synchronizedList(new ArrayList<>());

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        return emitter;
    }

    public void sendRoomAssignment(Long studentId, String roomNo) {
        List<SseEmitter> dead = new ArrayList<>();
        synchronized (emitters) {
            for (SseEmitter emitter : emitters) {
                try {
                    Map<String, Object> payload = new HashMap<>();
                    payload.put("studentId", studentId);
                    payload.put("roomNo", roomNo);
                    emitter.send(SseEmitter.event().name("room-assigned").data(payload));
                } catch (Exception ex) {
                    dead.add(emitter);
                }
            }
            emitters.removeAll(dead);
        }
    }
}
