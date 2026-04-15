package com.pravin.spring.controller;

import com.pravin.spring.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping("/inventory/{quantity}")
    public ResponseEntity<String> inventory(@PathVariable("quantity") String quantity) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getInventory(quantity));
    }
}

