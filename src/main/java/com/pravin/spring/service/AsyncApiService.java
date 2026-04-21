package com.pravin.spring.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncApiService {

    @Async("taskExecutor")
    public CompletableFuture<String> callUserApi() {
        sleep(1000);
        return CompletableFuture.completedFuture("UserData");
    }

    @Async("taskExecutor")
    public CompletableFuture<String> callOrderApi() {
        sleep(1200);
        return CompletableFuture.completedFuture("OrderData");
    }

    @Async("taskExecutor")
    public CompletableFuture<String> callRecommendationApi() {
        sleep(800);
        return CompletableFuture.completedFuture("RecommendationData");
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
