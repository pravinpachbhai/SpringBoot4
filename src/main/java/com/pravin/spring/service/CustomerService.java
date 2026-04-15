package com.pravin.spring.service;

import com.pravin.spring.dto.CustomerDto;
import com.pravin.spring.dto.PagedResponse;
import com.pravin.spring.dto.RegisterCustomer;
import com.pravin.spring.dto.UpdateCustomer;
import com.pravin.spring.entity.Customer;
import com.pravin.spring.exception.CustomerNotFoundException;
import com.pravin.spring.exception.CustomerSaveException;
import com.pravin.spring.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {
    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    @Cacheable(value = "customerList", key = "'all'")
    public List<CustomerDto> getCustomers() {
        log.info("Fetching all customers");
        return customerRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Cacheable(value = "customerList", key = "'page:' + #pageable.pageNumber + ':' + #pageable.pageSize + ':' + #pageable.sort.toString()")
    public PagedResponse<CustomerDto> getCustomers(Pageable pageable) {
        log.info("Fetching customers page: {}", pageable);
        Page<CustomerDto> page = customerRepository.findAll(pageable)
                .map(this::toDto);

        return new PagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    @Cacheable(value = "customer", key = "#id")
    public CustomerDto getCustomerById(Integer id) {
        return customerRepository.findById(id).map(this::toDto).orElseThrow(() -> new CustomerNotFoundException(
                "Customer not found with id: " + id
        ));
    }

    @Transactional
    @CacheEvict(value = "customerList", allEntries = true)
    @CachePut(value = "customer", key = "#result.id")
    public CustomerDto register(RegisterCustomer registerCustomer) {
        log.info("Registering customer: {}", registerCustomer.email());
        customerRepository.findByEmailIgnoreCase(registerCustomer.email())
                .ifPresent(c -> {
                    throw new CustomerSaveException(
                            "Customer already exists with email: " + registerCustomer.email()
                    );
                });

        Customer user = new Customer();
        user.setAddress(registerCustomer.address());
        user.setCity(registerCustomer.city());
        user.setName(registerCustomer.name());
        user.setZipCode(registerCustomer.zipCode());
        user.setEmail(registerCustomer.email());
        user = customerRepository.save(user);
        return toDto(user);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "customer", key = "#id"),
            @CacheEvict(value = "customerList", allEntries = true)
    })
    public CustomerDto updateCustomer(Integer id, UpdateCustomer updateCustomer) {
        log.info("Updating customer id: {}", id);
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(
                "Customer not found with id: " + id
        ));
        customer.setCity(updateCustomer.city());
        customer.setAddress(updateCustomer.address());
        customer.setZipCode(updateCustomer.zipCode());
        customer = customerRepository.save(customer);
        return toDto(customer);
    }

    private CustomerDto toDto(Customer customer) {
        return new CustomerDto(customer.getId(),
                customer.getName(),
                customer.getAddress(),
                customer.getCity(),
                customer.getZipCode(),
                customer.getEmail());
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "customer", key = "#id"),
            @CacheEvict(value = "customerList", allEntries = true)
    })
    public void deleteCustomer(Integer id) {
        log.info("Deleting customer id: {}", id);
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(
                "Customer not found with id: " + id
        ));
        customerRepository.delete(customer);
    }
}
