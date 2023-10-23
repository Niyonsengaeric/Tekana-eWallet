package com.java.TekanaeWallet.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.TekanaeWallet.Exceptions.BadRequestException;
import com.java.TekanaeWallet.Exceptions.NotFoundException;
import com.java.TekanaeWallet.Model.Customer;
import com.java.TekanaeWallet.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> addCustomer(@Valid @RequestBody Customer customer) {
        if (customerService.existsByEmail(customer.getEmail())) {
            throw new BadRequestException("email already registered");
        }
        return new ResponseEntity<>(customerService.addCustomer(customer), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request) {
        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> tokens = oMapper.convertValue(request.getAttribute("data"), Map.class);
        Map<String, Object> data = new HashMap<>();
        for (Map.Entry<String, Object> token : tokens.entrySet()) {
            data.put(token.getKey(), token.getValue().toString());
        }
        return ResponseEntity.ok(data);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Customer>> getCustomers() {
        List<Customer> customers = customerService.getCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            throw new NotFoundException("customer not found");

        }
        return ResponseEntity.ok(customer);
    }
}
