package com.pravin.spring.controller;

import com.pravin.spring.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping("/order")
    public ResponseEntity<String> order() throws InterruptedException {
        return ResponseEntity.status(HttpStatus.OK).body(service.placeOrder());
    }

}