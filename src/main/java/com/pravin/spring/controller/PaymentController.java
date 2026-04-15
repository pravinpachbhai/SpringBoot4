package com.pravin.spring.controller;

import com.pravin.spring.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    Client Request
       ↓
    Retry (maxAttempts=3)
       ↓
    CircuitBreaker
       ↓
    Bulkhead (limit concurrent calls)
       ↓
    Actual method
       ↓
    Fallback (if any stage fails)
*/

@RestController
@RequestMapping("/api/v1")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @GetMapping("/payment")
    public ResponseEntity<String> payment() {
        return ResponseEntity.status(HttpStatus.OK).body(service.callPaymentService());
    }
}

