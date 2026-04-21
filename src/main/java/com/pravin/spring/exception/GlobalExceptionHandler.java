package com.pravin.spring.exception;

import io.opentelemetry.api.trace.Span;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handleCustomerNotFound(
            CustomerNotFoundException ex) {
        log.error("Customer not found: {}", ex.getMessage(), ex);
        String traceId = Span.current().getSpanContext().getTraceId();
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Customer Not Found");
        problem.setDetail("The requested customer does not exist.");
        problem.setProperty("traceId", traceId);
        return problem;
    }

    @ExceptionHandler(CustomerSaveException.class)
    public ProblemDetail handleCustomerSaveException(
            CustomerSaveException ex) {
        log.error("Error while saving Customer: {}", ex.getMessage(), ex);
        String traceId = Span.current().getSpanContext().getTraceId();
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Customer Save Failed");
        problem.setDetail("Unable to process request. Please check input and try again.");
        problem.setProperty("traceId", traceId);
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        String traceId = Span.current().getSpanContext().getTraceId();
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("Internal Server Error");
        problem.setDetail("Something went wrong. Please contact support.");
        problem.setProperty("traceId", traceId);
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        log.error("Validation failed", ex);
        String traceId = Span.current().getSpanContext().getTraceId();
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validation failed");
        problem.setDetail("One or more fields are invalid");
        problem.setProperty("traceId", traceId);
        problem.setProperty("traceId", UUID.randomUUID().toString());
        List<Map<String, String>> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            Map<String, String> fieldError = new HashMap<>();
            fieldError.put("field", error.getField());
            fieldError.put("message", error.getDefaultMessage());
            fieldError.put("code", error.getCode());
            errors.add(fieldError);
        });
        problem.setProperty("errors", errors);
        return problem;
    }

}
