package com.pravin.spring.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @RateLimiter(name = "orderService", fallbackMethod = "orderFallback")
    @CircuitBreaker(name = "orderService", fallbackMethod = "orderFallback")
    @Bulkhead(name = "orderService", type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "orderFallback")
    public String placeOrder() throws InterruptedException {
        throw new RuntimeException("Service Down");
    }

    public String orderFallback(Throwable t) {
        return "Order fallback: " + t.getMessage();
    }


}