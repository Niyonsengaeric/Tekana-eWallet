package com.java.TekanaeWallet.Repo;

import com.java.TekanaeWallet.Model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    List<Wallet> findAllByCustomer_Id(Long customerId);
    Optional<Wallet> findByAccountNumber(String accountNumber);
    List<Wallet> findAllByOrderByCreatedAtDesc();
}
