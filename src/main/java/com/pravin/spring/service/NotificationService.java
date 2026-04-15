package com.pravin.spring.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @RateLimiter(name = "notificationService", fallbackMethod = "notificationFallback")
    @Bulkhead(name = "notificationService", type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "notificationFallback")
    public String sendNotification() throws InterruptedException {
        throw new RuntimeException("Service Down");
    }

    public String notificationFallback(Throwable t) {
        return "Notification fallback: " + t.getMessage();
    }

}