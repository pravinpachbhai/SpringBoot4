package com.pravin.spring.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    /*
        Annotation order matters
         * Retry first → reduces failures   ---- Retry fixes transient issues
         * CircuitBreaker second → stops cascade  --- CircuitBreaker stops repeated failures
         * RateLimiter third -> protects downstream
         * Bulkhead last → protects resources  ---- Bulkhead protects resources
     */

    @Retry(name = "paymentService", fallbackMethod = "paymentFallback")
    @CircuitBreaker(name = "paymentService", fallbackMethod = "paymentFallback")
    @RateLimiter(name = "paymentService", fallbackMethod = "paymentFallback")
    @Bulkhead(name = "paymentService", type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "paymentFallback")
    public String callPaymentService() {
        log.info("Calling payment provider");
        throw new RuntimeException("Service Down");
    }

    public String paymentFallback(Throwable t) {
        log.error("Fallback triggered due to error", t);
        return "Payment Fallback: " + t.getMessage();
    }

}