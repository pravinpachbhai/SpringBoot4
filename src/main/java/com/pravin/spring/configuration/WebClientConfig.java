package com.pravin.spring.configuration;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .filter((request, next) -> {
                    String correlationId = MDC.get("correlationId");
                    return next.exchange(
                            ClientRequest.from(request)
                                    .header("X-Correlation-Id", correlationId)
                                    .build()
                    );
                })
                .build();
    }
}