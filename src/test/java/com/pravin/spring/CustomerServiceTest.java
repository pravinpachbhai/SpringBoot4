package com.pravin.spring;

import com.pravin.spring.dto.CustomerDto;
import com.pravin.spring.entity.Customer;
import com.pravin.spring.repository.CustomerRepository;
import com.pravin.spring.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository personRepository;

    @InjectMocks
    private CustomerService personService;

    @Test
    public void testGetPersons(){
        when(personRepository.findAll()).thenReturn(Arrays.asList(new Customer()));
        List<CustomerDto> persons = personService.getCustomers();
        assertNotNull(persons);
    }

}
