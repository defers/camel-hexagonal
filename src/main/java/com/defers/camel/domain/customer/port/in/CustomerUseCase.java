package com.defers.camel.domain.customer.port.in;

import com.example.customerservice.Customer;
import java.util.List;

public interface CustomerUseCase {
    List<Customer> find(String name);
}
