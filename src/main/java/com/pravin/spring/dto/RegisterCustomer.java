package com.pravin.spring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterCustomer(@NotBlank(message = "Name is mandatory") String name,
                               @NotBlank(message = "Address is mandatory")
                               String address,
                               @NotBlank(message = "City is mandatory")
                               String city,
                               @NotBlank(message = "zipCode is mandatory")
                               @Size(min = 5, max = 5, message = "Zipcode must be exactly 5 characters")
                               String zipCode,
                               @NotBlank(message = "Email is mandatory")
                               String email) {
}
