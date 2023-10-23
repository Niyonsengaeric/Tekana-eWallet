package com.java.TekanaeWallet.Repo;

import com.java.TekanaeWallet.Model.WalletActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletActivityLogRepo extends JpaRepository<WalletActivityLog, Long> {
    List<WalletActivityLog> findAllByOrderByCreatedAtDesc();
    List<WalletActivityLog> findByTransactionId(Long transactionId);
}
