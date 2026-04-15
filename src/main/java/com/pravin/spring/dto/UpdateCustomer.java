package com.pravin.spring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateCustomer(
        @NotBlank(message = "Address is mandatory")
        String address,
        @NotBlank(message = "City is mandatory")
        String city,
        @NotBlank(message = "zipCode is mandatory")
        @Size(min = 5, max = 5, message = "Zipcode must be exactly 5 characters")
        @Pattern(regexp = "\\d+")
        String zipCode) {

}

