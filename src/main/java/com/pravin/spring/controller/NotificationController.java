package com.pravin.spring.controller;

import com.pravin.spring.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @GetMapping("/notify")
    public ResponseEntity<String> notifyUser() throws InterruptedException {
        return ResponseEntity.status(HttpStatus.OK).body(service.sendNotification());
    }
}