package com.defers.camel.domain.customer.usecase;

import com.defers.camel.domain.customer.port.in.CustomerUseCase;
import com.example.customerservice.Customer;
import com.example.customerservice.CustomerType;
import java.math.BigDecimal;
import java.util.List;

public class CustomerService implements CustomerUseCase {
    @Override
    public List<Customer> find(String name) {
        var mockCustomer = new Customer();
        mockCustomer.setName("Buyer1");
        mockCustomer.setRevenue(5);
        mockCustomer.setTest(BigDecimal.valueOf(15));
        mockCustomer.setNumOrders(10);
        mockCustomer.setType(CustomerType.PRIVATE);
        return List.of(mockCustomer);
    }
}
