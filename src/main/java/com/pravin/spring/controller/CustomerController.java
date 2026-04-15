package com.pravin.spring.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.pravin.spring.dto.CustomerDto;
import com.pravin.spring.dto.PagedResponse;
import com.pravin.spring.dto.RegisterCustomer;
import com.pravin.spring.dto.UpdateCustomer;
import com.pravin.spring.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@Tag(name = "Customer Controller", description = "APIs for customer management")
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @Operation(summary = "Get paginated customers", description = "Fetches a paginated list of customers with optional sorting and paging parameters")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved paginated customers", content = @Content(schema = @Schema(implementation = PagedResponse.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error",content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<PagedResponse<CustomerDto>>  getCustomers(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Get paginated customers");
        return ResponseEntity.ok(customerService.getCustomers(pageable));
    }

    @GetMapping(value = "/all")
    @Operation(summary = "Get all customers", description = "Fetches a list of all customers")
    @ApiResponse(responseCode = "200", description = "List of all customers", content = @Content(schema = @Schema(implementation = CustomerDto.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<List<CustomerDto>> getCustomers() {
        log.info("Get all customers");
        return ResponseEntity.ok(customerService.getCustomers());
    }


    @GetMapping(value = "/{id}")
    @Operation(summary = "Get customer by ID", description = "Returns a single customer")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved customer")
    @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getCustomerById(id));
    }

    @PostMapping()
    @Operation(summary = "Create a new customer", description = "Registers a new customer in the system")
    @ApiResponse(responseCode = "201", description = "Customer successfully created", content = @Content(schema = @Schema(implementation = CustomerDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<CustomerDto> saveCustomer(@Valid @RequestBody RegisterCustomer customer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.register(customer));
    }

    @PatchMapping(value = "/{id}")
    @Operation(summary = "Update customer", description = "Partially updates an existing customer")
    @ApiResponse(responseCode = "200", description = "Customer successfully updated", content = @Content(schema = @Schema(implementation = CustomerDto.class)))
    @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable("id") Integer id, @RequestBody UpdateCustomer customer) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.updateCustomer(id, customer));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete customer", description = "Deletes a customer by ID")
    @ApiResponse(responseCode = "204", description = "Customer successfully deleted")
    @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") Integer id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/trace-test")
    public String traceTest() {
        return io.opentelemetry.api.trace.Span.current().getSpanContext().getTraceId();
    }

    @GetMapping("/span-test")
    public String spanTest() {
        return io.opentelemetry.api.trace.Span.current().getSpanContext().getSpanId();
    }

}
