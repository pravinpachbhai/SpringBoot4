package com.pravin.spring.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "Customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String address;
    private String city;
    private String zipCode;
    private String email;

}
