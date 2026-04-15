package com.pravin.spring.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class InventoryService {
    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);
    private final WebClient webClient;
    private final RestClient restClient;
    public InventoryService(WebClient webClient, RestClient restClient) {
        this.webClient = webClient;
        this.restClient = restClient;
    }

    @Cacheable(value = "productCache", key = "#quantity")
    @Retry(name = "inventoryService", fallbackMethod = "getInventoryFallback")
    @CircuitBreaker(name = "inventoryService", fallbackMethod = "getInventoryFallback")
    @Bulkhead(name = "inventoryService", type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "getInventoryFallback")
    public String getInventory(String quantity) {
        log.info("Calling https://fakerapi.it/api/v2/addresses?_quantity=1");
        return restClient.get().uri("https://fakerapi.it/api/v2/addresses?_quantity={quantity}", quantity).retrieve()
                .body(String.class);
    }


    @Cacheable(value = "productCache", key = "#quantity")
    @Retry(name = "inventoryService", fallbackMethod = "getInventoryFallback")
    @CircuitBreaker(name = "inventoryService", fallbackMethod = "getInventoryFallback")
    @Bulkhead(name = "inventoryService", type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "getInventoryFallback")
    public Mono<String> getInventory_WebClient(String quantity) {
        log.info("Calling https://fakerapi.it/api/v2/addresses?_quantity=1");
        return webClient.get().uri("https://fakerapi.it/api/v2/addresses?_quantity=", quantity).retrieve()
                .bodyToMono(String.class);
    }

    public String getInventoryFallback(String quantity, Throwable t) {
        log.error("Fallback triggered due to error", t);
        return "Fallback: Inventory Service unavailable";
    }
}