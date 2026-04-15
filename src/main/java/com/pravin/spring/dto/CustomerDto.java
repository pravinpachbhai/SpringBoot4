package com.pravin.spring.dto;

public record CustomerDto( Integer id,
         String name,
         String address,
         String city,
         String zipCode,
         String email) {
}
