package com.pravin;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class SpringApplication {
/*
 http://localhost:8080/api-docs
 http://localhost:8080/swagger-ui/index.html
 http://localhost:8080/actuator/circuitbreakers
 http://localhost:8080/actuator/health
 http://localhost:8080/actuator/metrics/resilience4j.circuitbreaker.calls
 http://localhost:8080/actuator/ratelimiters
 http://localhost:8080/actuator/bulkheads
 http://localhost:8080/actuator/metrics/resilience4j.ratelimiter.calls
 http://localhost:8080/actuator/prometheus
 GET /api/customers?page=0&size=10&sort=name,asc
 GET /api/customers?sort=city,desc

 INSERT INTO person (id, name, address, city, zip_code) VALUES (1, 'Pravin', 'address 1', 'St Louis', 12345);
 INSERT INTO person (id, name, address, city, zip_code) VALUES (2, 'John', 'address 2', 'St Louis', 12345);
*/


	public static void main(String[] args) {
		org.springframework.boot.SpringApplication.run(SpringApplication.class, args);
	}

}
