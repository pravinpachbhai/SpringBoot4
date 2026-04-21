package com.pravin.spring.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class ParallelCallService {

    private static String callUserApi() {
        sleep(1000);
        return "UserData";
    }

    private static String callOrderApi() {
        sleep(1200);
        return "OrderData";
    }

    private static String callRecommendationApi() {
        sleep(800);
        return "RecommendationData";
    }

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            IO.println(e);
        }
    }

    public static void main(String[] args) throws Exception {
        ParallelCallService service = new ParallelCallService();
        System.out.println(service.getData());
        System.out.println(service.getDataWait());
    }

    public String getData() throws ExecutionException, InterruptedException {

        CompletableFuture<String> userFuture =
                CompletableFuture.supplyAsync(ParallelCallService::callUserApi);

        CompletableFuture<String> orderFuture =
                CompletableFuture.supplyAsync(ParallelCallService::callOrderApi);

        CompletableFuture<String> recFuture =
                CompletableFuture.supplyAsync(ParallelCallService::callRecommendationApi);

        CompletableFuture<String> combinedFuture =
                userFuture.thenCombine(orderFuture, (user, orders) -> user + " | " + orders)
                        .thenCombine(recFuture, (combined, recs) -> combined + " | " + recs);

        return combinedFuture.get();
    }

    public String getDataWait() throws ExecutionException, InterruptedException {

        CompletableFuture<String> userFuture = callUserApiWait();
        CompletableFuture<String> orderFuture = callOrderApiWait();
        CompletableFuture<String> recFuture = callRecommendationApiWait();

        CompletableFuture<Void> allFutures =
                CompletableFuture.allOf(userFuture, orderFuture, recFuture);

        CompletableFuture<String> resultFuture = allFutures.thenApply(v ->
                userFuture.join() + " | " +
                        orderFuture.join() + " | " +
                        recFuture.join()
        );

        return resultFuture.get();
    }

    private CompletableFuture<String> callUserApiWait() {
        return CompletableFuture.supplyAsync(ParallelCallService::callUserApi);
    }

    private CompletableFuture<String> callOrderApiWait() {
        return CompletableFuture.supplyAsync(ParallelCallService::callOrderApi);
    }

    private CompletableFuture<String> callRecommendationApiWait() {
        return CompletableFuture.supplyAsync(ParallelCallService::callRecommendationApi);
    }
}
