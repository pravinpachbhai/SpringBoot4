package com.pravin.spring.dto;

import java.time.LocalDateTime;

public record CustomError(LocalDateTime date, Integer errorCode, String message) {
}
