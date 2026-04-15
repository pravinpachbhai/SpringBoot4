package com.pravin.spring.configuration;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.text.SimpleDateFormat;

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

    @Bean
    public RestClient restClient(){
        JsonMapper jsonMapper = JsonMapper.builder()
                .findAndAddModules()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .defaultDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
                .build();

        return RestClient.builder()
                .configureMessageConverters(client -> {
                    client.registerDefaults().withJsonConverter(new JacksonJsonHttpMessageConverter(jsonMapper));
                })
                .build();
    }


}