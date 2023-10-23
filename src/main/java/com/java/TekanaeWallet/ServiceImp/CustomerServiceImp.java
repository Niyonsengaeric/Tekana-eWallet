package com.java.TekanaeWallet.ServiceImp;

import com.java.TekanaeWallet.Exceptions.AuthException;
import com.java.TekanaeWallet.Exceptions.BadRequestException;
import com.java.TekanaeWallet.Model.Customer;
import com.java.TekanaeWallet.Repo.CustomerRepository;
import com.java.TekanaeWallet.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImp implements CustomerService, UserDetailsService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerServiceImp(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Customer addCustomer(Customer customer) {
        if (existsByEmail(customer.getEmail()) || existsByNationalId(customer.getNationalId())) {
            throw new BadRequestException("Customer with the same email or national ID already exists.");
        }
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
        return customerRepository.save(customer);
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByNationalId(String nationalId) {
        return customerRepository.existsByNationalId(nationalId);
    }

    @Override
    public List<Customer> getCustomers() {
        return customerRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Customer getCustomerById(Long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        return customer.orElse(null);
    }

    @Override
    public Customer findByEmail(String customerEmail) {
        return customerRepository.findByEmail(customerEmail);
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Customer user = customerRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        if (!user.isStatus()) {
            throw new AuthException("User cannot log into the system. Please contact the system admin.");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
