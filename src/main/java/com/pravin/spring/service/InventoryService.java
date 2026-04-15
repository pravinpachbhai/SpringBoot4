package com.pravin.spring.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class InventoryService {
    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);
    private final WebClient webClient;

    public InventoryService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Cacheable(value = "productCache", key = "#productId")
    @Retry(name = "inventoryService", fallbackMethod = "getInventoryFallback")
    @CircuitBreaker(name = "inventoryService", fallbackMethod = "getInventoryFallback")
    @Bulkhead(name = "inventoryService", type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "getInventoryFallback")
    public Mono<String> getInventory(String productId) {
        log.info("Calling https://fakerapi.it/api/v2/addresses?_quantity=1");
        return webClient.get().uri("https://fakerapi.it/api/v2/addresses?_quantity=1", productId).retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getInventoryFallback(String productId, Throwable t) {
        log.error("Fallback triggered due to error", t);
        String response = "Fallback: Inventory Service unavailable";
        return Mono.just(response);
    }
}