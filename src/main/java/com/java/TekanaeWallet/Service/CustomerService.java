package com.java.TekanaeWallet.Service;

import com.java.TekanaeWallet.Model.Customer;

import java.util.List;

public interface CustomerService {
    Customer addCustomer(Customer customer);
    boolean existsByEmail(String email);
    boolean existsByNationalId(String nationalId);
    List<Customer> getCustomers ();

    Customer getCustomerById(Long customerId);

    Customer findByEmail(String customerEmail);
}
