package com.pravin.spring.service;

import com.pravin.spring.service.AsyncApiService;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class AggregatorService {

    private final AsyncApiService apiService;

    public AggregatorService(AsyncApiService apiService) {
        this.apiService = apiService;
    }

    public String getCombinedData() throws Exception {

        CompletableFuture<String> userFuture =
                apiService.callUserApi()
                        .completeOnTimeout("UserTimeout", 2, TimeUnit.SECONDS)
                        .exceptionally(ex -> "UserError");

        CompletableFuture<String> orderFuture =
                apiService.callOrderApi()
                        .completeOnTimeout("OrderTimeout", 2, TimeUnit.SECONDS)
                        .exceptionally(ex -> "OrderError");

        CompletableFuture<String> recFuture =
                apiService.callRecommendationApi()
                        .completeOnTimeout("RecTimeout", 2, TimeUnit.SECONDS)
                        .exceptionally(ex -> "RecError");

        CompletableFuture<Void> all =
                CompletableFuture.allOf(userFuture, orderFuture, recFuture);

        return all.thenApply(v ->
                userFuture.join() + " | " +
                        orderFuture.join() + " | " +
                        recFuture.join()
        ).get();
    }
}