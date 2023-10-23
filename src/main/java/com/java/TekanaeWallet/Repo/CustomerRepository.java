package com.java.TekanaeWallet.Repo;

import com.java.TekanaeWallet.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);
    boolean existsByNationalId(String nationalId);
    Customer findByEmail(String email);
    List<Customer> findAllByOrderByCreatedAtDesc();
}
